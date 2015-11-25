package com.core.dom.person

import java.math.BigInteger
import java.security.{MessageDigest, NoSuchAlgorithmException}
import javax.persistence._

import com.core.dom.MainEntity
import play.api.Logger

@Entity
class User extends MainEntity {

  var salt: String = _

  @Column(nullable = false, unique = true)
  var email: String = _

  //human readable identifier
  @Column(nullable = false, unique = true)
  var username: String = _


  /**
   * the hash code of the password
   */
  private var passwordHashCode: String = _


  /**
   * Combines password and salt to a SHA-256-crypted password
   *
   * @param password
   * @return
   */
  def crypt(password: String): String = {
    var combinedPassword: String = salt + password

    var m: MessageDigest = null
    try {
      m = MessageDigest.getInstance("SHA-256");
      m.update(combinedPassword.getBytes("UTF-8")); // Change this to "UTF-16" if needed
      return new BigInteger(1, m.digest()).toString(16);

    } catch {
      case e: NoSuchAlgorithmException => {
        Logger.error(e.toString)
        return null
      }

    }

  }


  def comparePassword(password: String): Boolean = passwordHashCode.equals(crypt(password))

  def setPasswordHashCode(password: String) = passwordHashCode = crypt(password)

}
