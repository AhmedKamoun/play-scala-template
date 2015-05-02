package security

import authentikat.jwt.JsonWebToken
import com.core.dal.admin.AdministratorRepository
import com.core.dom.admin.AccessPermission
import com.core.enumeration.AdminRole
import com.core.enumeration.AdminRole._
import com.core.service.admin.AdministratorService
import org.springframework.beans.factory.annotation.Autowired
import play.api.Play._
import play.api.mvc._

class SecuredController extends Controller {

  @Autowired
  var administratorService: AdministratorService = _
  @Autowired
  var administratorRepository: AdministratorRepository = _

  /**
   *
   * @param request
   * @return Retrieve the connected user identifier.
   */
  private def getIdentifier(request: RequestHeader) = {
    //http://angular-tips.com/blog/2014/05/json-web-tokens-examples/
    //http://self-issued.info/docs/draft-ietf-oauth-v2-bearer.html#toc
    //TODO -> Format is Authorization: Bearer [token]
    request.headers.get("Authorization") match {
      case Some(jwt) => {
        //verify the token then get admin_id from claim
        val secret_key = current.configuration.getString("application.secret").getOrElse("SecretKey")
        val isValid = JsonWebToken.validate(jwt, secret_key)

        if (isValid) {

          jwt match {
            case JsonWebToken(header, claimsSet, signature) => {

              val claims = claimsSet.asSimpleMap.toOption.getOrElse(Map.empty[String, String])
              //TODO verify if token has expired

              claims.get("admin_id")
            }

            case x => None

          }

        } else None
      }
      case None => None
    }
  }

  /**
   * Redirect to login if the user in not authorized.
   */
  private def onUnauthorized(request: RequestHeader) = Results.Forbidden

  /**
   * Action for authenticated users.
   */
  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = {
    IsAuthenticated[AnyContent](parse.anyContent)(f)

  }

  /**
   *
   * Action  with parser body for authenticated users.
   */

  def IsAuthenticated[A](b: BodyParser[A])(f: => String => Request[A] => Result) =
    Security.Authenticated(getIdentifier, onUnauthorized) {
      administrator =>
        Action(b)(request => f(administrator)(request))
    }

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
