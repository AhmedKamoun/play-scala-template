package controllers.akka

import akka.actor.{Actor, ActorRef}
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import play.api.libs.json.{JsObject, Json}

import scala.beans.BeanProperty

@Component("EventActor")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class EventActor extends Actor {
  @BeanProperty
  var out: ActorRef = _
  @BeanProperty
  var response: JsObject = _

  def receive = {
    case _ => out ! Json.prettyPrint(response)
  }

}
