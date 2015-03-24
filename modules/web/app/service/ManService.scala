package service

import java.util.{List => JList}
import com.core.dom.LikePerson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import repositories._
import repositories.person.ManRepository
import scala.collection.JavaConversions._

@Service
class ManService {

  @Autowired
  var manRepository: ManRepository = _
  @Autowired
  var likePersonRepository: LikePersonRepository = _

  @Transactional
  def addLikes() = {

    val person = manRepository.findOne("ff80818149195bee0149195c593b0000")
    var like1 = new LikePerson()
    like1.setDescription("like_1")
    person.likes.add(like1)


    var like2 = new LikePerson()
    like2.setDescription("like_2")
    person.likes.add(like2)

    manRepository.save(person)


  }

  @Transactional
  def removeLikes() = {
    val person = manRepository.findOne("ff80818149195bee0149195c593b0000")
    person.likes.clear()
    manRepository.save(person)
  }

  @Transactional
  def getLikes(): Set[LikePerson] = {
    val person = manRepository.findOne("ff80818149195bee0149195c593b0000")
    person.getLikes().toSet
  }


}