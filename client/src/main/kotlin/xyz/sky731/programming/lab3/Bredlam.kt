package xyz.sky731.programming.lab3

import xyz.sky731.programming.lab8.Id
import xyz.sky731.programming.lab8.OneToMany
import xyz.sky731.programming.lab8.Table
import java.io.Serializable
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime


@Table("Bredlam")
data class Bredlam(var name: String = "NoNameBredlam", var endOfLight: Boolean = false,
                   var flagColor: String = "Blue", var x: Int = 0, var y: Int = 0,
                   val creation: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault()),
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

  override fun compareTo(other: Bredlam) = people.size - other.people.size

  fun size() = people.size

  override fun toString() = name

}