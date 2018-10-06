package xyz.sky731.programming.lab8

import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.jupiter.api.BeforeAll
import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab3.Human
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

class SimpleORMTest {
  val orm = SimpleORM("jdbc:postgresql://localhost:5432/postgres", "sky", "sky")

  @Before
  fun dropAllTables() {
    orm.connection.createStatement().executeUpdate("drop table if exists human")
    orm.connection.createStatement().executeUpdate("drop table if exists bredlam")

    orm.createTable<Bredlam>()
  }


  @Test
  fun insertTest1() {
    val bredlam = Bredlam("PrimaryBredlam", false, "Blue", 1, 12,
        ZonedDateTime.now(ZoneId.systemDefault()))

    orm.insert<Bredlam>(bredlam)
  }

  @Test
  fun insertTest2() {
    val bredlam1 = Bredlam("SecondaryBredlam", false, "Red", 0, 1,
        ZonedDateTime.now(ZoneId.systemDefault()))
    val bredlam2 = Bredlam("OtherBredlam", true, "Green", 0, -15,
        ZonedDateTime.now(ZoneId.systemDefault()), 100)
    val bredlam3 = Bredlam("Kuliti!")

    orm.insert<Bredlam>(bredlam1)
    orm.insert<Bredlam>(bredlam2)
    orm.insert<Bredlam>(bredlam3)
  }

  @Test
  fun insertSelectTest() {
    val creation = ZonedDateTime.now(ZonedDateTime.now(ZoneOffset.systemDefault()).offset)
    val bredlam = Bredlam("CockoffHere!", false, "Red", -1, 1, creation)
    orm.insert<Bredlam>(bredlam)

    val bredlams = orm.selectAll<Bredlam>().toList()

    assertEquals(1, bredlams.size)
    assertEquals("CockoffHere!", bredlams[0].name)
    assertEquals(false, bredlams[0].endOfLight)
    assertEquals("Red", bredlams[0].flagColor)
    assertEquals(-1, bredlams[0].x)
    assertEquals(1, bredlams[0].y)
    assertEquals(creation, bredlams[0].creation)
  }

  @Test
  fun insertSelectTestWithPeople() {
    val bredlam = Bredlam("GuysHere!")
    bredlam.people.add(Human("NintendoMan", 2000))
    bredlam.people.add(Human("Rogogit", 1000))

    orm.insert<Bredlam>(bredlam)

    val bredlams = orm.selectAll<Bredlam>().toList()
    assertEquals(1, bredlams.size)
    assertEquals(2, bredlams[0].people.size)
    assertEquals("NintendoMan", bredlams[0].people[0].name)
    assertEquals(2000, bredlams[0].people[0].money)
    assertEquals("Rogogit", bredlams[0].people[1].name)
    assertEquals(1000, bredlams[0].people[1].money)
  }

}