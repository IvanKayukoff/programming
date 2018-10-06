package xyz.sky731.programming.lab7

class Coordinates {

  companion object {
    val quality = 32

    fun caclCircleCoords(x: Double, y: Double, r: Double): Coordinates {

      val result = Coordinates()
      for (i in 0..quality-1) {
        result.x[i] = r * Math.cos(2 * Math.PI / quality * i) + x
        result.y[i] = r * Math.sin(2 * Math.PI / quality * i) + y
      }
      return result
    }
  }

  val x = DoubleArray(quality)
  val y = DoubleArray(quality)

}

