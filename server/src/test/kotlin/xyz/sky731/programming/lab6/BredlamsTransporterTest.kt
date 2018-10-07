package xyz.sky731.programming.lab6

import org.junit.Assert.assertEquals
import org.junit.Test
import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab3.Human

class BredlamsTransporterTest {
  @Test
  fun realToPseudo_then_pseudoToReal() {
    val bredlam = Bredlam().apply { people.add(Human("Man", 10001)) }
    val transporter = BredlamsTransporter()
    val pseudo = transporter.realToPseudoBredlam(bredlam)
    val reversedBredlam = transporter.pseudoToRealBredlam(pseudo)

    assertEquals(bredlam.creation, reversedBredlam.creation)
    assertEquals(bredlam.name, reversedBredlam.name)
    assertEquals(bredlam.flagColor, reversedBredlam.flagColor)
    assertEquals(bredlam.people, reversedBredlam.people)
    assertEquals(bredlam.x, reversedBredlam.x)
    assertEquals(bredlam.y, reversedBredlam.y)
  }
}