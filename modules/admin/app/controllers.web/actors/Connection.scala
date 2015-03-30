package controllers.web.actors

import akka.actor.{Actor, ActorRef, Props}
import controllers.web.actors.Register.SaveConnection
import play.api.libs.json.JsValue


object Connection {


  /**
   * Create Props for an actor of this type.It is a good idea to provide factory methods on the companion object of each Actor
   * which help keeping the creation of suitable Props as close to the actor definition as possible.
   * @return a Props for creating this actor, which can then be further configured
   *         (e.g. calling `.withDispatcher()` on it)
   */
  def props(user_id: String)(out: ActorRef) = Props(new Connection(user_id, out))
}

/**
 * This actor represents a client connection by webSocket
 *
 */
class Connection(user_id: String, out: ActorRef) extends Actor {

  override def preStart() = {
    ///First,we should register this newly client connection
    Register.getRegister ! SaveConnection(user_id, self)
  }

  def receive = {

    case update: JsValue => out ! update

  }


}
