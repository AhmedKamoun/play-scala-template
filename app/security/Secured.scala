package security

import play.api.mvc.BodyParsers.parse
import play.api.mvc._

/**
 * Provide security features
 */

trait Secured {

  /**
   * Retrieve the connected user login.
   */
  private def login(request: RequestHeader) = request.session.get("APPLICATION.USER_ID")

  /**
   * Redirect to login if the user in not authorized.
   */
  private def onUnauthorized(request: RequestHeader) = Results.Redirect(controllers.login.routes.Login.login())

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
    Security.Authenticated(login, onUnauthorized) {
      userAccount =>
        Action(b)(request => f(userAccount)(request))
    }


}

