package controllers

import dto.PersonDTO
import dto.PersonDTOWrites._
import entity.person.{Man, Woman}
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype
import play.api.Logger
import play.api.data.Forms._
import play.api.data._
import play.api.libs.json.Json
import play.api.mvc.{Action, _}
import repositories._
import repositories.person.{ManRepository, PersonRepository, WomanRepository}
import repositories.queryDSL.ManQueryDsl
import security.Secured
import service._

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

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

  def configRead() = Action {
    implicit request =>

      play.Play.application.configuration.getDoubleList("stations").foreach { station =>
        logger.debug(s"STATION: $station")
      }
      Ok
  }


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


}