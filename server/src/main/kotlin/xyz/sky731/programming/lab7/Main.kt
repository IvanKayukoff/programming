package xyz.sky731.programming.lab7

import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab5.QueueHandler
import xyz.sky731.programming.lab6.ServerMain
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.PriorityBlockingQueue
import javax.swing.SwingUtilities
import javax.swing.SwingWorker
import kotlin.system.exitProcess

fun main(args: Array<String>) = SwingUtilities.invokeLater {
  val fileName = System.getenv("BREDLAM_FILE") ?: run {
    println("Environment variable BREDLAM_FILE not found")
    "queueFile"
  }
  val queue = loadCollectionFromDisk(fileName)

  val gui = ServerGUI(queue, fileName)

  val worker = object : SwingWorker<Unit, TreeChange>() {
    override fun doInBackground() {
      ServerMain(queue, fileName) {
        publish(*it.toTypedArray())
      }.run()
    }

    override fun process(result: MutableList<TreeChange>?) {
      result?.let { gui.updateTree(it) }
    }
  }
  // LoginWindow("Login to Bredlam server", gui)
  gui.isVisible = true // FIXME

  worker.execute()
}

fun loadCollectionFromDisk(fileName: String): PriorityBlockingQueue<Bredlam> {
  val path = Paths.get(fileName)
  try {
    if (!Files.exists(path)) {
      println("File $fileName not found, creating it")
      Files.createFile(path)
      return PriorityBlockingQueue(8, Bredlam.BredlamNameComp())
    }

    println("Found file \"$fileName\", loading..")
    return QueueHandler.loadFromFile(fileName)
  }
  catch (e: IOException) {
    println("Error while trying to load the file")
    exitProcess(0)
  }
}
