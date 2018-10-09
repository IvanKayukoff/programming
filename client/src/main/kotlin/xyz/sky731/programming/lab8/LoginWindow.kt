package xyz.sky731.programming.lab8

import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab5.Bredlams
import xyz.sky731.programming.lab5.JsonUser
import xyz.sky731.programming.lab6.BredlamsTransporter
import xyz.sky731.programming.lab6.ClientMain
import xyz.sky731.programming.lab7.ClientGUI
import java.awt.Color
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import javax.swing.*
import javax.swing.border.EmptyBorder

fun String.md5(): String {
  val md = MessageDigest.getInstance("MD5")
  return BigInteger(1, md.digest(toByteArray()))
      .toString(16).padStart(32, '0')
}

class LoginWindow(header: String, mainGui: ClientGUI, client: ClientMain) : JFrame(header) {
  private val passwordField = JPasswordField(18)
  private val loginField = JTextField(18)

  init {
    val mouseAdapter = object : MouseAdapter() {
      override fun mouseClicked(e: MouseEvent?) {
        loginField.background = Color.WHITE
        passwordField.background = Color.WHITE
        this@LoginWindow.title = "Login to Bredlam server"
      }
    }

    loginField.addMouseListener(mouseAdapter)
    passwordField.addMouseListener(mouseAdapter)

    defaultCloseOperation = EXIT_ON_CLOSE
    val loginLabel = JLabel("Login:")
    val loginBox = Box.createHorizontalBox().apply {
      add(loginLabel)
      add(Box.createHorizontalStrut(6))
      add(loginField)
    }

    val passLabel = JLabel("Password:")
    val passBox = Box.createHorizontalBox().apply {
      add(passLabel)
      add(Box.createHorizontalStrut(6))
      add(passwordField)
    }

    /** Sends cmd with bredlam to server and returns received bredlam */
    fun sendCommand(cmd: String, arg: Bredlam): Bredlam? {
      val jsonUser = JsonUser()
      val response = client.sendMessage(cmd, arg)
      val respBredlams = if (response != "") jsonUser.unmarshal(response).getBredlams()
      else Bredlams().apply { bredlam = ArrayList<Bredlam>() }.also {
        JOptionPane.showMessageDialog(null,
            "There is connection problem, maybe server is unavailable")
      }
      return if (respBredlams.bredlam.size > 0) respBredlams.bredlam[0] else null
    }

    val submitButton = JButton("OK").apply {
      addActionListener {
        if (passwordField.password.size < 3 || loginField.text.length < 3
            || passwordField.password.any { it == ' ' } || loginField.text.toCharArray().any { it == ' ' }) {
          JOptionPane.showMessageDialog(null,
              "Length of login and password can not be less than 3 and can not contain spaces")
        } else {
          val response = sendCommand("login", Bredlam(loginField.text, false,
              String(passwordField.password).md5())) ?: return@addActionListener

          if (response.endOfLight) {
            mainGui.isVisible = true
            this@LoginWindow.dispose()
          } else {
            title = "Wrong login or password"
            loginField.background = Color.RED
            passwordField.background = Color.RED
          }
        }
      }
    }

    val cancelButton = JButton("Cancel").apply {
      addActionListener {
        this@LoginWindow.dispose()
        mainGui.dispose()
      }
    }

    val registerButton = JButton("Register").apply {
      addActionListener {
        if (passwordField.password.size < 3 || loginField.text.length < 3
            || passwordField.password.any { it == ' ' } || loginField.text.toCharArray().any { it == ' ' }) {
          JOptionPane.showMessageDialog(null,
              "Length of login and password can not be less than 3 and can not contain spaces")
        } else {
          val response = sendCommand("register", Bredlam(loginField.text, false,
              String(passwordField.password).md5())) ?: return@addActionListener

          if (response.endOfLight) {
            JOptionPane.showMessageDialog(null,
                "Registration success, now you can authorize")
          } else {
            JOptionPane.showMessageDialog(null,
                "Registration failed, this login is already taken")
          }
        }
      }
    }

    val buttonsBox = Box.createHorizontalBox().apply {
      add(Box.createHorizontalGlue())
      add(registerButton)
      add(Box.createHorizontalStrut(12))
      add(submitButton)
      add(Box.createHorizontalStrut(12))
      add(cancelButton)
    }

    loginLabel.preferredSize = passLabel.preferredSize

    val mainBox = Box.createVerticalBox().apply {
      border = EmptyBorder(12, 12, 12, 12)
      add(loginBox)
      add(Box.createVerticalStrut(12))
      add(passBox)
      add(Box.createVerticalStrut(17))
      add(buttonsBox)
    }
    contentPane = mainBox
    pack()
    isResizable = false
    isVisible = true

    rootPane.defaultButton = submitButton
  }
}