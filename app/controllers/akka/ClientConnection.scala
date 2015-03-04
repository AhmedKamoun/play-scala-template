package controllers.akka

import akka.actor.{Actor, ActorRef, Props}
import controllers.akka.ClientConnection.GuiUpdate
import controllers.akka.ClientsRegister.AddClient


object ClientConnection {

  /**
   * A good practice is to declare what messages an Actor can receive in the companion object of the Actor, which makes
   * easier to know what it can receive
   */
  case class GuiUpdate(response: String)

  /**
   * Create Props for an actor of this type.It is a good idea to provide factory methods on the companion object of each Actor
   * which help keeping the creation of suitable Props as close to the actor definition as possible.
   * @return a Props for creating this actor, which can then be further configured
   *         (e.g. calling `.withDispatcher()` on it)
   */
  def props(user_id: String, out: ActorRef) = Props(new ClientConnection(user_id, out))
}

/**
 * This actor represents a client connection by webSocket
 *
 */
class ClientConnection(user_id: String, out: ActorRef) extends Actor {

  override def preStart() = {
    ///First,we should register this newly client connection
    ClientsRegister.getRegister ! AddClient(user_id, self)
  }

  def receive = {

    case u: GuiUpdate => out ! u.response

  }


}
