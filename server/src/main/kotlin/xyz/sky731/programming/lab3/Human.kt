package xyz.sky731.programming.lab3

import xyz.sky731.programming.lab8.ForeignKey
import xyz.sky731.programming.lab8.Id
import xyz.sky731.programming.lab8.Table
import java.io.Serializable

@Table("Human")
class Human(var name: String, var money: Int, @Id val id: Int? = null) : Serializable {

  constructor(name: String, money: Int) : this(name, money, null)

  override fun toString() = name
}
