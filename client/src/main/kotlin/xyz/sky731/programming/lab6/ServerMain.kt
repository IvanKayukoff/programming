package xyz.sky731.programming.lab6

import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab5.QueueHandler
import xyz.sky731.programming.lab7.TreeChange

import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.PriorityBlockingQueue
import kotlin.system.exitProcess

class ServerMain(val callback: (List<TreeChange>) -> Unit) : Runnable {
  private val fileName = System.getenv("BREDLAM_FILE") ?: run {
    println("Environment variable BREDLAM_FILE not found")
    "queueFile"
  }
  private val queue = loadCollectionFromDisk()

  override fun run() {
    try {
      DatagramSocket(26425).use { socket ->
        while (true) {
          val packet = DatagramPacket(ByteArray(16000), 16000)
          socket.receive(packet)

          Thread(Responder(socket, packet, fileName, queue, callback)).start()
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun loadCollectionFromDisk(): PriorityBlockingQueue<Bredlam> {
    val path = Paths.get(fileName)
    try {
      if (!Files.exists(path)) {
        println("File $fileName not found, creating it")
        Files.createFile(path)
        return PriorityBlockingQueue(8, Bredlam.BredlamNameComp())
      }

      println("Found file \"$fileName\", loading..")
      val queue = QueueHandler.loadFromFile(fileName)
      callback(queue.map{ TreeChange(it, isAdded = true) })
      return queue
    }
    catch (e: IOException) {
      println("Error while trying to load the file")
      exitProcess(0)
    }
  }
}
