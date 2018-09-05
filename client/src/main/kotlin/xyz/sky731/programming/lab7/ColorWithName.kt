package xyz.sky731.programming.lab7

import java.awt.Color

class ColorWithName(val color: Color, val name: String) {
  override fun toString(): String {
    return name
  }

  constructor(colorWithName: ColorWithName) : this(Color(colorWithName.color.rgb), colorWithName.name)


}