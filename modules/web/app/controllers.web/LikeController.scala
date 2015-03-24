package controllers.web

import java.util.{List => JList}

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype
import play.api.libs.json.Json
import play.api.mvc.{Action, _}
import repositories._
import repositories.person.{ManRepository, WomanRepository}
import repositories.queryDSL.ManQueryDsl
import service._

import scala.collection.JavaConversions._

@stereotype.Controller
class LikeController extends Controller {

  @Autowired
  var womanRepository: WomanRepository = _
  @Autowired
  var manRepository: ManRepository = _
  @Autowired
  var likeRepository: LikePersonRepository = _
  @Autowired
  var artistQueryDsl: ManQueryDsl = _
  @Autowired
  var manService: ManService = _


  def addLikesToPerson() = Action {

//    manService.addLikes()

    Ok(Json.prettyPrint(Json.obj("insert" -> "done!")))
  }


  def removeLikesFromPerson() = Action {

    manService.removeLikes()
    Ok(Json.prettyPrint(Json.obj("remove" -> "done!")))
  }

  def getLikesToPerson() = Action {

    val allLikes = manService.getLikes()
    var likes = Json.arr()
    for (like <- allLikes) {
      likes = likes :+ Json.obj("description" -> like.getDescription)
    }


    Ok(Json.prettyPrint(likes))
  }

  def getLikesBy(description: String) = Action {
    val artist = manRepository.findOne("ff80818149195bee0149195c593b0000")
    val allLikes = likeRepository.findByManByDescription(artist, description).toList
    var likes = Json.arr()

    for (like <- allLikes) {
      likes = likes :+ Json.obj("description" -> like.getDescription)
    }


    Ok(Json.prettyPrint(likes))
  }


}