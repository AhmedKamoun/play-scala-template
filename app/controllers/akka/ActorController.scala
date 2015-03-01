package controllers.akka

import java.util.{List => JList}

import akka.actor.ActorSystem
import controllers.akka.configuration.SpringExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype
import play.api.Logger
import play.api.Play.current
import play.api.libs.json.Json
import play.api.mvc.{Controller, WebSocket}
import repositories._
import repositories.person.{ManRepository, PersonRepository, WomanRepository}
import repositories.queryDSL.ManQueryDsl
import service._

//http://www.toptal.com/scala/concurrency-and-fault-tolerance-made-easy-an-intro-to-akka
//http://www.infoq.com/fr/articles/trio-akka-spring-scala
//https://github.com/typesafehub/activator-akka-java-spring/tree/master/src/main/java/sample
@stereotype.Controller
class ActorController extends Controller {

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
  @Autowired
  var actorSystem: ActorSystem = _ // get hold of the actor system


  val logger: Logger = Logger(this.getClass())


  def socket = WebSocket.acceptWithActor[String, String] {
    request => out =>

      //var simple_actor: SimpleActor = actorSystem.actorOf(SpringExtension.SpringExtProvider.get(actorSystem).props("SimpleActor")).asInstanceOf[SimpleActor]
      //val timeout :Timeout = new Timeout(Duration.create(30, "seconds"))

      val response = Json.obj("message" -> "hello world")
      SpringExtension.SpringExtProvider.get(actorSystem).props("EventActor", out, response)

  }


}