package xyz.sky731.programming.lab6

import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab7.TreeChange
import xyz.sky731.programming.lab8.SimpleORM

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.util.concurrent.PriorityBlockingQueue

class ServerMain(val queue: PriorityBlockingQueue<Bredlam>,
                 val orm: SimpleORM,
                 val callback: (List<TreeChange>) -> Unit) : Runnable {

  override fun run() {
    callback(queue.map{ TreeChange(it, isAdded = true) })
    try {
      DatagramSocket(26425).use { socket ->
        while (true) {
          val packet = DatagramPacket(ByteArray(16000), 16000)
          socket.receive(packet)

          Thread(Responder(socket, packet, orm, queue, callback)).start()
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }
}
