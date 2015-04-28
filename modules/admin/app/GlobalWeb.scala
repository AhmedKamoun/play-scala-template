import org.springframework.context._
import org.springframework.context.support._
import play.api.mvc.WithFilters
import play.api.{Application, GlobalSettings}
import play.filters.headers.SecurityHeadersFilter
import security.filters.CORSFilter


object GlobalWeb extends WithFilters(CORSFilter, SecurityHeadersFilter()) with GlobalSettings {

  private var ctx: ApplicationContext = _

  override def onStart(app: Application) {

    ctx = new ClassPathXmlApplicationContext("spring-context-data.xml")

  }

  override def getControllerInstance[A](clazz: Class[A]): A = {
    ctx.getBean(clazz).asInstanceOf[A]

  }

}