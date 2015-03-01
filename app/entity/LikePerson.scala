package entity

import javax.persistence._
import scala.beans.BeanProperty

@Entity
class LikePerson extends SuperEntity {

  @BeanProperty
  var description: String = _

}
