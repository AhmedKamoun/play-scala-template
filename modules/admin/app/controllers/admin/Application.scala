package controllers.admin

import com.core.dal.admin.AccessPermissionRepository
import dto.TestDTO
import dto.TestWrites._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.Action
import security.SecuredController

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
  def testService() = Action {
    Ok(Json.toJson(TestDTO()))
  }
}