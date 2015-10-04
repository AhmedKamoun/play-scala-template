package controllers.web

import com.core.dal.LikePersonRepository
import com.core.dal.person.{ManRepository, PersonRepository, WomanRepository}
import com.core.dal.queryDSL.ManQueryDsl
import com.core.dom.person.{Man, Woman}
import com.core.dto.PersonDTO
import com.core.dto.PersonDTOWrites._
import com.core.service.ManService
import exception.{ErrorHandler, ErrorType, SystemException}
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype
import play.api.Logger
import play.api.data.Forms._
import play.api.data._
import play.api.libs.json.Json
import play.api.mvc.{Action, _}
import security.Secured

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Success, Try}

@stereotype.Controller
class Application extends Controller with Secured {

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

  val logger: Logger = Logger(this.getClass())

  val addWomanForm = Form(
    tuple(
      "name" -> text,
      "speciality" -> text

    )
  )


  def submit() = Action {
    implicit request =>

      val addManForm = Form(
        tuple(
          "name" -> nonEmptyText,
          "age" -> number(min = 10, max = 150))
      )
      addManForm.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(formWithErrors.errorsAsJson)
        }
        ,
        form => {

          var artist: Man = new Man()
          artist.name = form._1
          artist.age = form._2
          personRepository.save(artist)
          Ok("new user was created")
        }
      )
  }


  def addWoman() = Action {
    Ok(views.html.addDoctor(addWomanForm))
  }

  def submitWoman() = Action {
    implicit request =>
      addWomanForm.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(views.html.addDoctor(formWithErrors))
        }
        ,
        SucceededForm => {

          var doctor = new Woman()
          doctor.setName(SucceededForm._1)

          womanRepository.save(doctor)

          Ok(views.html.addDoctor(addWomanForm))
        }
      )
  }


  /**
   *
   * @return toutes les personne "men" qui sont des artistes, le domaine auquel elles sont lié est une instance de "Art"
   */
  def allArtistMen() = Action {

    val total = manRepository.findArtistMen()
    Ok(Json.prettyPrint(Json.obj("total_artist_men" -> total)))
  }


  def allPersons() = IsAuthenticated {
    request => userID =>
      var response: ListBuffer[PersonDTO] = new ListBuffer[PersonDTO]

      for (dto <- personRepository.findPersonsInPrivateSector().toList) {
        if (dto.person.isInstanceOf[Man])
          dto.sex = "male"
        else
          dto.sex = "female"
        response += dto
      }
      Ok(
        Json.prettyPrint(Json.obj("all_persons" -> response.toList))
      )
  }

  /**
   *
   * @return  une réponse complexe et composée. par example List[(String,Personne)]
   */
  def compositeResponse(temporal_reference: Long) = Action {

    var startTime: DateTime = new DateTime(temporal_reference)

    Ok(Json.prettyPrint(Json.obj("elepsed" -> (((new DateTime().getMillis) - startTime.getMillis) / 1000))))

    // val list = manQueryDsl.personList()
    //Ok(Json.prettyPrint(list))
  }

  def exception(number: Int) = Action {
    try {
      throwException(number) match {
        case Success(response) => Ok("success") //Success push
        case Failure(exception) => ErrorHandler.manageException(exception)
      }
    }
    catch {
      case exception:  SystemException => ErrorHandler.manageException(exception)

    }


  }

  @throws(classOf[SystemException])
  def throwException(number: Int): Try[Unit] = {
    if (number == 0)
      Success(())
    else if (number == 1)
      Failure(SystemException("NotFoundResource", ErrorType.NotFoundResource))
    else if (number == 2)
      throw SystemException("Throw exception: InternalServerError", ErrorType.InternalServerError)
    else
      throw new NullPointerException()
  }


}