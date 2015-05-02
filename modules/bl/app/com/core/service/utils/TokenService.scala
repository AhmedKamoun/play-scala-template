package com.core.service.utils

import java.util.{List => JList}

import authentikat.jwt.{JsonWebToken, JwtClaimsSet, JwtHeader}
import org.joda.time.DateTime
import org.springframework.stereotype.Service
import play.api.Logger
import play.api.Play.current

@Service
class TokenService {
  val logger: Logger = Logger(this.getClass())

  /**
   *
   * @param private_claims
   * @return generate a token with application.secret key
   */
  def generateToken(private_claims: Map[String, String]): String = {
    val header = JwtHeader("HS256")
    var public_claims: Map[String, String] = Map()
    public_claims += ("iat" -> DateTime.now().getMillis.toString) //The timestamp when the JWT was created
    public_claims += ("exp" -> DateTime.now().plusDays(2).getMillis.toString) //A timestamp defining an expiration time (end time) for the token
    val claims = public_claims ++ private_claims
    val claimsSet = JwtClaimsSet(claims)

    val secret_key = current.configuration.getString("application.secret").getOrElse("SecretKey")

    JsonWebToken(header, claimsSet, secret_key)
  }

}