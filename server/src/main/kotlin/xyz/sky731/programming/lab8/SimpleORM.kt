package xyz.sky731.programming.lab8

import java.lang.IllegalArgumentException
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.PriorityBlockingQueue
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.jvm.javaType

@Target(AnnotationTarget.CLASS)
annotation class Table(val name: String)

@Target(AnnotationTarget.PROPERTY)
annotation class Id

@Target(AnnotationTarget.PROPERTY)
annotation class OneToMany

@Target(AnnotationTarget.PROPERTY)
annotation class ForeignKey

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

   /** Selects all [T] elements from db, also fills ArrayList<[U]> which marked as @OneToMany property */
  inline fun <reified T: Any, reified U: Any> selectAll() : PriorityBlockingQueue<T> {
    val tableName = getTableName<T>()
    val statement = connection.prepareStatement("select * from $tableName")
    val response = statement.executeQuery()
    val queue = PriorityBlockingQueue<T>()

    while (response.next()) {
      val constr = T::class.constructors.elementAt(0)
      queue.add(constr.call(*constr.parameters.map {
        when (it.type.javaType.typeName) {
          "java.lang.String" -> response.getString(it.name)
          "java.time.ZonedDateTime" ->
            LocalDateTime.parse(response.getString(it.name), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) // FIXME ZoneId
                .atZone(ZoneId.of("Europe/Moscow"))
          "int" -> response.getInt(it.name)
          "boolean" -> response.getBoolean(it.name)
          else -> response.getObject(it.name)
        }
      }.toTypedArray()).also {
        val list = T::class.declaredMemberProperties.find { it.annotations.any { annotation -> annotation is OneToMany } } as ArrayList<U>?
        list?.addAll(selectAllChildren<U>(getId<T>()?.get(it) as Int))
      })
    }
    return queue
  }

  // FIXME Can I refactor this hell ? I don't think so..
  inline fun <reified T: Any> selectAllChildren(foreignId: Int) : ArrayList<T> {
    val tableName = getTableName<T>()
    val foreignKey = getForeignKey<T>()
    val statement = connection.prepareStatement("select * from $tableName where ${foreignKey?.name}=$foreignId")
    val response = statement.executeQuery()
    val list = ArrayList<T>()

    while (response.next()) {
      val constr = T::class.constructors.elementAt(0)
      list.add(constr.call(*constr.parameters.map {
        when (it.type.javaType.typeName) {
          "java.lang.String" -> response.getString(it.name)
          "java.time.ZonedDateTime" ->
            LocalDateTime.parse(response.getString(it.name), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) // FIXME ZoneId
                .atZone(ZoneId.of("Europe/Moscow"))
          "int" -> response.getInt(it.name)
          "boolean" -> response.getBoolean(it.name)
          else -> response.getObject(it.name)
        }
      }.toTypedArray()))
    }
    return list
  }

  fun convert(string: String) = when (string) {
    "java.sql.Timestamp" -> "timestamp"
    "java.lang.String" -> "text"
    "int" -> "integer"
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
  inline fun <reified T : Any> getId() = T::class.declaredMemberProperties.find {
    it.annotations.any { annotation -> annotation is Id }
  }

  /** Gets the first field with "ForeignKey" annotation */
  inline fun <reified T : Any> getForeignKey() = T::class.declaredMemberProperties.find {
    it.annotations.any { annotation -> annotation is ForeignKey }
  }
}
