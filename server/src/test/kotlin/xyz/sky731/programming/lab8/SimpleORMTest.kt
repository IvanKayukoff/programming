package xyz.sky731.programming.lab8

import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.BeforeAll
import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab3.Human
import java.time.ZoneId
import java.time.ZonedDateTime

class SimpleORMTest {
  val orm = SimpleORM("jdbc:postgresql://localhost:5432/postgres", "sky", "sky")

  @Before
  fun dropAllTables() {
    orm.connection.createStatement().executeUpdate("drop table human")
    orm.connection.createStatement().executeUpdate("drop table bredlam")
  }

  @Test
  fun createTableTest() {
    orm.createTable<Bredlam>()
  }

//  @Test
//  fun insertTest() {
//    val bredlam = Bredlam("TestBredlam", false, "Blue", 1, 0,
//        ZonedDateTime.now(ZoneId.systemDefault()), 1)
//    orm.insert<Bredlam, Human>(bredlam)
//
//  }
}