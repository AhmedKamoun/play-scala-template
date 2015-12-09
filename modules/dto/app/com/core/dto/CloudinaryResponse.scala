package com.core.dto


import com.core.dto.ResourceBuilder.{resourceReads, resourceWrites}
import play.api.libs.json.{JsPath, Json, Reads, Writes}

case class CloudinaryResponse(var resources: List[Resource])

object CloudinaryResponseBuilder {

  implicit val ClResponseWrites: Writes[CloudinaryResponse] = Writes {
    (dto: CloudinaryResponse) => {
      Json.obj("resources" -> Json.toJson(dto.resources))
    }
  }

  implicit val clResponseReads: Reads[CloudinaryResponse] =
    (JsPath \ "resources").read[List[Resource]].map(CloudinaryResponse)

}
