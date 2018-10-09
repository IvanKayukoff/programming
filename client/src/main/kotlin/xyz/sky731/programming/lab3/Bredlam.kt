package xyz.sky731.programming.lab3

import xyz.sky731.programming.lab8.Id
import xyz.sky731.programming.lab8.OneToMany
import xyz.sky731.programming.lab8.Table
import java.io.Serializable
import java.time.ZoneId
import java.time.ZonedDateTime


@Table("Bredlam")
data class Bredlam(var name: String = "NoNameBredlam", var endOfLight: Boolean = false,
                   var flagColor: String = "Blue", var x: Int = 0, var y: Int = 0,
                   var creation: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault()),
                   @Id var id: Int? = null) : Serializable, Comparable<Bredlam> {

  @OneToMany(cls = Human::class, foreignKey = "bredlam_id")
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

  override fun equals(other: Any?): Boolean {
    if (other == null) {
      return false
    } else {
      if (other.javaClass != this.javaClass) return false

      val o = other as Bredlam
      if (name == o.name && endOfLight == o.endOfLight && flagColor == o.flagColor && x == o.x
          && y == o.y && id == o.id) return true
    }
    return false
  }

  override fun hashCode(): Int {
    return super.hashCode()
  }

  override fun compareTo(other: Bredlam) = people.size - other.people.size

  fun size() = people.size

  override fun toString() = name

}
