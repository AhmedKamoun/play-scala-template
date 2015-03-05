import akka.actor.Props
import controllers.akka.Register
import org.springframework.context._
import org.springframework.context.support._
import play._
import play.api.Play.current
import play.api.libs.concurrent.Akka

class Global extends GlobalSettings {

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