package xyz.sky731.programming.lab7

import xyz.sky731.programming.lab6.ClientMain

fun main(args: Array<String>) {
  val client = ClientMain("localhost", 26425)
  val gui = ClientGUI(client, "Client")

}