package com.core.service.utils

import java.io.File
import java.util
import java.util.{List => JList}

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.core.dto.Resource
import org.joda.time.{DateTime, DateTimeZone}
import org.springframework.stereotype.Service
import play.api.Logger
import play.api.libs.json.{Json, Writes}

import scala.collection.JavaConversions._

case class CloudinarySign(timestamp: String, folder: Option[String], signature: String)

object CloudinarySignWrites {

  implicit val cloudinarySignWrites: Writes[CloudinarySign] = Writes {
    (data: CloudinarySign) => {
      var response = Json.obj(
        "signature" -> data.signature,
        "timestamp" -> data.timestamp
      )
      data.folder match {
        case Some(value) => response = response + ("folder" -> Json.toJson(value))
        case None => {}
      }

      response
    }
  }
}

object CloudinaryConfig {

  var cloudinary: Cloudinary = _
  val cloud_name = "ahmed-kamoun"
  val api_secret = "lGIZPZ8oI28ONz264oSuTWcTil8"
  val api_key = "878623561675743"

  def getInstance(): Cloudinary = {
    Option(cloudinary) match {
      case Some(instance) => instance
      case None => {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
          "cloud_name", cloud_name,
          "api_key", api_key,
          "api_secret", api_secret))
        cloudinary
      }
    }
  }
}

@Service
class CloudinaryService {
  val logger: Logger = Logger(this.getClass())

  /**
   * @author Kamoun Ahmed
   * @return create temporary file
   */
  def createFile(): File = {
    val directory = new File("/tmp/test/")
    directory.mkdirs()
    new File(directory, java.util.UUID.randomUUID.toString)

  }

  def upload_server_side(picture: File) = {
    val folder = "ahmed/kamoun/repo"
    CloudinaryConfig.getInstance().uploader().upload(picture, ObjectUtils.asMap("folder", folder));

  }

  def generateSignature(folder: Option[String]): CloudinarySign = {
    val parameters = new util.HashMap[String, AnyRef]()
    //expiration after 15 minutes!
    val timestamp = DateTime.now(DateTimeZone.UTC).minusMinutes(45).getMillis / 1000
    parameters.put("timestamp", timestamp.toString)
    parameters.put("tags", "temporary")

    folder match {
      case Some(value) => parameters.put("folder", value)
      case None => {}
    }

    val signature = CloudinaryConfig.getInstance().apiSignRequest(parameters, CloudinaryConfig.api_secret)

    CloudinarySign(timestamp.toString, folder, signature)
  }

  def delete(public_id: String) = CloudinaryConfig.getInstance().uploader().destroy(public_id, ObjectUtils.asMap("invalidate", java.lang.Boolean.TRUE))

  def remove_tag(tag: String, public_ids: Array[String]) = CloudinaryConfig.getInstance().uploader().removeTag(tag, public_ids, ObjectUtils.emptyMap())

  /**
   * use this from cron actor job every 3h.
   * @param resources
   * @return
   */
  def deleteUnusedTemporary(resources: List[Resource]): List[String] = {
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
    pictures_to_delete.toList
  }


}