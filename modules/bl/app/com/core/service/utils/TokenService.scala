package com.core.service.utils

import java.util.{List => JList}

import authentikat.jwt.{JsonWebToken, JwtClaimsSet, JwtHeader}
import org.joda.time.DateTime
import org.springframework.stereotype.Service
import play.api.Logger
import play.api.Play.current
import play.api.mvc.{AnyContent, Request}
import play.mvc.Http

/**
 * https://github.com/jasongoodwin/authentikat-jwt
 * https://auth0.com/blog/2014/01/27/ten-things-you-should-know-about-tokens-and-cookies/
 * https://auth0.com/blog/2014/01/07/angularjs-authentication-with-cookies-vs-token/
 * http://self-issued.info/docs/draft-ietf-oauth-v2-bearer.html#anchor6
 */
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

    //to keep it simple for this example we take secret key from application.conf.
    //TODO  encrypt the token by user hashed password to let him controls (disable) old generated tokens if he changes his password.
    val secret_key = current.configuration.getString("application.secret").getOrElse("BestSecretKey")

    JsonWebToken(header, claimsSet, secret_key)
  }

  def getTokenClaim(key: String)(implicit request: Request[AnyContent]): Option[String] = {

    request.headers.get(Http.HeaderNames.AUTHORIZATION) match {
      case Some(token) => {

        token match {
          case JsonWebToken(header, claimsSet, signature) => {

            val claims = claimsSet.asSimpleMap.toOption.getOrElse(Map.empty[String, String])
            //to keep it simple for this example we take secret key from application.conf.
            //TODO In prod mode we make more secure solution
            val secret_key = current.configuration.getString("application.secret").getOrElse("BestSecretKey")

            //Check if the jwt is valid
            if (JsonWebToken.validate(token, secret_key)) {

              val expiration = new DateTime(claims.get("exp").getOrElse(DateTime.now.getMillis.toString).toLong)

              if (expiration.isAfterNow)
              //successful authentication and valid token
                claims.get(key)

              else None
            }

            else None
          }

          case _ => None

        }
      }
      case None => None

    }
  }

}