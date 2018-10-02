package xyz.sky731.programming.lab3

import xyz.sky731.programming.lab8.Table
import java.io.Serializable

@Table("Human")
class Human(var name: String, var money: Int) : Serializable {
  override fun toString() = name
}
