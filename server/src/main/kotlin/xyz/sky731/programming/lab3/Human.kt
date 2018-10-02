package xyz.sky731.programming.lab3

import xyz.sky731.programming.lab8.Id
import xyz.sky731.programming.lab8.Table
import java.io.Serializable

@Table("Human")
class Human(var name: String, var money: Int, val bredlamId: Int) : Serializable {

  @Id
  val id = amount

  init {
    amount++
  }

  companion object {
    var amount = 1
  }

  override fun toString() = name
}
