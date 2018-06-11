package xyz.sky731.programming.lab7

import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel

class ServerGUI(name: String = "Server GUI") : JFrame(name) {
  val mainTree = BredlamTree()
  init {
    isResizable = true
    minimumSize = Dimension(800, 400)
    defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    isVisible = true

    contentPane.layout = GridLayout().apply {
      add(JButton("ClickMe").apply {
        addActionListener {
          this@ServerGUI.dispose()
          //System.exit(0)
        }
      })
      add(mainTree)
      add(JLabel("ClickLeft"))
    }

    pack()

  }

  val updateTree = mainTree::applyChanges
}