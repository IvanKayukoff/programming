package xyz.sky731.programming.lab5

import sun.reflect.generics.tree.Tree
import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab6.BredlamsTransporter
import xyz.sky731.programming.lab7.TreeChange
import xyz.sky731.programming.lab8.Account
import xyz.sky731.programming.lab8.SimpleORM
import java.sql.SQLException

import java.util.*
import kotlin.collections.ArrayList

class CmdExecutor(private val queue: Queue<Bredlam>, private val orm: SimpleORM) {

  companion object {
    private val deletedBredlams = ArrayList<Bredlam>()
  }

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
    "login" -> login(arg)
    "register" -> register(arg)
    else -> unchanged("Unknown command")
  }

  /** Really dirty hack, but there is no ways to implement authorization mechanism
   *  without significant code rewriting.
   *  So, info contains login and password :) Where:
   *  info.name acts as login
   *  info.flagColor acts as password which appears as MD5 hash-sum
   *  Result is info.endOfLight, true if authorization success, false - otherwise
   **/
  private fun login(info: Bredlam?) : Pair<String, List<TreeChange>> {
    val accounts = orm.selectAll<Account>()
    if (info == null) return unchanged("The account's data are not represented")

    info.endOfLight = accounts.find { acc -> acc.login == info.name && acc.password == info.flagColor } != null

    val bredlams = Bredlams()
    bredlams.bredlam = ArrayList<Bredlam>().apply { add(info) }
    val transporter = BredlamsTransporter().apply { setBredlams(bredlams) }
    val jsonUser = JsonUser()
    return unchanged(jsonUser.marshal(transporter))
  }

  /** One more cheat here, as in the previous function: [login], info contains login and password, where:
   *  info.name is login
   *  info.flagColor is password
   *  Result is info.endOfLight, true if registration success, false - otherwise
   **/
  private fun register(info: Bredlam?) : Pair<String, List<TreeChange>> {
    val accounts = orm.selectAll<Account>()
    if (info == null) return unchanged("The account's data are not represented")

    info.endOfLight = accounts.find { acc -> acc.login == info.name } == null
    val newAccount = Account(info.name, info.flagColor)
    orm.insert(newAccount)

    val bredlams = Bredlams()
    bredlams.bredlam = ArrayList<Bredlam>().apply { add(info) }
    val transporter = BredlamsTransporter().apply { setBredlams(bredlams) }
    val jsonUser = JsonUser()
    return unchanged(jsonUser.marshal(transporter))
  }

  /** Returns json-formatted bredlams */
  private fun getCollection(): Pair<String, List<TreeChange>> = with(queue) {
    val bredlams = Bredlams()
    bredlams.bredlam = this.toCollection(ArrayList<Bredlam>())
    val transporter = BredlamsTransporter().apply { setBredlams(bredlams) }
    val jsonUser = JsonUser()
    unchanged(jsonUser.marshal(transporter))
  }

  private fun add(bredlam: Bredlam?) =
      bredlam?.let {
        queue.add(it)
        added("Added bredlam to queue: $bredlam", it)
      } ?: unchanged("Wrong json code")

  private fun info(): Pair<String, List<TreeChange>> {
    var result = "Type collection: ${queue.javaClass}\n"
    if (queue.size > 0) result += "Type elements in collection: ${queue.peek().javaClass}\n"
    else result += "Unknown elements type\n"
    result += "Size collection: ${queue.size}"
    return unchanged(result)
  }

  private fun readQueue(): Pair<String, List<TreeChange>> {
    val removed = queue.toList()
    val added = orm.selectAll<Bredlam>()
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

    deletedBredlams.forEach {
      it.id?.let { orm.deleteById<Bredlam>(it) }
    }
    deletedBredlams.clear()

    // Updates bredlams if exists and otherwise inserts him
    for (bredlam in queue) {
      if (bredlam.id != null) {
        orm.update(bredlam)
        for (human in bredlam.people) {
          if (human.id != null) {
            orm.update(human)
          } else {
            orm.deleteById<Bredlam>(bredlam.id as Int)
            orm.insert(bredlam)
          }
        }
      } else {
        orm.insert(bredlam)
      }
    }
    // We need to update bredlam's id, so call readQueue
    readQueue()
    return unchanged("Saved collection to database")
  }

  private fun removeFirst(): Pair<String, List<TreeChange>> {
    val bredlam = queue.poll()
    return bredlam?.let {
      removed("Deleted first element $bredlam", bredlam)
    } ?: unchanged("Collection is already empty")
  }

  private fun remove(bredlam: Bredlam?) =
      bredlam?.let {
        deletedBredlams.add(it)
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
