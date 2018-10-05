package xyz.sky731.programming.lab8

import org.junit.Test
import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab3.Human
import java.time.ZoneId
import java.time.ZonedDateTime

class SimpleORMTest {

  @Test
  fun createTableTest() {
    val bredlam = Bredlam("TestBredlam", false, "Blue", 1, 0,
        ZonedDateTime.now(ZoneId.systemDefault()), 1)
    val orm = SimpleORM("jdbc:postgresql://localhost:5432/db", "postgres", "admin")
    orm.createTable<Bredlam, Human>()
  }
}