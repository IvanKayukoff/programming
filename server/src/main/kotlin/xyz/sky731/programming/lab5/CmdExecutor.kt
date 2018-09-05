package xyz.sky731.programming.lab5

import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab7.TreeChange

import java.util.*
import kotlin.collections.ArrayList

class CmdExecutor(private val queue: Queue<Bredlam>, private val filename: String) {
  private fun added(response: String, vararg elements: Bredlam) =
      Pair(response, elements.map { TreeChange(it, isAdded = true) })

  private fun unchanged(response: String) = Pair(response, emptyList<TreeChange>())
  private fun removed(response: String, vararg elements: Bredlam) =
      Pair(response, elements.map { TreeChange(it, isAdded = false) })

  fun execute(cmd: String, arg: Bredlam?): Pair<String, List<TreeChange>> = when (cmd) {
    "info" -> info()
    "load" -> readQueue()
    "remove_last" -> removeLast()
    "remove_first" -> removeFirst()
    "save" -> save()
    "remove" -> remove(arg)
    "remove_lower" -> removeLower(arg)
    "add" -> add(arg)
    "get_collection" -> getCollection()
    else -> unchanged("Unknown command")
  }

  /** Returns json-formatted bredlams */
  private fun getCollection(): Pair<String, List<TreeChange>> = with(queue) {
    val bredlams = Bredlams()
    bredlams.bredlam = this.toCollection(ArrayList<Bredlam>())
    val jsonUser = JsonUser()
    unchanged(jsonUser.marshal(bredlams))
  }

  fun add(bredlam: Bredlam?) =
      bredlam?.let {
        queue.add(it)
        added("Added bredlam to queue: $bredlam", it)
      } ?: unchanged("Wrong json code")

  private fun info(): Pair<String, List<TreeChange>> =
      unchanged("""
      Type collection: ${queue.javaClass}
      ${if (queue.size > 0) "Type elements in collection: ${queue.peek().javaClass}"
      else "Unknown elements type"}
      Size collection: ${queue.size}
    """.trimIndent())

  private fun readQueue(): Pair<String, List<TreeChange>> {
    val removed = queue.toList()
    val added = QueueHandler.loadFromFile(filename)
    queue.clear()
    queue.addAll(added)
    return Pair("Read collection from file", removed.map { TreeChange(it, isAdded = false) }
        + added.map { TreeChange(it, isAdded = true) })
  }

  private fun removeLast(): Pair<String, List<TreeChange>> = with(queue) {
    lastOrNull()?.let { removed ->
      val bredlams = take(queue.size - 1)
      clear()
      addAll(bredlams)
      removed("Deleted last bredlam $removed", removed)
    } ?: unchanged("Collection is already empty")
  }

  private fun save(): Pair<String, List<TreeChange>> {
    QueueHandler.writeToFile(filename, queue)
    return unchanged("Saved collection to file")
  }

  private fun removeFirst(): Pair<String, List<TreeChange>> {
    val bredlam = queue.poll()
    return bredlam?.let {
      removed("Deleted first element $bredlam", bredlam)
    } ?: unchanged("Collection is already empty")
  }

  private fun remove(bredlam: Bredlam?) =
      bredlam?.let {
        if (queue.remove(bredlam)) removed("Deleted $bredlam", bredlam)
        else unchanged("$bredlam doesn't exist in the collection")
      } ?: unchanged("Wrong json code")

  private fun removeLower(bredlam: Bredlam?): Pair<String, List<TreeChange>> = bredlam?.let {
    val deleted = queue.filter { it < bredlam }
    val isDeleted = queue.removeAll(deleted)
    if (isDeleted) removed("Deleted bredlams less than $bredlam", *deleted.toTypedArray())
    else unchanged("Nothing deleted")
  } ?: unchanged("Wrong json code")
}
