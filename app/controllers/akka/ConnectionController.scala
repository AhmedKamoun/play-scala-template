package controllers.akka

import java.util.{List => JList}

import controllers.akka.ClientsRegister.UpdateClient
import org.springframework.stereotype
import play.api.Logger
import play.api.Play.current
import play.api.mvc.{Action, Controller, WebSocket}


//http://www.toptal.com/scala/concurrency-and-fault-tolerance-made-easy-an-intro-to-akka
//http://www.infoq.com/fr/articles/trio-akka-spring-scala
//https://github.com/typesafehub/activator-akka-java-spring/tree/master/src/main/java/sample
//http://letitcrash.com/post/55958814293/akka-dependency-injection@stereotype.Controller
/**
 * La solution choisi pour resoudre le probleme du mise à jour des GUI clients suite à un server event est la creation
 * d'un registre HashMap (Id_user, Topic_user), si un event a été produit alors il sera enpilé dans le topic adequat ensuite
 * ce topic va diffuser l'update à toutes les web sockets inscrites dans ce dernier.
 */
@stereotype.Controller
class ConnectionController extends Controller {

  val logger: Logger = Logger(this.getClass())

  def socket(user_id: String) = WebSocket.acceptWithActor[String, String] {
    request => out =>

      ClientConnection.props(user_id, out)
  }

  def updateActor(id: String, msg: String) = Action {

    ClientsRegister.getRegister ! UpdateClient(id, msg)
    Ok
  }


}
