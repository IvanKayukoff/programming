package xyz.sky731.programming.lab5

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver
import xyz.sky731.programming.lab6.BredlamsTransporter


class JsonUser {
  fun unmarshal (data: String): BredlamsTransporter {
    val xstream = XStream(JettisonMappedXmlDriver())
    return xstream.fromXML(data) as BredlamsTransporter
  }

  fun marshal (bredlams: BredlamsTransporter): String {
    val xstream = XStream(JettisonMappedXmlDriver())
    return xstream.toXML(bredlams)
  }
}