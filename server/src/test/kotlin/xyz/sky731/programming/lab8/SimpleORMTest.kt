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
  fun insertTestWithMan() {
    val bredlam = Bredlam("CockoffHere!")
    bredlam.people.add(Human("Cockoff", 1000, 1))

    orm.insert<Bredlam>(bredlam)
  }

  @Test
  fun insertTestWithPeople() {
    val bredlam = Bredlam("GuysHere!")
    bredlam.people.add(Human("NintendoMan", 2000))
    bredlam.people.add(Human("Rogogit", 1000))

    orm.insert<Bredlam>(bredlam)
  }

}