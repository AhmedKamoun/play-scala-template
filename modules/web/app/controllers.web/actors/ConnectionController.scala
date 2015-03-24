package controllers.web.actors

import java.util.{List => JList}

import org.springframework.stereotype
import play.api.Logger
import play.api.Play.current
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, Controller, WebSocket}

import scala.concurrent.Future


//http://www.toptal.com/scala/concurrency-and-fault-tolerance-made-easy-an-intro-to-akka
//http://www.infoq.com/fr/articles/trio-akka-spring-scala
//https://github.com/typesafehub/activator-akka-java-spring/tree/master/src/main/java/sample
//http://letitcrash.com/post/55958814293/akka-dependency-injection@stereotype.Controller

//TODO
/**
 * La solution choisi pour resoudre le probleme du mise à jour des GUI clients suite à un server event est la creation
 * d'un registre controllant tous les clients, si un event a été produit alors il sera envoyé vers le registre qui decidera
 * l'acheminement vers le client adequat.Finallement le clients diffusera l'update à toutes ses connections.
 *
 * Note.1 :
 * la conception peut étre optimisée en consacrant un registre pour chaque pays et en affectant un registre-manager responsable
 * à l'acheminement vers le regitre du pays correspondant. Pour cela le COUNTRY_CODE doit etre envoyé au serveur dés l'initialisation
 * du websocket et il sera enregistré dans les cookies (comme le USER_ID). Un user sans COUNTRY_CODE sera enregistré dans
 * le registre UNDEFINED_COUNTRY.
 * http://docs.shopify.com/manual/configuration/store-customization/page-specific/store-wide/get-a-visitors-location.
 *
 * Note.2 :
 * On peut definir plusieurs registre managers afin de répondre rapidement au requetes d'update.
 *
 * Note.3 :
 * On peut limiter le nombre des connexions websocket pour chaque user.
 * .
 */
@stereotype.Controller
class ConnectionController extends Controller {

  val logger: Logger = Logger(this.getClass())

  def socket() = WebSocket.tryAcceptWithActor[JsValue, JsValue] {
    request =>
      Future.successful(request.session.get("APPLICATION.USER_ID") match {
        case None => Left(Forbidden)
        case Some(user_id) => Right(Connection.props(user_id))
      })
  }

  def updateActor(client_id: String, msg: String) = Action {

    Register.update(client_id, Json.obj("message" -> msg))

    Ok
  }


}
