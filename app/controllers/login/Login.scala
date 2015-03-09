package controllers.login

import java.util.{List => JList}

import entity.person.User
import org.joda.time.{DateTime, DateTimeZone}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype
import play.api.Logger
import play.api.data.Forms._
import play.api.data._
import play.api.mvc.{Action, _}
import repositories.person.UserRepository
import security.Secured
import service.tools

@stereotype.Controller
class Login extends Controller with Secured {
  @Autowired
  var userRepository: UserRepository = _

  val logger: Logger = Logger(this.getClass())

  val login_form = Form(
    tuple(
      "email" -> email,
      "password" -> nonEmptyText

    ) verifying("error login!", result => result match {
      case (email, password) => authenticate(email, password)
    })
  )
  val registration = Form(
    tuple(
      "username" -> nonEmptyText,
      "email" -> email,
      "password" -> nonEmptyText,
      "password_confirm" -> nonEmptyText
    ) verifying("confirm password again!", result => result match {
      case (username, email, password, password_confirm) => checkRegistration(email, password, password_confirm)
    })
  )


  def login() = Action {
    request =>
      request.session.get("APPLICATION.USER_ID") match {
        case Some(user_id) => Redirect(controllers.login.routes.Login.index)
        case None => Ok(views.html.login.login(login_form, registration))
      }

  }

  def index() = IsAuthenticated {
    request => userID =>
      Ok(views.html.login.index())
  }

  def logout() = IsAuthenticated {
    request => userID =>
      Redirect(controllers.login.routes.Login.login()).withNewSession
  }

  def submitLogin() = Action {
    implicit request =>
      login_form.bindFromRequest.fold(
        formWithErrors => {
          Logger.warn("login failed !")
          BadRequest(views.html.login.login(formWithErrors, registration))
        }
        ,
        SucceededForm => {
          Logger.debug("login succeeded !")
          val user = userRepository.findByEmail(SucceededForm._1)
          val session = request.session + ("APPLICATION.USER_ID" -> user.id)

          Redirect(controllers.login.routes.Login.index).withSession(session)
        }
      )
  }

  def submitRegistration() = Action {
    implicit request =>
      registration.bindFromRequest.fold(
        formWithErrors => {
          Logger.warn("registration failed !")
          BadRequest(views.html.login.login(login_form, formWithErrors))
        }
        ,
        SucceededForm => {

          //save new user
          var new_user = new User()
          new_user.salt = tools.getRandomString
          new_user.username = SucceededForm._1
          new_user.email = SucceededForm._2
          new_user.setPasswordHashCode(SucceededForm._3)
          new_user.createdOn = DateTime.now(DateTimeZone.UTC) //relatively to UTC
          userRepository.save(new_user)
          Logger.debug("registration succeeded !")
          val session = request.session + ("APPLICATION.USER_ID" -> new_user.id)
          Redirect(controllers.login.routes.Login.index).withSession(session)
        }
      )
  }


  /**
   *
   *
   * SERVICES
   *
   *
   */

  def authenticate(email: String, password: String): Boolean = {
    // check if user exist
    Option(userRepository.findByEmail(email)) match {
      case Some(user) => {
        user.comparePassword(password)
      }
      case None => false
    }
  }

  def checkRegistration(email: String, password: String, confirm_password: String): Boolean = {
    // check if there is already user with this email
    Option(userRepository.findByEmail(email)) match {
      case Some(user) => false
      case None => {
        // password and confirm_password should be equal
        password.equals(confirm_password)
      }
    }
  }

}