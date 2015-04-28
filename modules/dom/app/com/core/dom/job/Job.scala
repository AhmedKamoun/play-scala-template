package com.core.dom.job

import javax.persistence._

import com.core.dom.MainEntity

import scala.beans.BeanProperty

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
class Job extends MainEntity {

  @BeanProperty
  var private_job: Boolean = _

}
