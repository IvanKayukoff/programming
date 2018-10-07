package xyz.sky731.programming.lab5

import org.junit.Assert
import org.junit.Test
import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab6.BredlamsTransporter

class JsonUserTest {

  @Test
  fun marshallingBredlams() {
    val bredlams = Bredlams()
    bredlams.bredlam = ArrayList<Bredlam>()
    bredlams.bredlam.apply {
      add(Bredlam("BlueGuys"))
      add(Bredlam("PinkHmm").apply {
        flagColor = "Pink"
        endOfLight = true
        x = 15
        y = 13
      })
    }

    val jsonUser = JsonUser()
    val jsonData = jsonUser.marshal(BredlamsTransporter().apply { setBredlams(bredlams) })
  }

  @Test
  fun unmarshallingTest() {
    val json = "{\"xyz.sky731.programming.lab6.BredlamsTransporter\":{\"pseudoBredlams\":[{\"xyz.sky731.programming" +
        ".lab6.PseudoBredlam\":[{\"name\":\"BlueGuys\",\"endOfLight\":false,\"flagColor\":\"Blue\",\"x\":0,\"y\":0,\"" +
        "creation\":\"2018-10-07T20:26:48.952+03:00[Europe/Moscow]\"},{\"name\":\"PinkHmm\",\"endOfLight\":true,\"" +
        "flagColor\":\"Pink\",\"x\":15,\"y\":13,\"creation\":\"2018-10-07T20:26:48.953+03:00[Europe/Moscow]\"}]}]}}"
    val jsonUser = JsonUser()
    val transporter = jsonUser.unmarshal(json)
    val bredlams = transporter.getBredlams()

    Assert.assertEquals("BlueGuys", bredlams.bredlam[0].toString())
    Assert.assertEquals("PinkHmm", bredlams.bredlam[1].toString())
  }
}