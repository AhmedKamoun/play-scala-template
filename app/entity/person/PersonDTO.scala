package entity.person

import play.api.libs.json.{Json, Writes}

case class PersonDTO(var person: Person, var sex: String)

object PersonDTOWrites {

  implicit val PersonWrites: Writes[PersonDTO] = Writes {
    (result: PersonDTO) => {
      Json.obj(
        "name" -> Json.toJson(result.person.name),
        "sex" -> Json.toJson(result.sex)
      )


    }
  }
}