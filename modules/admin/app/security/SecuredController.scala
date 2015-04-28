package security

import com.core.dal.admin.AdministratorRepository
import com.core.dom.admin.AccessPermission
import com.core.enumeration.AdminRole
import com.core.enumeration.AdminRole._
import com.core.service.admin.AdministratorService
import org.springframework.beans.factory.annotation.Autowired
import play.api.mvc._

class SecuredController extends Controller with Secured {

  @Autowired
  var administratorService: AdministratorService = _
  @Autowired
  var administratorRepository: AdministratorRepository = _


  /*
   *  this function check if a user has a specific permission
   */
  /**
   *
   * Without body parser
   * // Overloaded method to use the default body parser
   **/
  def IsAuthorized(requiredPermission: AccessPermission, role: AdminRole = AdminRole.All)(f: => String => Request[AnyContent] => Result): EssentialAction = {

    IsAuthorized[AnyContent](parse.anyContent)(requiredPermission, role)(f)

  }

  /**
   *
   * With body parser
   **/
  def IsAuthorized[A](b: BodyParser[A])(requiredPermission: AccessPermission, role: AdminRole)(f: => String => Request[A] => Result) = IsAuthenticated(b) {
    administratorID => request => {

      role match {
        case AdminRole.Sudo => {
          Option(administratorRepository.findOneByPermissionIsAdmin(administratorID, requiredPermission, true)) match {
            case Some(customerAdmin) => f(administratorID)(request)
            case None => play.api.mvc.Results.Unauthorized
          }
        }
        case AdminRole.Simple => {
          Option(administratorRepository.findOneByPermissionIsAdmin(administratorID, requiredPermission, false)) match {
            case Some(customerAdmin) => f(administratorID)(request)
            case None => play.api.mvc.Results.Unauthorized
          }
        }
        case AdminRole.All => {
          Option(administratorRepository.findOneByPermission(administratorID, requiredPermission)) match {
            case Some(customerAdmin) => f(administratorID)(request)
            case None => play.api.mvc.Results.Unauthorized
          }
        }

      }

    }

  }


}
