package controllers.akka

import java.util.{List => JList}

import org.springframework.stereotype
import play.api.Logger
import play.api.Play.current
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, Controller, WebSocket}


//http://www.toptal.com/scala/concurrency-and-fault-tolerance-made-easy-an-intro-to-akka
//http://www.infoq.com/fr/articles/trio-akka-spring-scala
//https://github.com/typesafehub/activator-akka-java-spring/tree/master/src/main/java/sample
//http://letitcrash.com/post/55958814293/akka-dependency-injection@stereotype.Controller
/**
 * La solution choisi pour resoudre le probleme du mise à jour des GUI clients suite à un server event est la creation
 * d'un registre controllant tous les clients, si un event a été produit alors il sera envoyé vers le registre qui decidera
 * l'acheminement vers le client adequat.Finallement le clients diffusera l'update à toutes ses connections.
 * l'acheminement vers le client adequat.Finallement le clients diffusera l'update à toutes ses connections.
 */
@stereotype.Controller
class ConnectionController extends Controller {

  val logger: Logger = Logger(this.getClass())

  def socket(user_id: String) = WebSocket.acceptWithActor[String, JsValue] {
    request => out =>
      Connection.props(user_id, out)
  }

  def updateActor(client_id: String, msg: String) = Action {
    Register.update(client_id, Json.obj("message" -> msg))
    Ok
  }


}
