package xyz.sky731.programming.lab3

import java.io.Serializable
import java.time.ZoneId
import java.time.ZonedDateTime

data class Bredlam(var name: String = "NoNameBredlam", var endOfLight: Boolean = false,
                   var flagColor: String = "Blue", var x: Int = 0, var y: Int = 0,
                   var creation: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault()),
                   var id: Int? = null) : Serializable, Comparable<Bredlam> {

  var people = ArrayList<Human>()

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

  override fun compareTo(other: Bredlam) = people.size - other.people.size

  fun size() = people.size

  override fun toString() = name

}
