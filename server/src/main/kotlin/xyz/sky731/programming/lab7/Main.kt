package xyz.sky731.programming.lab7

import xyz.sky731.programming.lab6.ServerMain
import javax.swing.SwingUtilities
import javax.swing.SwingWorker

fun main(args: Array<String>) = SwingUtilities.invokeLater {
  val gui = ServerGUI()
  val worker = object : SwingWorker<Unit, TreeChange>() {
    override fun doInBackground() {
      ServerMain {
        publish(*it.toTypedArray())
      }.run()
    }

    override fun process(result: MutableList<TreeChange>?) {
      println("k")
      result?.let { gui.updateTree(it) }
    }
  }
  worker.execute()
}
