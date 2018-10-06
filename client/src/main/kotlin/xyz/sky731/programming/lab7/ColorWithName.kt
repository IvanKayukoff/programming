package xyz.sky731.programming.lab7

import java.awt.Color
import java.lang.IllegalArgumentException

class ColorWithName(val color: Color, val name: String) {
  override fun toString(): String {
    return name
  }

  constructor(colorWithName: ColorWithName) : this(Color(colorWithName.color.rgb), colorWithName.name)

  constructor(name: String) : this(when (name) {
    "Red" -> Color.RED
    "Green" -> Color.GREEN
    "Blue" -> Color.BLUE
    "Pink" -> Color.PINK
    "Orange" -> Color.ORANGE
    "Yellow" -> Color.YELLOW
    else -> throw IllegalArgumentException("Unsupported color name")
  }, name)
}
