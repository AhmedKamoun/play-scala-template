package com.core.dto

import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{JsPath, Json, Reads, Writes}

case class Resource(var public_id: String, var created_on: DateTime)

object ResourceBuilder {

  implicit val dateReads = Reads.jodaDateReads("yyyy-MM-dd'T'HH:mm:ssZ")
  implicit val resourceReads: Reads[Resource] = (
    (JsPath \ "public_id").read[String] and
    (JsPath \ "created_at").read[DateTime](dateReads)//relative to GMT
    )(Resource.apply _)


  implicit val resourceWrites: Writes[Resource] = Writes {
    (r: Resource) => {
      Json.obj(
        "public_id" -> Json.toJson(r.public_id),
        "created_at" -> Json.toJson(r.created_on)
      )
    }
  }
}
