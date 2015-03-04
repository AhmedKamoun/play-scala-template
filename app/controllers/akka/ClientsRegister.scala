package controllers.akka

import akka.actor.{Actor, ActorRef, ActorSelection, Terminated}
import controllers.akka.ClientConnection.GuiUpdate
import controllers.akka.ClientsRegister.{AddClient, UpdateClient}
import play.api.Logger
import play.api.Play.current
import play.api.libs.concurrent.Akka

import scala.collection.mutable

//TODO we can ameliorate more the register's concept by dividing it into different ones controlled by a register manager
class ClientsRegister extends Actor {


  private var register: mutable.HashMap[String, ActorRef] = new mutable.HashMap[String, ActorRef]()

  def receive = {

    case AddClient(id, client) => {
      register += (id -> client)
      context watch client
      Logger.info("new client created with id: " + id + ", register size:" + register.size)
      //TODO we can control the limit number of created client socket actor. ex: connection_limit = 10
    }

    case UpdateClient(id, response) => {
      register.get(id) match {
        case Some(client) => client ! GuiUpdate(response)
        case None => Logger.error("no web socket found for user id: " + id)
      }

    }

    case Terminated(ref) => {
      //TODO test performance of deleting entry
      register = register filterNot { case (_, v) => v == ref}
      Logger.info("client terminated, register size:" + register.size) // if terminated so should be deleted from the register.

    }
  }
}

object ClientsRegister {

  var clients_register: ActorSelection = _

  /**
   *
   * A good practice is to declare what messages an Actor can receive in the companion object of the Actor, which makes
   * easier to know what it can receive
   */
  //Different received messages type
  case class AddClient(id: String, socket: ActorRef)
  case class UpdateClient(id: String, response: String)


  def getRegister = Option(clients_register) match {
    case Some(register) => {
      Logger.warn("Found register.")
      register
    }
    case None => {
      Logger.warn("No register was found so search for it...")
      clients_register = Akka.system.actorSelection("/user/ClientsRegister")
      clients_register
    }
  }

}



