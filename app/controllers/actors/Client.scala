package controllers.actors

import akka.actor._
import controllers.actors.Client.{BroadcastUpdate, addConnection}
import play.api.Logger
import play.api.libs.json.JsValue

import scala.collection.mutable.ListBuffer


object Client {

  /**
   * A good practice is to declare what messages an Actor can receive in the companion object of the Actor, which makes
   * easier to know what it can receive
   */

  case class BroadcastUpdate(msg: JsValue)

  case class addConnection(cnx: ActorRef)

  def props = Props(new Client())

}

class Client extends Actor {
  private var connections: ListBuffer[ActorRef] = ListBuffer.empty

  def receive = {
    case addConnection(cnx) => {
      connections += cnx
      context watch cnx
      Logger.debug("Total client cnx: " + connections.size)
      //TODO we can control the limit number of created client socket actor. ex: connection_limit = 10
    }

    case BroadcastUpdate(response) => {
      Logger.debug("Broadcasting update to " + connections.size + " client's connections")
      for (cnx <- connections) {
        cnx ! response
      }

    }
    case Terminated(ref) => {
      connections -= ref
      Logger.warn("Connection terminated, total client cnx: " + connections.size) // if terminated so should be deleted from the register.

      if (connections.isEmpty)
        self ! PoisonPill
    }


  }

}
