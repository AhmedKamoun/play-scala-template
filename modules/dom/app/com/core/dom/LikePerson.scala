package com.core.dom

import javax.persistence._

import scala.beans.BeanProperty

@Entity
class LikePerson extends MainEntity {

  @BeanProperty
  var description: String = _

}
