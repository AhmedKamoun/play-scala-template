package com.core.dom.admin

import javax.persistence._

import com.core.dom.MainEntity

@Entity
class Administrator extends MainEntity {

  var name: String = _

  @Column(nullable = false, unique = true)
  var email: String = _

  @ManyToOne
  @JoinColumn(nullable = false)
  var group: GroupAdmin = _

}
