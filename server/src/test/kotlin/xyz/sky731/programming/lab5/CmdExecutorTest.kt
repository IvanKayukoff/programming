package xyz.sky731.programming.lab5

import org.junit.Test
import xyz.sky731.programming.lab7.loadCollectionFromDisk

class CmdExecutorTest {

  @Test
  fun getCollectionTest() {
    val filename = "queueFile"
    val queue = loadCollectionFromDisk(filename)
    val cmdExecutor = CmdExecutor(queue, filename)
    val (response, changes) = cmdExecutor.execute("get_collection", null)
    println(response)

    val jsonUser = JsonUser()
    val bredlams = jsonUser.unmarshal(response)
    bredlams.bredlam.forEach { bredlam ->
      println(bredlam)
      bredlam.people.forEach { man ->
        println("\t" + man)
      }
    }
  }
}