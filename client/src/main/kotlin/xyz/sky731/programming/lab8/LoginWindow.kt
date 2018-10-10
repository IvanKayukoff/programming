package xyz.sky731.programming.lab8

import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab5.Bredlams
import xyz.sky731.programming.lab5.JsonUser
import xyz.sky731.programming.lab6.ClientMain
import xyz.sky731.programming.lab7.ClientGUI
import java.awt.Color
import java.awt.event.ItemEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import javax.swing.*
import javax.swing.border.EmptyBorder

fun String.md5(): String {
  val md = MessageDigest.getInstance("MD5")
  return BigInteger(1, md.digest(toByteArray()))
      .toString(16).padStart(32, '0')
}

class LoginWindow(header: String, mainGui: ClientGUI, client: ClientMain) : JFrame(header) {
  private val passwordField = JPasswordField(15)
  private val loginField = JTextField(15)

  private var curLocale = Locale.forLanguageTag("en-AU")

  private val languageComboBox = JComboBox<String>(arrayOf("English(AU)", "Russian", "Hungarian", "Estonian")).apply {
    selectedItem = localeToName(Locale.getDefault())
    curLocale = localeFromName(selectedItem as String)
  }
  private val rb = ResourceBundle.getBundle("Resources", curLocale, UTF8Control())

  private val loginLabel = JLabel(rb.getString("login_label"))
  private val passLabel = JLabel(rb.getString("password_label"))
  private val submitButton = JButton(rb.getString("ok"))
  private val registerButton = JButton(rb.getString("register"))
  private val cancelButton = JButton(rb.getString("cancel"))

  init {
    val mouseAdapter = object : MouseAdapter() {
      override fun mouseClicked(e: MouseEvent?) {
        loginField.background = Color.WHITE
        passwordField.background = Color.WHITE
        setCurTitle()
      }
    }

    loginField.addMouseListener(mouseAdapter)
    passwordField.addMouseListener(mouseAdapter)

    defaultCloseOperation = EXIT_ON_CLOSE
    val loginBox = Box.createHorizontalBox().apply {
      add(loginLabel)
      add(Box.createHorizontalStrut(6))
      add(loginField)
    }

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
      else Bredlams().apply { bredlam = ArrayList<Bredlam>() }.also { msgSrvUnavailable() }

      return if (respBredlams.bredlam.size > 0) respBredlams.bredlam[0] else null
    }

    submitButton.apply {
      addActionListener {
        if (passwordField.password.size < 3 || loginField.text.length < 3
            || passwordField.password.any { it == ' ' } || loginField.text.toCharArray().any { it == ' ' }) {
          msgInvalidInput()
        } else {
          val response = sendCommand("login", Bredlam(loginField.text, false,
              String(passwordField.password).md5())) ?: return@addActionListener

          if (response.endOfLight) {
            mainGui.isVisible = true
            this@LoginWindow.dispose()
          } else {
            setAccessDeniedTitle()
            loginField.background = Color.RED
            passwordField.background = Color.RED
          }
        }
      }
    }

    cancelButton.apply {
      addActionListener {
        this@LoginWindow.dispose()
        mainGui.dispose()
      }
    }

    registerButton.apply {
      addActionListener {
        if (passwordField.password.size < 3 || loginField.text.length < 3
            || passwordField.password.any { it == ' ' } || loginField.text.toCharArray().any { it == ' ' }) {
          msgInvalidInput()
        } else {
          val response = sendCommand("register", Bredlam(loginField.text, false,
              String(passwordField.password).md5())) ?: return@addActionListener

          if (response.endOfLight) {
            msgRegSucceed()
          } else {
            msgRegFailed()
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

    val languagesBox = Box.createHorizontalBox().apply {
      add(languageComboBox.apply {
        addItemListener {
          if (it.stateChange == ItemEvent.SELECTED) {
            when (it.item as String) {
              "English(AU)" -> applyLocale(Locale.forLanguageTag("en-AU"))
              "Russian" -> applyLocale(Locale.forLanguageTag("ru-RU"))
              "Hungarian" -> applyLocale(Locale.forLanguageTag("hu"))
              "Estonian" -> applyLocale(Locale.forLanguageTag("et-EE"))
            }
          }
        }
      })
    }


    val mainBox = Box.createVerticalBox().apply {
      border = EmptyBorder(12, 12, 12, 12)
      add(loginBox)
      add(Box.createVerticalStrut(12))
      add(passBox)
      add(Box.createVerticalStrut(17))
      add(buttonsBox)
      add(Box.createVerticalStrut(17))
      add(languagesBox)
    }

    alignApply()

    contentPane = mainBox
    pack()
    isResizable = false
    isVisible = true

    rootPane.defaultButton = submitButton
  }

  private fun setAccessDeniedTitle() {
    val rb = ResourceBundle.getBundle("Resources", curLocale, UTF8Control())
    title = rb.getString("access_denied")
  }

  private fun setCurTitle() {
    val rb = ResourceBundle.getBundle("Resources", curLocale, UTF8Control())
    title = rb.getString("login_title")
  }

  private fun msgRegFailed() {
    val rb = ResourceBundle.getBundle("Resources", curLocale, UTF8Control())
    JOptionPane.showMessageDialog(null,
        rb.getString("registration_failed"))
  }

  private fun msgRegSucceed() {
    val rb = ResourceBundle.getBundle("Resources", curLocale, UTF8Control())
    JOptionPane.showMessageDialog(null,
        rb.getString("registration_succeed"))
  }

  private fun msgInvalidInput() {
    val rb = ResourceBundle.getBundle("Resources", curLocale, UTF8Control())
    JOptionPane.showMessageDialog(null,
        rb.getString("invalid_input"))
  }

  private fun msgSrvUnavailable() {
    val rb = ResourceBundle.getBundle("Resources", curLocale, UTF8Control())
    JOptionPane.showMessageDialog(null,
        rb.getString("server_unavailable"))
  }

  private fun localeToName(locale: Locale) = when (locale.toString()) {
    "ru_RU" -> "Russian"
    "hu" -> "Hungarian"
    "et_EE" -> "Estonian"
    else -> "English(AU)"
  }

  private fun localeFromName(name: String) = when (name) {
    "Russian" -> Locale.forLanguageTag("ru-RU")
    "Hungarian" -> Locale.forLanguageTag("hu")
    "Estonian" -> Locale.forLanguageTag("et-EE")
    else -> Locale.forLanguageTag("en-AU")
  }

  private fun alignApply() {
    if (loginLabel.preferredSize.width >= passLabel.preferredSize.width) {
      passLabel.preferredSize = loginLabel.preferredSize
    } else {
      loginLabel.preferredSize = passLabel.preferredSize
    }

    if (loginField.preferredSize.width >= passwordField.preferredSize.width) {
      passwordField.preferredSize = loginField.preferredSize
    } else {
      loginField.preferredSize = passwordField.preferredSize
    }
  }

  private fun applyLocale(locale: Locale) {
    curLocale = locale
    val rb = ResourceBundle.getBundle("Resources", locale, UTF8Control())

    title = rb.getString("login_title")
    loginLabel.text = rb.getString("login_label")
    passLabel.text = rb.getString("password_label")
    submitButton.text = rb.getString("ok")
    registerButton.text = rb.getString("register")
    cancelButton.text = rb.getString("cancel")

    alignApply()
    pack()
  }
}