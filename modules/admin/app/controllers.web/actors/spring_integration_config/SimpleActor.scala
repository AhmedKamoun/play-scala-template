package controllers.web.actors.spring_integration_config

import akka.actor.{Actor, ActorRef}
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import play.api.libs.json.{JsObject, Json}

import scala.beans.BeanProperty

/**
 * This is an example of an actor managed by spring, so we can use all services and repository into it.
 * if we want to get instance of it from play web socket so proceed like below:

  @ Autowired
  var actorSystem: ActorSystem = _ // get hold of the actor system

   1.to get ActorRef
      var simple_actor: SimpleActor = actorSystem.actorOf(SpringExtension.SpringExtProvider.get(actorSystem).props("SimpleActor", out, response)).asInstanceOf[SimpleActor]
OR
   2.to get Prop object
      val response = Json.obj("message" -> "hello world")
      SpringExtension.SpringExtProvider.get(actorSystem).props("SimpleActor", out, response)

 */
@Component("SimpleActor")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class SimpleActor extends Actor {
  @BeanProperty
  var out: ActorRef = _
  @BeanProperty
  var response: JsObject = _

  def receive = {
    case _ => out ! Json.prettyPrint(response)
  }

}
