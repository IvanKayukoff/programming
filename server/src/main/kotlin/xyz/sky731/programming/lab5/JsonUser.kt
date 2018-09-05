package xyz.sky731.programming.lab5

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver


class JsonUser {
  fun unmarshal (data: String): Bredlams {
    val xstream = XStream(JettisonMappedXmlDriver())
    return xstream.fromXML(data) as Bredlams
  }

  fun marshal (bredlams: Bredlams): String {
    val xstream = XStream(JettisonMappedXmlDriver())
    return xstream.toXML(bredlams)
  }
}