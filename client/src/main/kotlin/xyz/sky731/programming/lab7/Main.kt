package xyz.sky731.programming.lab7

import xyz.sky731.programming.lab6.ClientMain
import xyz.sky731.programming.lab8.LoginWindow
import java.lang.ClassCastException
import java.lang.RuntimeException

fun main(args: Array<String>) {
  val serverIp = System.getenv("SERVER_IP")
      ?: throw RuntimeException("SERVER_IP environment variable must be set")
  val serverPort = System.getenv("SERVER_PORT")
      ?: throw RuntimeException("SERVER_POST environment variable must be set")

  val port: Int
  try {
    port = Integer.parseInt(serverPort)
  } catch (e: ClassCastException) { throw RuntimeException("SERVER_PORT must have valid value") }

  val client = ClientMain(serverIp, port)
  val mainGUI = ClientGUI(client, "Client")
  val loginWindow = LoginWindow("Login to Bredlam viewer", mainGUI, client)
  mainGUI.isVisible = false
}