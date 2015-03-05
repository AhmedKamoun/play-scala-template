package controllers.akka

import akka.actor.{Actor, ActorRef, ActorSelection}
import controllers.akka.Client.{BroadcastUpdate, addConnection}
import controllers.akka.Register.{SaveConnection, UpdateClient}
import play.api.Logger
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.json.JsValue


object Register {

  var clients_register: ActorSelection = _

  /**
   *
   * A good practice is to declare what messages an Actor can receive in the companion object of the Actor, which makes
   * easier to know what it can receive
   */
  //Different received messages type
  case class UpdateClient(id: String, response: JsValue)

  case class SaveConnection(user_id: String, cnx: ActorRef)

  def update(client_id: String, message: JsValue) = {
    getRegister ! UpdateClient(client_id, message)
  }

  def getRegister = Option(clients_register) match {
    case Some(register) => register
    case None => {
      clients_register = Akka.system.actorSelection("/user/ClientsRegister")
      clients_register
    }
  }

}


//TODO we can ameliorate more the register's concept by dividing it into different ones controlled by a register manager
class Register extends Actor {

  def receive = {

    //add connection to the corresponding client
    case SaveConnection(user_id, cnx) => {

      //search for client
      context.child(user_id) match {
        case Some(client) => {
          //tell client child to add this cnx to its list
          Logger.debug("Child with id: " + user_id + " was found")

          client ! addConnection(cnx)
        }
        case None => {
          // create new client which the name is user_id
          val new_client = context.actorOf(Client.props, name = user_id)
          Logger.debug("New client created with id: " + user_id)

          new_client ! addConnection(cnx)

        }
      }
      Logger.debug("Total saved clients:" + context.children.size)

    }

    case UpdateClient(user_id, response) => {
      context.child(user_id) match {
        case Some(client) => client ! BroadcastUpdate(response)
        case None => Logger.error("No client was found with id: " + user_id + ", update will be ignored.")
      }

    }


  }
}



