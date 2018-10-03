package xyz.sky731.programming.lab3

import xyz.sky731.programming.lab8.ForeignKey
import xyz.sky731.programming.lab8.Id
import xyz.sky731.programming.lab8.Table
import java.io.Serializable

@Table("Human")
class Human(var name: String, var money: Int, @ForeignKey val bredlamId: Int, @Id val id: Int) : Serializable {
  
  init {
    amount++
  }

  constructor(name: String, money: Int, bredlamId: Int) : this(name, money, bredlamId, amount)

  companion object {
    var amount = 1
  }

  override fun toString() = name
}
