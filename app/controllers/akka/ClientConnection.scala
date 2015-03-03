package controllers.akka

import akka.actor.{Actor, ActorRef, Props}

import scala.util.Random


object ClientConnection {
  def props(out: ActorRef) = Props(new ClientConnection(out))
}

/**
 * Manage in/out connection of webSocket
 * @param out
 */
class ClientConnection(out: ActorRef) extends Actor {
  def receive = {
    //IN: RECEIVED FROM FROM JS CLIENT
    case "CONNECT" => {
      ///First,we should register client connection
      val id = Random.alphanumeric.take(10).mkString
      ClientsRegister.getRegister ! AddClient(id, self)
    }
    //OUT: RECEIVED FROM FROM INNER SERVICES
    case u: GuiUpdate => out ! u.response

  }


}
