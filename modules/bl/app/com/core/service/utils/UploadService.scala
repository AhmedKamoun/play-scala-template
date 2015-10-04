package com.core.service.utils

import java.io.File
import java.util.{List => JList}

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import org.springframework.stereotype.Service
import play.api.Logger


object Cloudinary {

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
    Cloudinary.getInstance().uploader().upload(picture, ObjectUtils.asMap("folder", folder));

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