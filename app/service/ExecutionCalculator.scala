package service

import play.api.Logger

import scala.collection.mutable


object ExecutionCalculator {
  var testMap: mutable.HashMap[String, Long] = new mutable.HashMap[String, Long]()
  val mapSize = 4000000

  def mapInitialize() = {
    if (testMap.isEmpty) {
      Logger.warn("map with 4000000 fields creation starts:")
      ExecutionCalculator.time({
        for (i <- 1 until mapSize)
          testMap += ("hello" + i.toString + "world" + i.toString -> i.toLong)
      })
      Logger.warn("map with 4000000 fields creation ends:")

    }
  }

  // THIS FUNCTION IS FOR CALCULATING CODE PERFORMANCE BY ELAPSED TIME
  def time[R](block: => R): R = {
    val t0 = System.currentTimeMillis()
    val result = block // call-by-name
    val t1 = System.currentTimeMillis()
    Logger.debug("Elapsed time: " + (t1 - t0) + " MilliSec")
    result
  }

}
