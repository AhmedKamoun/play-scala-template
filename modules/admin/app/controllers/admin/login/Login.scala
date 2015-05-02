package controllers.admin.login

import java.util.{List => JList}

import com.core.service.utils.TokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype
import play.api.Logger
import play.api.data.Forms._
import play.api.data._
import play.api.mvc.Action
import security.SecuredController

@stereotype.Controller
class Login extends SecuredController {

  @Autowired
  var tokenService: TokenService = _

  val logger: Logger = Logger(this.getClass())

  val login_form = Form(
    tuple(
      "email" -> email,
      "password" -> nonEmptyText

    ) verifying("error login, invalid email or password!", result => result match {
      case (email, password) => authenticate(email, password)
    })
  )

  def login() = Action {
    request =>
      request.session.get("APPLICATION.ADMIN_ID") match {
        case Some(user_id) => Ok("you are already logged in!")
        case None => Ok(views.html.login.login(login_form))
      }

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
          val admin = administratorRepository.findByEmail(SucceededForm._1)
          Ok(tokenService.generateToken(Map("admin_id" -> admin.id))) //return token
        }
      )
  }

  def index() = IsAuthenticated {
    admin_id => request =>
      Ok("admin with id " + admin_id + " has a valid token, he can access to this api")
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
    Ok
  }
}