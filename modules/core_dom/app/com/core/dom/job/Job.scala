package com.core.dom.job

import javax.persistence._

import com.core.dom.SuperEntity

import scala.beans.BeanProperty

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
class Job extends SuperEntity {

  @BeanProperty
  var private_job: Boolean = _

}
