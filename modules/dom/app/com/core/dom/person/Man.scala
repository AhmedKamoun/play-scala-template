package com.core.dom.person

import javax.persistence._

import com.core.enumeration.Visibility
import com.core.enumeration.Visibility.Visibility


@Entity
@DiscriminatorValue(value = "man")
class Man extends Person {
  var age: Int = _

}
