package exception

import play.api.libs.json.{Json, Writes}


case class FailResult(reason: String)

object FailResultWrites {

  implicit val failResultWrites: Writes[FailResult] = Writes {
    (failure: FailResult) => {
      Json.obj(
        "error_message" -> failure.reason
      )
    }
  }
}