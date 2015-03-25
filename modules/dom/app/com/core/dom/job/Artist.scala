package com.core.dom.job

import javax.persistence._

import scala.beans.BeanProperty

@Entity
@PrimaryKeyJoinColumn(name = "id")
class Artist extends Job {

  @BeanProperty
  var instrument: String = _

}
