package xyz.sky731.programming.lab5

import org.junit.Test
import org.junit.jupiter.api.Assertions
import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab3.Human

class XmlUserTest {
  val xmlUser = XmlUser("queueFile")

  @Test
  fun marshallingBredlam() {
    val bredlams = Bredlams()
    val bredlamsList = ArrayList<Bredlam>()
    bredlamsList.add(Bredlam("Hello?").apply {
      this.humans.add(Human("Cockoff", 100000))
      this.humans.add(Human())
    })
    bredlams.bredlam = bredlamsList


    try {
      xmlUser.marshal(bredlams)
    } catch (e: Exception) {
      e.printStackTrace()
      assert(false)
    }
  }

  @Test
  fun unmarshallingBredlam() {
    val bredlams = xmlUser.unmarshal()
    bredlams?.bredlam?.forEach {
      println(it)
      it.humans.forEach {
        print("\t")
        println(it)
      }
    }
  }
}