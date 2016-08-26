package controllers.admin

import com.core.dal.admin.AccessPermissionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype
import play.api.Logger
import play.api.Play.current
import play.api.libs.ws.WS
import play.api.mvc.{Action, Results}
import play.mvc.Http.{HeaderNames, MimeTypes}
import security.SecuredController

import scala.concurrent.ExecutionContext.Implicits.global

@stereotype.Controller
class Application extends SecuredController {

  @Autowired
  var accessPermissionRepository: AccessPermissionRepository = _


  val logger: Logger = Logger(this.getClass())


  def getAdmin() = IsAuthorized(accessPermissionRepository.findByIdentifier("user-management")) {
    administratorID => implicit request =>

      Ok("Hello admin!")

  }

  //TEST Scala Object with autowired spring service
  def testService() = Action.async {


    WS.url("https://connect.stripe.com/oauth/token")
      .withQueryString(
        "grant_type" -> "authorization_code")
      .withHeaders(HeaderNames.ACCEPT -> MimeTypes.JSON)
      .post(Results.EmptyContent())
      .map { response =>
        throw new RuntimeException("")
        Ok
      }.recover {
      case exception: Throwable => InternalServerError("SHIT YOU!")
    }

  }
}