package com.core.dom.admin

import javax.persistence._

import com.core.dom.MainEntity

@Entity
class GroupAdmin extends MainEntity {

  @Column(unique = true)
  var name: String = _

  var description: String = _


}
