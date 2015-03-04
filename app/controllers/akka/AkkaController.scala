package controllers.akka

import java.util.{List => JList}

import controllers.akka.ClientsRegister.UpdateClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype
import play.api.Logger
import play.api.Play.current
import play.api.mvc.{Action, Controller, WebSocket}
import repositories._
import repositories.person.{ManRepository, PersonRepository, WomanRepository}
import repositories.queryDSL.ManQueryDsl
import service._

import scala.util.Random


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
class AkkaController extends Controller {

  @Autowired
  var womanRepository: WomanRepository = _
  @Autowired
  var manRepository: ManRepository = _
  @Autowired
  var personRepository: PersonRepository = _
  @Autowired
  var likeRepository: LikePersonRepository = _
  @Autowired
  var manQueryDsl: ManQueryDsl = _
  @Autowired
  var manService: ManService = _


  val logger: Logger = Logger(this.getClass())

  /**
   *
   * @param key
   * @return test read performance for map with long String key
   */
  def searchFromMap(key: String) = Action {
    ExecutionCalculator.time({

      ExecutionCalculator.mapInitialize()
      ExecutionCalculator.testMap.get(key) match {
        case Some(value) => Ok("value: " + value + " found for key: " + key)
        case None => NotFound(" key: " + key + " was not found")
      }


    })

  }

  def socket = WebSocket.acceptWithActor[String, String] {
    request => out =>
      //TODO id is the identifier of the connected user
      val id = Random.alphanumeric.take(10).mkString
      ClientConnection.props(id, out)
  }


  def updateActor(id: String, msg: String) = Action {

    ClientsRegister.getRegister ! UpdateClient(id, msg)

    Ok

  }


}
