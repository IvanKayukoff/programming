package xyz.sky731.programming.lab8

import java.lang.IllegalArgumentException
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.PriorityBlockingQueue
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaType

@Target(AnnotationTarget.CLASS)
annotation class Table(val name: String)

@Target(AnnotationTarget.PROPERTY)
annotation class Id

@Target(AnnotationTarget.PROPERTY)
annotation class OneToMany(val cls: KClass<*>, val foreignKey: String)

@Target(AnnotationTarget.PROPERTY)
annotation class ForeignKey

class SimpleORM(url: String, username: String, password: String) {
  val connection: Connection = DriverManager.getConnection(url, username, password)

  /**
   * @param T is a base type
   * [T] class may contain a few fields which marked with @OneToMany annotation
   **/
  inline fun <reified T : Any> createTable() {
    createTableFromClass(T::class)
  }

  fun createTableFromClass(cls: KClass<*>, foreignKeyRef: Pair<String, String>? = null) {
    val tableName = getTableName(cls)
    val fields = cls.declaredMemberProperties.filter { it.annotations.any { it is OneToMany } }


    val str = cls.declaredMemberProperties.filterNot {
      it.annotations.any { annotation -> annotation is OneToMany }
    }.map {
      var type = convert(it.returnType.javaType.typeName)
      if (it.annotations.any { it is Id }) {
        type = "serial primary key"
      }
      it.name + " " + type + if (it.returnType.isMarkedNullable) "" else " not null"
    }.let { tableFields ->
      if (foreignKeyRef != null)
        tableFields + (foreignKeyRef.first + " integer references " + foreignKeyRef.second)
      else tableFields
    }.joinToString(",")
    println(str)

    connection.createStatement().executeUpdate("create table " + tableName + "( " + str + ")")

    fields.forEach { field ->
      val childAnnot = (field.annotations.find { it is OneToMany } as OneToMany)
      createTableFromClass(childAnnot.cls, Pair(childAnnot.foreignKey, tableName))
    }
  }


  inline fun <reified T : Any> selectAll(): PriorityBlockingQueue<T> {
    return selectAllFromClass(T::class) as PriorityBlockingQueue<T>
  }

  /** Selects all [T] elements from db, also fills ArrayList<[U]> which marked as @OneToMany property */
  fun selectAllFromClass(cls: KClass<*>): PriorityBlockingQueue<Any> {
    val tableName = getTableName(cls)
    val statement = connection.prepareStatement("select * from $tableName")
    val response = statement.executeQuery()
    val queue = PriorityBlockingQueue<Any>()

    while (response.next()) {
      val constr = cls.constructors.elementAt(0)
      val recordParams = constr.parameters.map {
        when (it.type.javaType.typeName) {
          "java.lang.String" -> response.getString(it.name)
          "java.time.ZonedDateTime" -> {
            val (datetime, offset) = """(.+)([+-]\d\d)$""".toRegex().matchEntire(response.getString(it.name))!!.destructured
            val datetimeLocal = LocalDateTime.parse(datetime.replace(" ", "T"))
            val zoneId = ZoneId.of(offset)
            ZonedDateTime.ofLocal(datetimeLocal, zoneId, ZoneOffset.of(offset))
          }
          "int" -> response.getInt(it.name)
          "boolean" -> response.getBoolean(it.name)
          else -> response.getObject(it.name)
        }
      }.toTypedArray()
      val record = constr.call(*recordParams)
      queue.add(record)
    }
    return queue
  }

  inline fun <reified T : Any> selectAllChildren(foreignId: Int): ArrayList<T> {
    val tableName = getTableName(T::class as KClass<Any>)
    val foreignKey = getForeignKey(T::class as KClass<Any>)
    val statement = connection.prepareStatement("select * from $tableName where ${foreignKey?.name}=$foreignId")
    val response = statement.executeQuery()
    val list = ArrayList<T>()

    while (response.next()) {
      val constr = T::class.constructors.elementAt(0)
      val argumentArray = constr.parameters.map {
        when (it.type.javaType.typeName) {
          "java.lang.String" -> response.getString(it.name)
          "java.time.ZonedDateTime" ->
            LocalDateTime.parse(response.getString(it.name), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) // FIXME ZoneId
                .atZone(ZoneId.of("Europe/Moscow"))
          "int" -> response.getInt(it.name)
          "boolean" -> response.getBoolean(it.name)
          else -> response.getObject(it.name)
        }
      }.toTypedArray()

      list.add(constr.call(*argumentArray))
    }
    return list
  }

  /**
   * Inserts [any] to db
   * [any] may have a few ArrayList as his fields, which should be marked with @OneToMany annotation
   * @param T is a base element's type
   */
  inline fun <reified T : Any> insert(any: Any) {
    insertFromClass(any, T::class)
  }

