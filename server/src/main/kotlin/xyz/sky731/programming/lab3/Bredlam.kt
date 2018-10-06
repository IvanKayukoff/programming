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
data class Bredlam(var name: String, var endOfLight: Boolean,
                   var flagColor: String, var x: Int, var y: Int, val creation: ZonedDateTime,
                   @Id val id: Int) : Serializable, Comparable<Bredlam> {

  @OneToMany(Human::class)
  val people = ArrayList<Human>()

  init {
    amount++
  }

  constructor(name: String = "NoNameBredlam") : this(name, false,
      "Red", 0, 0, ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Europe/Moscow")), amount) // FIXME ZoneId

  companion object {
    var amount = 1
    class BredlamNameComp : Comparator<Bredlam> {
      override fun compare(p0: Bredlam?, p1: Bredlam?): Int {
        if (p0 != null && p1 != null) {
          return p0.name.compareTo(p1.name)
        }
        return 0
      }
    }
  }

  fun creationTimestamp() = Timestamp.valueOf(creation.toLocalDateTime())

  override fun compareTo(other: Bredlam) = people.size - other.people.size

  fun size() = people.size

  override fun toString() = name

}
