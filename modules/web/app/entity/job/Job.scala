package entity.job

import javax.persistence._

import entity.SuperEntity

import scala.beans.BeanProperty

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
class Job extends SuperEntity {

  @BeanProperty
  var private_job: Boolean = _

}
