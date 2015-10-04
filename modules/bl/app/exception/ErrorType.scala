package exception

object ErrorType extends Enumeration {
  val
  NotFoundResource, //Undefined Achievement, AchievementGroup, ReputationLevel, LoyalCraftCustomer...
  NotImplementedService,
  InternalServerError,
  TooManyRequest,
  Conflict,
  InternalServiceImplementationError, // Problem from LoyalCraft administration exp: Level transition conditions not well defined
  BadRequest // INTEGRITY PROBLEM: REQUEST THAT DOES NOT MAKE SENS
  = Value
}
