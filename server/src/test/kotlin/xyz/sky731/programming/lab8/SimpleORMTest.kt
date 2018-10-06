package xyz.sky731.programming.lab8

import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab3.Human
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

class SimpleORMTest {
  val orm = SimpleORM("jdbc:postgresql://localhost:5432/postgres", "postgres", "admin")

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

    orm.insert(bredlam)
  }

  @Test
  fun insertTest2() {
    val bredlam1 = Bredlam("SecondaryBredlam", false, "Red", 0, 1,
        ZonedDateTime.now(ZoneId.systemDefault()))
    val bredlam2 = Bredlam("OtherBredlam", true, "Green", 0, -15,
        ZonedDateTime.now(ZoneId.systemDefault()), 100)
    val bredlam3 = Bredlam("Kuliti!")

    orm.insert(bredlam1)
    orm.insert(bredlam2)
    orm.insert(bredlam3)
  }

  @Test
  fun insertSelectTest() {
    val creation = ZonedDateTime.now(ZonedDateTime.now(ZoneOffset.systemDefault()).offset)
    val bredlam = Bredlam("CockoffHere!", false, "Red", -1, 1, creation)
    orm.insert(bredlam)

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

    val bredlam2 = Bredlam("P3202")
    bredlam2.people.add(Human("Kurman", 9000000))
    bredlam2.people.add(Human("Andrew Memov", 0))

    orm.insert(bredlam)
    orm.insert(bredlam2)

    val bredlams = orm.selectAll<Bredlam>().toList()
    assertEquals(2, bredlams.size)
    assertEquals(2, bredlams[0].people.size)
    assertEquals("NintendoMan", bredlams[0].people[0].name)
    assertEquals(2000, bredlams[0].people[0].money)
    assertEquals("Rogogit", bredlams[0].people[1].name)
    assertEquals(1000, bredlams[0].people[1].money)

    assertEquals(2, bredlams[1].people.size)
    assertEquals("Kurman", bredlams[1].people[0].name)
    assertEquals(9000000, bredlams[1].people[0].money)
    assertEquals("Andrew Memov", bredlams[1].people[1].name)
    assertEquals(0, bredlams[1].people[1].money)
  }

  @Test
  fun insertAndFindByIdTest() {
    for (i in 0..10) {
      orm.insert(Bredlam("$i Bredlam").apply {
        this.people.add(Human("Slave of $i", i))
      })
    }
    val bredlams = orm.selectAll<Bredlam>()
    assertEquals(11, bredlams.size)

    val foundById = orm.findById<Bredlam>(1)
    assertEquals("0 Bredlam", foundById?.name)
    assertEquals(1, foundById?.people?.size)
    assertEquals("Slave of 0", foundById?.people?.get(0)?.name)
  }

  @Test
  fun insertAndDeleteByIdTest() {
    for (i in 0..10) {
      orm.insert(Bredlam("$i Bredlam").apply {
        this.people.add(Human("Slave of $i", i))
      })
    }

    val bredlams = orm.selectAll<Bredlam>()
    assertEquals(11, bredlams.size)
    for (i in 1..11) {
      orm.deleteById<Bredlam>(i)
      assertEquals(11 - i, orm.selectAll<Bredlam>().size)
    }
  }

  @Test
  fun updateTest() {
    val bredlam1 = Bredlam("XBOX KVEST")
    val bredlam2 = Bredlam("Gorenje")
    orm.insert(bredlam1)
    orm.insert(bredlam2)

    bredlam1.name = "XBOX KVEST SPL"
    orm.update(bredlam1)

    val bredlams = orm.selectAll<Bredlam>().map { it.name }
    assertArrayEquals(arrayOf("XBOX KVEST SPL", "Gorenje").sortedArray(), bredlams.toTypedArray())
  }


}