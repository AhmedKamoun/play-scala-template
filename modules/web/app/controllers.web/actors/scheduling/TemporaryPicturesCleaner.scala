package controllers.web.actors.scheduling

import java.util

import akka.actor.Actor
import com.cloudinary.utils.ObjectUtils
import com.core.dto.CloudinaryResponse
import com.core.dto.CloudinaryResponseBuilder._
import com.core.service.utils.CloudinaryConfig
import org.joda.time.{DateTime, DateTimeZone}
import play.api.Logger
import play.api.Play.current
import play.api.http.{HeaderNames, MimeTypes}
import play.api.libs.json.{JsError, JsSuccess}
import play.api.libs.ws.{WS, WSAuthScheme}

import scala.collection.JavaConversions._
import scala.concurrent.duration._
import scala.language.postfixOps

case class DeleteTemporaryPicture()

class TemporaryPicturesCleaner extends Actor {

  import context.dispatcher

  val logger: Logger = Logger(this.getClass())

  val task =
    context.system.scheduler.schedule(10 minute, 3 hour, self, DeleteTemporaryPicture)

  override def postStop() = task.cancel()

  def receive = {
    case DeleteTemporaryPicture => {

      //1. find first 100 images that are tagged by temporary keyword.
      WS.url("https://api.cloudinary.com/v1_1/ahmed-kamoun/resources/image/tags/temporary")
        .withAuth(CloudinaryConfig.api_key, CloudinaryConfig.api_secret, WSAuthScheme.BASIC)
        .withQueryString("max_results" -> "100")
        .withQueryString("direction" -> "desc")
        .withHeaders(HeaderNames.ACCEPT -> MimeTypes.JSON)
        .get()
        .map { response =>
          response.json.validate[CloudinaryResponse] match {
            case JsSuccess(response, _) => {
              val resources = response.resources
              var pictures_to_delete = new util.ArrayList[String]()
              //2. filter all resources that are older than 1h .
              for (r <- resources) {
                if (DateTime.now(DateTimeZone.UTC).minusHours(1).isAfter(r.created_on)) {
                  pictures_to_delete += r.public_id
                }
              }
              //3. delete pictures by PUBLIC_ID.
              if (pictures_to_delete.nonEmpty)
                CloudinaryConfig.getInstance().api.deleteResources(pictures_to_delete, ObjectUtils.emptyMap())

            }
            case e: JsError => logger.error("error while parsing response from cloudinary APIs!")

          }

        }
    }


  }
}
