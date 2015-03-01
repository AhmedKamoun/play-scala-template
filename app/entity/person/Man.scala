package entity.person

import javax.persistence._

@Entity
@DiscriminatorValue(value = "man")
class Man extends Person {

  var age: Int = _


}
