package xyz.sky731.programming.lab8

import xyz.sky731.programming.lab7.ColorWithName
import java.lang.IllegalArgumentException
import java.sql.Connection
import java.sql.DriverManager
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.jvm.javaType

annotation class Table(val name: String)
annotation class Id
annotation class OneToMany

class SimpleORM(url: String, username: String, password: String) {
  val connection: Connection = DriverManager.getConnection(url, username, password)

  inline fun <reified T: Any> createTable() {
    val tableName = T::class.annotations.find { it is Table }?.let { (it as Table).name }
        ?: throw IllegalArgumentException("This class is not mapped as Table")

    /** Gets the first field with "Id" annotation */
    val id = T::class.declaredMembers.find { it.annotations.any { annotation -> annotation is Id } }

    // TODO create a new table for every field with "OneToMany" annotation
    T::class.declaredMembers.filter { it.annotations.any {annotation ->  annotation is OneToMany } }
        .map {  }

    /** Takes all fields without "OneToMany" annotation and create List<String> using them */
    val str = T::class.declaredMemberProperties.filter { it.annotations.find { annotation -> annotation is OneToMany } == null }
        .map { it.name + " " +  convert(it.returnType.javaType.typeName) +
            if (it.annotations.any { annotation -> annotation is Id }) " primary key" else "" +
            if (it.returnType.isMarkedNullable) "" else " not null"
        }

    connection.createStatement().executeUpdate("create table " + tableName + "( " + str.joinToString(",") + ")")
  }

  inline fun <reified T: Any> createTable(foreignId: Int, foreignName: String) {

  }

  fun convert(string: String) = when(string) {
    "java.time.LocalDateTime" -> "timestamp"
    "java.lang.String" -> "text"
    "int" -> "integer"
    "xyz.sky731.programming.lab7.ColorWithName" -> "text"
    else -> string
  }
}

