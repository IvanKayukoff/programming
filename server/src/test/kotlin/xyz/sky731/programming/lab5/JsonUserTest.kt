package xyz.sky731.programming.lab5

import org.junit.Test
import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab3.Human
import xyz.sky731.programming.lab7.ColorWithName
import java.awt.Color

class JsonUserTest {

  @Test
  fun marshallingBredlams() {
    val bredlams = Bredlams()
    bredlams.bredlam = ArrayList<Bredlam>()
    bredlams.bredlam.apply {
      add(Bredlam("BlueGuys", Human("Cockoff", 100), Human("Nintendo", 100000)))
      add(Bredlam("PinkHmm", Human("Manya", 13)).apply {
        flagColor = ColorWithName(Color.PINK, "Pink")
        endOfLight = true
        coordinates.x = 15
        coordinates.y = 13
      })
    }

    val jsonUser = JsonUser()
    val jsonData = jsonUser.marshal(bredlams)
    println(jsonData)

  }

  @Test
  fun unmarshallingBredlams() {
    val jsonData = "{\"xyz.sky731.programming.lab5.Bredlams\":{\"bredlam\":[{\"xyz.sky731.programming.lab3.Bredlam\":[{\"name\":\"BlueGuys\",\"humans\":[{\"xyz.sky731.programming.lab3.Human\":[{\"money\":100,\"buildings\":[{\"xyz.sky731.programming.lab3.Home\":{\"cost\":100,\"id\":0}}],\"name\":\"Cockoff\"},{\"money\":100000,\"buildings\":[{\"xyz.sky731.programming.lab3.Home\":{\"cost\":100,\"id\":1}}],\"name\":\"Nintendo\"}]}],\"endOfLight\":false,\"coordinates\":{\"x\":0,\"y\":0}},{\"name\":\"PinkHmm\",\"humans\":[{\"xyz.sky731.programming.lab3.Human\":{\"money\":13,\"buildings\":[{\"xyz.sky731.programming.lab3.Home\":{\"cost\":100,\"id\":2}}],\"name\":\"Manya\"}}],\"endOfLight\":true,\"flagColor\":{\"color\":{\"red\":255,\"green\":175,\"blue\":175,\"alpha\":255},\"name\":\"Pink\"},\"coordinates\":{\"x\":15,\"y\":13}}]}]}}"

    val jsonUser = JsonUser()

    println(jsonData)

    val bredlams = jsonUser.unmarshal(jsonData)
    bredlams.bredlam.forEach {
      println(it)
      it.humans.forEach {
        println("\t" + it)
      }
    }
  }
}