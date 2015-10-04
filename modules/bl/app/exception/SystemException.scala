package exception

case class SystemException(reason: String, error: ErrorType.Value) extends Throwable(reason)