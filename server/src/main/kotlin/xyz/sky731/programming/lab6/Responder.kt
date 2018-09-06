package xyz.sky731.programming.lab6

import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab5.CmdExecutor
import xyz.sky731.programming.lab7.TreeChange

import java.io.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.util.Arrays
import java.util.Queue

class Responder(private val socket: DatagramSocket,
                private var packet: DatagramPacket,
                filename: String,
                queue: Queue<Bredlam>,
                val callback: (List<TreeChange>) -> Unit) : Runnable {

  companion object {
    private const val HASH_SIZE = 32
  }

  private val executor = CmdExecutor(queue, filename)

  override fun run() {
    val data = String(packet.data, 0, packet.length - HASH_SIZE)
    val hash = String(packet.data, packet.length - HASH_SIZE, HASH_SIZE)

    val bais = ByteArrayInputStream(Arrays.copyOfRange(packet.data, 0,
        packet.length - HASH_SIZE))

    val request = ObjectInputStream(bais).use { it.readObject() as Request }

    request.bredlam?.let { println(it) }
    println(request.cmd)

    val answerStr = if (hash == HashSum.MD5(data)) {
      val (response, changes) = executor.execute(request.cmd, request.bredlam)
      callback(changes)
      response
    } else {
      println("REQUEST IS BROKEN!")
      "REQUEST IS BROKEN :( Try again"
    }

    socket.send(answerStr.toByteArray().let { answer ->
      DatagramPacket(answer, answer.size, packet.address, packet.port)
    })
  }
}
