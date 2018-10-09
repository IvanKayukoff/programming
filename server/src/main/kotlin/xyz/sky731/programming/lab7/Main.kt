package xyz.sky731.programming.lab7

import org.postgresql.util.PSQLException
import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab5.QueueHandler
import xyz.sky731.programming.lab6.ServerMain
import xyz.sky731.programming.lab8.SimpleORM
import java.io.IOException
import java.lang.RuntimeException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.PriorityBlockingQueue
import javax.swing.SwingUtilities
import javax.swing.SwingWorker
import kotlin.system.exitProcess

fun main(args: Array<String>) = SwingUtilities.invokeLater {
  val orm: SimpleORM

  val username = System.getenv("POSTGRES_USER")
      ?: throw RuntimeException("POSTGRES_USER environment variable must be installed")
  val password = System.getenv("POSTGRES_PASSWORD")
      ?: throw RuntimeException("POSTGRES_PASSWORD environment variable must be installed")
  val dbUrl = System.getenv("POSTGRES_URL")
      ?: throw RuntimeException("POSTGRES_URL environment variable must be installed")

  try {
    orm = SimpleORM(dbUrl, username, password)
  } catch (e: Exception) {
    println("Can not connect to database, so exit..")
    exitProcess(1)
  }

  try {
    orm.createTable<Bredlam>()
  } catch (e: PSQLException) {
    println("Table with bredlams found")
  }

  val queue = orm.selectAll<Bredlam>()

  val gui = ServerGUI(queue, orm)

  val worker = object : SwingWorker<Unit, TreeChange>() {
    override fun doInBackground() {
      ServerMain(queue, orm) {
        publish(*it.toTypedArray())
      }.run()
    }

    override fun process(result: MutableList<TreeChange>?) {
      result?.let { gui.updateTree(it) }
    }
  }
  LoginWindow("Login to Bredlam server", gui)
  gui.isVisible = false

  worker.execute()
}

fun loadCollectionFromDisk(fileName: String): PriorityBlockingQueue<Bredlam> {
  val path = Paths.get(fileName)
  try {
    if (!Files.exists(path)) {
      println("File $fileName not found, creating it")
      Files.createFile(path)
      return PriorityBlockingQueue(8, Bredlam.Companion.BredlamNameComp())
    }

    println("Found file \"$fileName\", loading..")
    return QueueHandler.loadFromFile(fileName)
  }
  catch (e: IOException) {
    println("Error while trying to load the file")
    exitProcess(0)
  }
}
