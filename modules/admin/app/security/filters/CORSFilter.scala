package security.filters

import controllers.Default
import play.api.Logger
import play.api.http.HeaderNames._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._

import scala.concurrent.Future

/*
FOR MORE INFORMATION:
https://developer.mozilla.org/en-US/docs/Web/HTTP/Access_control_CORS
http://www.html5rocks.com/en/tutorials/cors/
https://www.playframework.com/documentation/2.3.7/ScalaHttpFilters
https://gist.github.com/jeantil/7214962
http://windrush.io/tech/2013/12/17/cors-and-play.html
http://stackoverflow.com/questions/10392545/custom-configuration-files-play-framework-2-0/21205813#21205813
 */

object CORSFilter extends Filter {

  val logger: Logger = Logger(this.getClass())

  def isPreFlight(r: RequestHeader) = (
    r.method.toLowerCase.equals("options")
    // &&  r.headers.get("Access-Control-Request-Method").nonEmpty
    )

  /**
   * //TODO could be improved by getting dynamically the list of allowed origins from database instead of static config file
   * @param request
   * @return Check if request origin is allowed
   */
  def getAllowedOrigin(request: RequestHeader): Option[String] = {

    request.headers.get(ORIGIN) match {
      case Some(origin) => {
        if (play.Play.application.configuration.getStringList("cors.allowed.domains").contains(origin))
          Some(origin)
        else None
      }
      case None => None
    }


  }

  def apply(nextFilter: (RequestHeader) => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {

    if (isPreFlight(requestHeader)) {
      //      logger.warn("[cors] request is preflight")

      Future.successful(Default.Ok.withHeaders(
        "Access-Control-Allow-Origin" -> getAllowedOrigin(requestHeader).getOrElse(""),
        "Access-Control-Allow-Methods" -> requestHeader.headers.get("Access-Control-Request-Method").getOrElse("*"),
        "Access-Control-Allow-Headers" -> requestHeader.headers.get("Access-Control-Request-Headers").getOrElse(""),
        "Access-Control-Allow-Credentials" -> "true")
      )

    }


    else {

      //  logger.warn("[cors] request is normal")
      nextFilter(requestHeader).map { result =>

        result.withHeaders(
          "Access-Control-Allow-Origin" -> getAllowedOrigin(requestHeader).getOrElse(""),
          "Access-Control-Allow-Methods" -> requestHeader.headers.get("Access-Control-Request-Method").getOrElse("*"),
          "Access-Control-Allow-Headers" -> requestHeader.headers.get("Access-Control-Request-Headers").getOrElse(""),
          "Access-Control-Allow-Credentials" -> "true")

      }


    }
  }
}