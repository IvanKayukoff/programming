package xyz.sky731.programming.lab6

import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab5.Bredlams
import java.io.Serializable
import java.time.ZonedDateTime

class BredlamsTransporter : Serializable {

  private val pseudoBredlams = ArrayList<PseudoBredlam>()

  fun setBredlams(bredlams: Bredlams) {
    pseudoBredlams.clear()
    for (item in bredlams.bredlam) {
      pseudoBredlams.add(realToPseudoBredlam(item))
    }
  }

  fun getBredlams(): Bredlams {
    val result = Bredlams()
    result.bredlam = ArrayList<Bredlam>()
    for (item in pseudoBredlams) {
      result.bredlam.add(pseudoToRealBredlam(item))
    }
    return result
  }

  fun realToPseudoBredlam(b: Bredlam) =
      PseudoBredlam(b.name, b.endOfLight, b.flagColor, b.x, b.y, b.creation.toString(), b.people)

  fun pseudoToRealBredlam(pb: PseudoBredlam) =
      Bredlam(pb.name, pb.endOfLight, pb.flagColor, pb.x, pb.y, ZonedDateTime.parse(pb.creation))
          .apply {
            if (pb.people != null) {
              people.addAll(pb.people)
            }
          }
}