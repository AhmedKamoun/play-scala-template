package controllers.admin.login

import java.util.{List => JList}

import com.core.dal.admin.AdministratorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype
import play.api.Logger
import play.api.data.Forms._
import play.api.data._
import play.api.mvc.{Action, _}
import security.Secured

@stereotype.Controller
class Login extends Controller with Secured {
  @Autowired
  var administratorRepository: AdministratorRepository = _

  val logger: Logger = Logger(this.getClass())

  val login_form = Form(
    tuple(
      "email" -> email,
      "password" -> nonEmptyText

    ) verifying("error login!", result => result match {
      case (email, password) => authenticate(email, password)
    })
  )

  def login() = Action {
    request =>
      request.session.get("APPLICATION.ADMIN_ID") match {
        case Some(user_id) => Redirect(controllers.admin.login.routes.Login.index)
        case None => Ok(views.html.login.login(login_form))
      }

  }

  def index() = IsAuthenticated {
    request => userID =>
      Ok(views.html.login.index())
  }

  def logout() = IsAuthenticated {
    request => userID =>
      Redirect(controllers.admin.login.routes.Login.login()).withNewSession
  }

  def submitLogin() = Action {
    implicit request =>
      login_form.bindFromRequest.fold(
        formWithErrors => {
          Logger.warn("login failed !")
          BadRequest(views.html.login.login(formWithErrors))
        }
        ,
        SucceededForm => {
          Logger.debug("login succeeded !")
          val user = administratorRepository.findByEmail(SucceededForm._1)
          val session = request.session + ("APPLICATION.ADMIN_ID" -> user.id)

          Redirect(controllers.admin.login.routes.Login.index).withSession(session)
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
    Option(administratorRepository.findByEmail(email)) match {
      case Some(user) => {
        //NOTE TO KEEP LOGIN SIMPLE WE DO NOT CHECK FOR PASSWORD
        true
      }
      case None => false
    }
  }


  def options(all: String) = Action {
    Ok("")
  }
}