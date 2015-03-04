package service

import play.api.Logger

import scala.util.Random


object tools {

  def getRandomString = Random.alphanumeric.take(10).mkString

  // THIS FUNCTION IS FOR CALCULATING CODE PERFORMANCE BY ELAPSED TIME
  def time[R](block: => R): R = {
    val t0 = System.currentTimeMillis()
    val result = block // call-by-name
    val t1 = System.currentTimeMillis()
    Logger.debug("Elapsed time: " + (t1 - t0) + " MilliSec")
    result
  }
}
