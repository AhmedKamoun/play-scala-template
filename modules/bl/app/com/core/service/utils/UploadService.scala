package com.core.service.utils

import java.io.File
import java.util
import java.util.{List => JList}

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import org.joda.time.DateTime
import org.springframework.stereotype.Service
import play.api.Logger
import play.api.libs.json.{Json, Writes}

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

object CloudinaryService {

  var cloudinary: Cloudinary = _

  def getInstance(): Cloudinary = {
    Option(cloudinary) match {
      case Some(instance) => instance
      case None => {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
          "cloud_name", "ahmed-kamoun",
          "api_key", "878623561675743",
          "api_secret", "lGIZPZ8oI28ONz264oSuTWcTil8"))
        cloudinary
      }
    }
  }
}

@Service
class UploadService {
  val logger: Logger = Logger(this.getClass())


  def uploadPicture(picture: File) = {
    val folder = "ahmed/kamoun/repo"
    CloudinaryService.getInstance().uploader().upload(picture, ObjectUtils.asMap("folder", folder));

  }

  def generateSignature(folder: Option[String]): CloudinarySign = {
    val parameters = new util.HashMap[String, AnyRef]()

    val timestamp = DateTime.now().getMillis / 1000
    parameters.put("timestamp", timestamp.toString)

    folder match {
      case Some(value) => parameters.put("folder", value)
      case None => {}
    }

    val signature = CloudinaryService.getInstance().apiSignRequest(parameters, "lGIZPZ8oI28ONz264oSuTWcTil8")

    CloudinarySign(timestamp.toString, folder, signature)
  }


  /**
   * @author Kamoun Ahmed
   * @return create temporary file
   */
  def createFile(): File = {
    val directory = new File("/tmp/test/")
    directory.mkdirs()
    new File(directory, java.util.UUID.randomUUID.toString)

  }

}