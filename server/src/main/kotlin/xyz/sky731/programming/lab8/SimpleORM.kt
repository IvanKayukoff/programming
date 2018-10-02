package xyz.sky731.programming.lab8

import java.lang.IllegalArgumentException
import java.sql.Connection
import java.sql.DriverManager
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.jvm.javaType

@Target(AnnotationTarget.CLASS)
annotation class Table(val name: String)

@Target(AnnotationTarget.PROPERTY)
annotation class Id

@Target(AnnotationTarget.PROPERTY)
annotation class OneToMany

class SimpleORM(url: String, username: String, password: String) {
  val connection: Connection = DriverManager.getConnection(url, username, password)

  /**
   * @param T is a base type, which contains [U] field
   * @param U is a aggregate type
   * [T] class may contain no more than one [U] field
   **/
  inline fun <reified T : Any, reified U : Any> createTable() {

    val tableName = getTableName<T>()

    /**
     * Creates a new table for every field with "OneToMany" annotation.
     * Children can't have a "OneToMany" annotation
     **/
    T::class.declaredMembers.filter { it.annotations.any { annotation -> annotation is OneToMany } }
        .map { createChildTable<U>() }

    val str = getInitStr<T>()

    connection.createStatement().executeUpdate("create table " + tableName + "( " + str.joinToString(",") + ")")
  }

  inline fun <reified T : Any> createChildTable() {

    val tableName = getTableName<T>()
    val str = getInitStr<T>()

    connection.createStatement().executeUpdate("create table " + tableName + "( " + str.joinToString(",") + ")")
  }

  fun convert(string: String) = when (string) {
    "java.time.LocalDateTime" -> "timestamp"
    "java.lang.String" -> "text"
    "int" -> "integer"
    "xyz.sky731.programming.lab7.ColorWithName" -> "text"
    else -> string
  }

  /** Creates initialization string which afterwards creates table */
  inline fun <reified T : Any> getInitStr() = T::class.declaredMemberProperties.filter {
    it.annotations.find { annotation -> annotation is OneToMany } == null
  }.map {
    it.name + " " + convert(it.returnType.javaType.typeName) +
        if (it.annotations.any { annotation -> annotation is Id }) " primary key" else "" +
            if (it.returnType.isMarkedNullable) "" else " not null"
  }

  /** Gets name value from "Table" annotation */
  inline fun <reified T : Any> getTableName() = T::class.annotations.find { it is Table }?.let { (it as Table).name }
      ?: throw IllegalArgumentException("This class is not mapped as Table")

  /** Gets the first field with "Id" annotation */
  inline fun <reified T : Any> getId() = T::class.declaredMembers.find { it.annotations.any { annotation -> annotation is Id } }
}

