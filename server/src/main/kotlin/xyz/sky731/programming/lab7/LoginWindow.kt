package xyz.sky731.programming.lab7

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

class LoginWindow(header: String, mainGui: ServerGUI) : JFrame(header) {
  private val passwordField = JPasswordField(15)
  private val loginField = JTextField(15)

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

    var logins: List<List<String>> = emptyList()
    try {
      logins = File("logins.csv").readLines().map { it.split(",") }
    } catch (e: IOException) {
      println("Logins file not found. Any login is wrong")
    }

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

    val submitButton = JButton("OK").apply {
      addActionListener {
        val check = logins.any {
          it[0] == loginField.text && String(passwordField.password).md5() == it[1]
        }
        if (check) {
          mainGui.isVisible = true
          this@LoginWindow.dispose()
        } else {
          title = "Permission denied"
          loginField.background = Color.RED
          passwordField.background = Color.RED
        }

      }
    }
    val cancelButton = JButton("Cancel").apply {
      addActionListener {
        this@LoginWindow.dispose()
        mainGui.dispose()
      }
    }
    val buttonsBox = Box.createHorizontalBox().apply {
      add(Box.createHorizontalGlue())
      add(submitButton)
      add(Box.createHorizontalStrut(12))
      add(cancelButton)
    }

    loginLabel.preferredSize = passLabel.preferredSize

    val mainBox = Box.createVerticalBox().apply {
      border = EmptyBorder(12, 12, 12, 12)
      add(loginBox)
      add(Box.createVerticalStrut(12));
      add(passBox)
      add(Box.createVerticalStrut(17));
      add(buttonsBox)
    }
    contentPane = mainBox
    pack()
    isResizable = false
    isVisible = true

    rootPane.defaultButton = submitButton
  }
}