package entity.job

import javax.persistence._

import scala.beans.BeanProperty

@Entity
@PrimaryKeyJoinColumn(name = "id")
class Doctor extends Job {

  @BeanProperty
  var speciality: String = _


}
