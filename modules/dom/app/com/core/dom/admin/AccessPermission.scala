package com.core.dom.admin

import javax.persistence._

import com.core.dom.MainEntity

@Entity
class AccessPermission extends MainEntity {

  @Column(unique = true)
  var identifier: String = _ // THIS VALUE MUST NOT BE CHANGED ELSE YOU SHOULD UPDATE ALSO THE CONSTANTS VALUES IN GMSConstants

  var description: String = _


}
