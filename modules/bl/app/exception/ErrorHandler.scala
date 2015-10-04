package exception

import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.mvc.Results._
import exception.FailResultWrites._
object ErrorHandler {

  def manageException(exception: Throwable): Result = {
    exception match {
      case SystemException(reason, ErrorType.NotFoundResource) => NotFound(Json.toJson(FailResult(reason)))
      case SystemException(reason, ErrorType.Conflict) => Conflict(Json.toJson(FailResult(reason)))
      case SystemException(reason, ErrorType.InternalServerError) => InternalServerError(Json.toJson(FailResult(reason)))
      case SystemException(reason, ErrorType.InternalServiceImplementationError) => NotImplemented(Json.toJson(FailResult(reason)))
      case SystemException(reason, ErrorType.NotImplementedService) => NotImplemented(Json.toJson(FailResult(reason)))
      case SystemException(reason, ErrorType.TooManyRequest) => TooManyRequest(Json.toJson(FailResult(reason)))
      case SystemException(reason, ErrorType.BadRequest) => BadRequest(Json.toJson(FailResult(reason)))
    }

  }
}
