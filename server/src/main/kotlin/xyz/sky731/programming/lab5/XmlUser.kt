package xyz.sky731.programming.lab5

import com.thoughtworks.xstream.XStream
import java.io.File

class XmlUser(val filename: String) {
  fun marshal(bredlams: Bredlams) {
    try {
      val xstream = XStream()
      val xml = xstream.toXML(bredlams)
      val fileReadWriter = FileReadWriter(filename)
      fileReadWriter.write(xml)
    } catch (e: Exception) {
      e.printStackTrace()
      println("Error while writing XML to file: \"$filename\"")
    }

  }

  fun unmarshal(): Bredlams? {
    try {
      val xstream = XStream()
      val bredlams = xstream.fromXML(File(filename)) as Bredlams
      return bredlams
    } catch (e: Exception) {
      e.printStackTrace()
      println("Wrong XML")
    }
    return null
  }
}