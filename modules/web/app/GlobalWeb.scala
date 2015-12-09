import akka.actor.Props
import controllers.web.actors.Register
import controllers.web.actors.scheduling.TemporaryPicturesCleaner
import exception.FailResult
import exception.FailResultWrites._
import org.springframework.context._
import org.springframework.context.support._
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.mvc.{RequestHeader, WithFilters}
import play.api.{Application, GlobalSettings}
import play.filters.headers.SecurityHeadersFilter
import security.filters.CORSFilter

import scala.concurrent.Future


object GlobalWeb extends WithFilters(CORSFilter, SecurityHeadersFilter()) with GlobalSettings {

  private var ctx: ApplicationContext = _

  override def onStart(app: Application) {

    ctx = new ClassPathXmlApplicationContext("spring-context-data.xml")

    //Create a register for client connection actor
    val clientsRegister = Akka.system.actorOf(Props[Register], name = "ClientsRegister")
    val temporaryPicturesCleaner = Akka.system.actorOf(Props[TemporaryPicturesCleaner], name = "TemporaryPicturesCleaner")

  }

  override def getControllerInstance[A](clazz: Class[A]): A = {
    ctx.getBean(clazz).asInstanceOf[A]

  }

  // 500 - internal server error
  override def onError(request: RequestHeader, throwable: Throwable) = {
    Future.successful(InternalServerError(
      Json.toJson(FailResult("please contact the backend developers team for that: " + throwable.getMessage))
    ))
  }


  // 404 - page not found error
  override def onHandlerNotFound(request: RequestHeader) = {
    Future.successful(NotFound(
      Json.toJson(FailResult("action not found: " + request.method + " " + request.path))
    ))
  }

  //The onBadRequest operation will be called if a route was found, but it was not possible to bind the request parameters
  override def onBadRequest(request: RequestHeader, error: String) = {
    Future.successful(BadRequest(
      Json.toJson(FailResult(error))
    ))
  }

}