  fun insertFromClass(any: Any, cls: KClass<*>, foreignKey: Pair<String, Int>? = null) {
    if (!any.javaClass.annotations.any { it is Table })
      throw IllegalArgumentException("The argument's class is not mapped as Table")
    val tableName = getTableName(cls)
    val properties = any.javaClass.kotlin.declaredMemberProperties
        .filterNot { it.annotations.any { it is OneToMany } }
        .filterNot { it.annotations.any { it is Id } && it.get(any) == null }

    val fields = properties.map { it.name }.let { fields ->
      if (foreignKey != null) fields + foreignKey.first
      else fields
    }
    val values = properties.map { it.get(any) }.let { values ->
      if (foreignKey != null) values + foreignKey.second
      else values
    }

    val fieldsOneToMany = any.javaClass.kotlin.declaredMemberProperties
        .filter { it.annotations.any { annotation -> annotation is OneToMany } }

    fieldsOneToMany.forEach {
      val foreignName = (it.annotations.find { it is OneToMany } as OneToMany).foreignKey as String?
      val foreignValue = any.javaClass.kotlin.declaredMemberProperties
          .find { it.annotations.any { it is Id } }?.get(any) as Int?

      var foreignKey: Pair<String, Int>? = null
      if (foreignName != null && foreignValue != null) foreignKey = Pair(foreignName, foreignValue)

      val childType = (it.annotations.find { it is OneToMany } as OneToMany).cls
      val list = it.get(any) as ArrayList<Any>?
      list?.forEach { insertFromClass(it, childType, foreignKey) }
    }

    val statement = connection.prepareStatement("insert into $tableName (" + fields
        .joinToString(", ") + ") values (" + values.map { "?" }.joinToString(", ") + ")")
    println(statement)

    values.forEachIndexed { i, value ->
      if (value?.javaClass?.typeName == "java.time.ZonedDateTime")
        statement.setObject(i + 1, (value as ZonedDateTime).toOffsetDateTime())
      else
        statement.setObject(i + 1, value)
    }

    statement.executeUpdate()
  }

//  /**
//   * Finds [T] object in db with specific primary key
//   *
//   * @return [T] object if found in db, otherwise - null
//   * @param id is [T]'s primary key
//   */
//  inline fun <reified T: Any> findById(id: Int) : T? {
//    val tableName = getTableName(T::class as KClass<*>)
//    val primaryKey = T::class.declaredMembers.find { it.annotations.any { it is Id }  }?.name
//        ?: throw IllegalArgumentException("@Id annotation is missing")
//    val statement = connection.prepareStatement("select * from $tableName where $primaryKey=$id")
//    val response = statement.executeQuery()
//
//    if (response.next()) {
//      val constr = T::class.constructors.elementAt(0)
//      return constr.call(*constr.parameters.map {
//        when (it.type.javaType.typeName) {
//          "java.lang.String" -> response.getString(it.name)
//          "java.time.ZonedDateTime" ->
//            LocalDateTime.parse(response.getString(it.name), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) // FIXME ZoneId
//                .atZone(ZoneId.of("Europe/Moscow"))
//          "int" -> response.getInt(it.name)
//          "boolean" -> response.getBoolean(it.name)
//          else -> response.getObject(it.name)
//        }
//      }.toTypedArray()).also {
//        val list = T::class.declaredMemberProperties
//            .find { it.annotations.any { annotation -> annotation is OneToMany } }?.get(it) as ArrayList<Any>?
//        list?.addAll(selectAllChildren(getId(T::class)?.get(it) as Int))
//      }
//    }
//
//    return null
//  }

  /**
   * Deletes [T] object from db by primary key
   *
   * @param id is primary key with which we will delete [T] object from db
   * @param T is parent element's type
   * @param U is child element's type, [deleteChildrenByFK] will be called for them
   */
//  inline fun <reified T: Any> deleteById(id: Int) {
//    deleteChildrenByFK<U>(id)
//
//    val tableName = getTableName(T::class as KClass<*>)
//    val field = T::class.declaredMembers.find { it.annotations.any {annotation -> annotation is Id }  }?.name
//        ?: throw IllegalArgumentException("@Id annotation is missing")
//    val statement = connection.prepareStatement("delete from $tableName where $field=$id")
//    statement.executeUpdate()
//  }

  /**
   * @param foreignId is foreign key with which we will delete all children from db
   * @param T is child element's type
   */
//  inline fun <reified T: Any> deleteChildrenByFK(foreignId: Int) {
//    val tableName = getTableName(T::class as KClass<*>)
//    val field = T::class.declaredMembers.find { it.annotations.any {annotation -> annotation is ForeignKey }  }?.name
//        ?: throw IllegalArgumentException("@ForeignKey annotation is missing")
//    val statement = connection.prepareStatement("delete from $tableName where $field=$foreignId")
//    statement.executeUpdate()
//  }

  fun convert(string: String) = when (string) {
    "java.sql.Timestamp" -> "timestamp"
    "java.lang.String" -> "text"
    "int" -> "integer"
    "java.time.ZonedDateTime" -> "timestamptz"
    else -> string
  }

  /** Gets name value from "Table" annotation */
  fun getTableName(cls: KClass<*>) = cls.annotations.find { it is Table }?.let { (it as Table).name.toLowerCase() }
      ?: throw IllegalArgumentException("This class is not mapped as Table")

  /** Gets the first field with "Id" annotation */
  fun getId(cls: KClass<*>) = cls.declaredMemberProperties.find {
    it.annotations.any { annotation -> annotation is Id }
  }

  /** Gets the first field with "ForeignKey" annotation */
  fun getForeignKey(cls: KClass<*>) = cls.declaredMemberProperties.find {
    it.annotations.any { annotation -> annotation is ForeignKey }
  }
}
