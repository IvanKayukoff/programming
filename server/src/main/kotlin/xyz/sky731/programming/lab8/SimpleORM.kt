package xyz.sky731.programming.lab8

import java.lang.IllegalArgumentException
import java.sql.DriverManager

annotation class Table(val name: String)
annotation class Id
annotation class OneToMany

class SimpleORM(url: String, username: String, password: String) {
  val connection = DriverManager.getConnection(url, username, password)

  inline fun <reified T: Any> createTable() {
    val tableName = T::class.annotations.find { it is Table }?.let { (it as Table).name }
        ?: throw IllegalArgumentException("This class is not mapped as Table")


  }

  inline fun <reified T: Any> createTable(foreignId: Int, foreignName: String) {

  }
}

