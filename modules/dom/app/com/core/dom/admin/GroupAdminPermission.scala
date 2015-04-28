package com.core.dom.admin

import javax.persistence._

import com.core.dom.MainEntity

@Entity
class GroupAdminPermission extends MainEntity {

  @ManyToOne
  @JoinColumn(nullable = false)
  var group: GroupAdmin = _

  @ManyToOne
  @JoinColumn(nullable = false)
  var permission: AccessPermission = _

  var isAdmin: Boolean = _

}
