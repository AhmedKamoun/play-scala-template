import akka.actor.Props
import controllers.actors.Register
import org.springframework.context._
import org.springframework.context.support._
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.mvc.WithFilters
import play.api.{Application, GlobalSettings}
import play.filters.headers.SecurityHeadersFilter
import security.filters.CORSFilter


object Global extends WithFilters(CORSFilter, SecurityHeadersFilter()) with GlobalSettings {

  private var ctx: ApplicationContext = _

  override def onStart(app: Application) {

    ctx = new ClassPathXmlApplicationContext("spring-context-data.xml")

    //Create a register for client connection actor
    val clientsRegister = Akka.system.actorOf(Props[Register], name = "ClientsRegister")

  }

  override def getControllerInstance[A](clazz: Class[A]): A = {
    ctx.getBean(clazz).asInstanceOf[A]

  }

}