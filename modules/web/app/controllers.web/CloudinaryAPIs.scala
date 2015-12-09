package controllers.web

import java.io.File

import com.core.dal.LikePersonRepository
import com.core.dal.person.{ManRepository, PersonRepository, WomanRepository}
import com.core.dal.queryDSL.ManQueryDsl
import com.core.dto.CloudinaryResponse
import com.core.dto.CloudinaryResponseBuilder._
import com.core.service.ManService
import com.core.service.utils.CloudinarySignWrites._
import com.core.service.utils.{CloudinaryConfig, CloudinaryService}
import exception.FailResultWrites._
import exception.{ErrorHandler, FailResult, SystemException}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype
import play.api.Logger
import play.api.Play.current
import play.api.data.Form
import play.api.data.Forms._
import play.api.http.{HeaderNames, MimeTypes}
import play.api.libs.json._
import play.api.libs.ws.{WS, WSAuthScheme}
import play.api.mvc.{Action, _}
import security.Secured

import scala.concurrent.ExecutionContext.Implicits.global

@stereotype.Controller
class CloudinaryAPIs extends Controller with Secured {

  @Autowired
  var womanRepository: WomanRepository = _
  @Autowired
  var manRepository: ManRepository = _
  @Autowired
  var personRepository: PersonRepository = _
  @Autowired
  var likeRepository: LikePersonRepository = _
  @Autowired
  var manQueryDsl: ManQueryDsl = _
  @Autowired
  var manService: ManService = _
  @Autowired
  var uploadService: CloudinaryService = _

  val logger: Logger = Logger(this.getClass())

  def uploadPicture() = Action(parse.multipartFormData) {
    implicit request =>
      val form = Form(
        single(
          "picture" -> ignored(Option.empty[File])
        )

      )
      request.body.file("picture").map { picture =>

        // handle the other form data
        form.bindFromRequest.fold(
          formWithErrors => {
            BadRequest(formWithErrors.errorsAsJson)
          },

          data => {

            try {

              // retrieve the image and put it where you want...
              var temporary_picture = uploadService.createFile()
              picture.ref.moveTo(temporary_picture)
              uploadService.upload_server_side(temporary_picture)
              //delete temporaries files
              temporary_picture.delete()
              Ok("upload done")
            }
            catch {
              case exception: SystemException => ErrorHandler.manageException(exception)

            }

          }
        )

      }.getOrElse(BadRequest(Json.toJson(FailResult("MISSING_UPLOADED_FILE"))))


  }

  def generateSignature(folder: Option[String]) = Action {
    implicit request =>
      val data = uploadService.generateSignature(folder)
      Ok(Json.toJson(data))

  }

  def delete(public_id: String) = Action {
    implicit request =>
      uploadService.delete(public_id)
      Ok("DELETING DONE")

  }


  def upload_picture_client_side(folder: Option[String]) = Action {
    val sign = uploadService.generateSignature(folder)
    Ok(views.html.uploadPic(sign))
  }

  def remove_tmp_tag(public_id: String) = Action {
    uploadService.remove_tag("temporary", Array(public_id))
    Ok
  }

  /**
   *
   * http://cloudinary.com/documentation/admin_api#list_resources_by_tag
   * @return Unused temporary pictures collector
   */
  def deleteTemporaryPictures() = Action.async {
    implicit request =>
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

              Ok(Json.toJson(uploadService.deleteUnusedTemporary(response.resources)))
            }
            case e: JsError => BadRequest(JsError.toFlatJson(e))
          }

        }
  }

  /**
   *
   * http://cloudinary.com/documentation/admin_api#list_resources_by_tag
   * @return Unused temporary pictures collector
   */
  def getTemporaryPictures() = Action.async {
    implicit request =>
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

              Ok(Json.toJson(response))
            }
            case e: JsError => BadRequest(JsError.toFlatJson(e))
          }

        }
  }

}