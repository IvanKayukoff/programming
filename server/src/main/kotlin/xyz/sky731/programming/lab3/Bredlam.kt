package xyz.sky731.programming.lab3

import xyz.sky731.programming.lab7.ColorWithName
import xyz.sky731.programming.lab8.Table
import java.awt.Color
import java.io.Serializable

@Table("Bredlam")
data class Bredlam(var name: String = "NoNameBredlam") : Serializable, Comparable<Bredlam> {

  override fun compareTo(other: Bredlam): Int {
    return people.size - other.people.size
  }

  val people = ArrayList<Human>()
  var endOfLight = false
  var flagColor = ColorWithName(Color.RED, "Red")
  var x = 0
  var y = 0

  companion object {
    class BredlamNameComp : Comparator<Bredlam> {
      override fun compare(p0: Bredlam?, p1: Bredlam?): Int {
        if (p0 != null && p1 != null) {
          return p0.name.compareTo(p1.name)
        }
        return 0
      }
    }
  }

  fun size() : Int {
    return people.size
  }

  override fun toString() = name

}
