package com.core.dom.person

import javax.persistence._

import com.core.dom.job.Job
import com.core.dom.{LikePerson, MainEntity}
import org.hibernate.annotations.Type
import org.joda.time.DateTime

import scala.beans.BeanProperty

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract class Person extends MainEntity {

  @BeanProperty
  var name: String = _

  @Type(`type` = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  @BeanProperty
  var createdOn: DateTime = _

  @ManyToOne
  @BeanProperty
  var job: Job = _

  @OneToMany(fetch = FetchType.LAZY, cascade = Array(CascadeType.ALL), orphanRemoval = true)
  @BeanProperty
  var likes: java.util.Set[LikePerson] = new java.util.HashSet[LikePerson]()



}
