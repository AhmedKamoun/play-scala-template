package com.core.dom

import javax.persistence.{Column, GeneratedValue, Id, MappedSuperclass}

import com.core.enumeration.Visibility
import com.core.enumeration.Visibility._
import org.hibernate.annotations.GenericGenerator


@MappedSuperclass
abstract class MainEntity extends Serializable {
  /*
	 * the technical database and object key.
	 */

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  @Column(name = "id", length = 40)
  var id: String = _

  private var visibility: Int = Visibility.getId(Visibility.Visible)

  def getVisibility(): Visibility = Visibility.getValue(visibility)

  def setVisibility(vis: Visibility) = visibility = Visibility.getId(vis)
}
