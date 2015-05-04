package security

import authentikat.jwt.JsonWebToken
import com.core.dal.admin.AdministratorRepository
import com.core.dom.admin.AccessPermission
import com.core.enumeration.AdminRole
import com.core.enumeration.AdminRole._
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import play.api.Play._
import play.api.mvc._
import play.mvc.Http

class SecuredController extends Controller {


  @Autowired
  var administratorRepository: AdministratorRepository = _

  /**
   *
   * @param request
   * @return Retrieve the json web token from Authorization http request header.
   */
  private def getToken(request: RequestHeader) = request.headers.get(Http.HeaderNames.AUTHORIZATION)

  /**
   * Return Forbidden if the user in not authenticated.
   */
  private def onUnauthorized(request: RequestHeader) = Results.Unauthorized.withHeaders(Http.HeaderNames.WWW_AUTHENTICATE -> "token not found in Authorization http request header")

  /**
   * Action for authenticated users.
   * Overloaded method to use the default body parser
   */
  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = IsAuthenticated[AnyContent](parse.anyContent)(f)

  /**
   *
   * Action  with parser body for authenticated users.
   */

  def IsAuthenticated[A](b: BodyParser[A])(f: => String => Request[A] => Result) = {

    Security.Authenticated(getToken, onUnauthorized) {
      JWToken => {

        JWToken match {
          case JsonWebToken(header, claimsSet, signature) => {

            val claims = claimsSet.asSimpleMap.toOption.getOrElse(Map.empty[String, String])
            //to keep it simple for this example we take secret key from application.conf.
            //TODO In prod mode we make more secure solution
            val secret_key = current.configuration.getString("application.secret").getOrElse("BestSecretKey")

            //Check if the jwt is valid
            if (JsonWebToken.validate(JWToken, secret_key)) {

              val admin_id = claims.get("admin_id").getOrElse("")
              val expiration = new DateTime(claims.get("exp").getOrElse(DateTime.now.getMillis.toString).toLong)

              if (expiration.isAfterNow)
              //successful authentication and valid token
                Action(b)(request => f(admin_id)(request))


              else
                Action(b)(request => Results.Unauthorized.withHeaders(Http.HeaderNames.WWW_AUTHENTICATE -> "expired token."))

            }


            else Action(b)(request => Results.Unauthorized.withHeaders(Http.HeaderNames.WWW_AUTHENTICATE -> "invalid token: invalid signature."))
          }

          case _ => Action(b)(request => Results.Unauthorized.withHeaders(Http.HeaderNames.WWW_AUTHENTICATE -> "invalid token: parsing error."))

        }

      }

    }

  }


  /**
   * This function check if a user has a specific permission and role
   * Without body parser
   * Overloaded method to use the default body parser
   **/
  def IsAuthorized(requiredPermission: AccessPermission, role: AdminRole = AdminRole.All)(f: => String => Request[AnyContent] => Result): EssentialAction = {

    IsAuthorized[AnyContent](parse.anyContent)(requiredPermission, role)(f)

  }

  /**
   *
   * With body parser
   **/
  def IsAuthorized[A](b: BodyParser[A])(requiredPermission: AccessPermission, role: AdminRole)(f: => String => Request[A] => Result) = IsAuthenticated[A](b) {
    admin_id => request => {

      role match {
        case AdminRole.Sudo => {
          Option(administratorRepository.findOneByPermissionIsAdmin(admin_id, requiredPermission, true)) match {
            case Some(customerAdmin) => f(admin_id)(request)
            case None => play.api.mvc.Results.Unauthorized
          }
        }
        case AdminRole.Simple => {
          Option(administratorRepository.findOneByPermissionIsAdmin(admin_id, requiredPermission, false)) match {
            case Some(customerAdmin) => f(admin_id)(request)
            case None => play.api.mvc.Results.Unauthorized
          }
        }
        case AdminRole.All => {
          Option(administratorRepository.findOneByPermission(admin_id, requiredPermission)) match {
            case Some(customerAdmin) => f(admin_id)(request)
            case None => play.api.mvc.Results.Unauthorized
          }
        }

      }

    }

  }


}
