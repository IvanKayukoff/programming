package xyz.sky731.programming.lab3

import xyz.sky731.programming.lab7.ColorWithName
import xyz.sky731.programming.lab8.Id
import xyz.sky731.programming.lab8.OneToMany
import xyz.sky731.programming.lab8.Table
import java.awt.Color
import java.io.Serializable

@Table("Bredlam")
data class Bredlam(var name: String = "NoNameBredlam") : Serializable, Comparable<Bredlam> {

  @OneToMany
  val people = ArrayList<Human>()
  var endOfLight = false
  var flagColor = ColorWithName(Color.RED, "Red")
  var x = 0
  var y = 0

  @Id
  val id = amount

  init {
    amount++
  }

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

  override fun compareTo(other: Bredlam) = people.size - other.people.size

  fun size() = people.size

  override fun toString() = name

}
