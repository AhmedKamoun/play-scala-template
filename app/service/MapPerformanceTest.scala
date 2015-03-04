package service

import play.api.Logger

import scala.collection.mutable


object MapPerformanceTest {
  var testMap: mutable.HashMap[String, Long] = new mutable.HashMap[String, Long]()
  val mapSize = 4000000

  def mapInitialize() = {
    if (testMap.isEmpty) {
      Logger.warn("map with 4000000 fields creation starts:")
      tools.time({
        for (i <- 1 until mapSize)
          testMap += ("hello" + i.toString + "world" + i.toString -> i.toLong)
      })
      Logger.warn("map with 4000000 fields creation ends:")

    }
  }


}
