package com.core.dom.person

import javax.persistence.{DiscriminatorValue, Entity}

@Entity
@DiscriminatorValue(value = "woman")
class Woman extends Person {

}
