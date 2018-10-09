package xyz.sky731.programming.lab6

import xyz.sky731.programming.lab3.Bredlam
import java.io.*
import java.net.InetSocketAddress
import java.net.PortUnreachableException
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel

class ClientMain(val host: String, val port: Int) {

  fun sendMessage(message: String, arg: Bredlam?): String {
    val baos = ByteArrayOutputStream()
    ObjectOutputStream(baos).apply {
      writeObject(Request(message, arg))
      flush()
    }

    val buffer = ByteBuffer.allocate(16000)
    buffer.put(baos.toByteArray())

    val hash = HashSum.MD5(String(baos.toByteArray()))
    buffer.put(hash!!.toByteArray())
    buffer.flip()

    val address = InetSocketAddress(host, port)
    val channel = DatagramChannel.open().apply {
      connect(address)
      send(buffer, address)
    }
    buffer.clear()
    println("Message \"$message\" sent successfully")

    /** Getting response */
    try {
      channel.receive(buffer)
    } catch (e: PortUnreachableException) {
      println("Server unavailable :( Try later..")
    }

    buffer.flip()

    val stringBuilder = StringBuilder()
    while (buffer.hasRemaining()) {
      stringBuilder.append(buffer.get().toChar())
    }

    return stringBuilder.toString()
  }
}

fun main(args: Array<String>) {

  val sender = ClientMain("localhost", 26425)

  val reader = BufferedReader(InputStreamReader(System.`in`))
  var message = ""
  while (true) {
    try {
      message = reader.readLine()
    } catch (e: IOException) {
      println("Oops, console has been closed :(")
      System.exit(1)
    }

    val response = message.run { sender.sendMessage(this@run, null) }
    println(response)
  }
}