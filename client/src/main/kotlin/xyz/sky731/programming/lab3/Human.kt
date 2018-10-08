package xyz.sky731.programming.lab3

import java.io.Serializable

class Human(var name: String, var money: Int, var id: Int? = null) : Serializable {

  constructor(name: String, money: Int) : this(name, money, null)

  override fun toString() = name
}
