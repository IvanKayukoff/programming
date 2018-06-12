package xyz.sky731.programming.lab7

import java.awt.*
import java.awt.event.ActionListener
import javax.swing.*

class ServerGUI(name: String = "Server GUI") : JFrame(name) {
  val mainTree = BredlamTree()
  init {
    isResizable = true
    minimumSize = Dimension(700, 400)
    defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    isVisible = false

    contentPane.layout = GridLayout().apply {
      add(mainTree)
      add(object : JComponent() {
        init {
          layout = GridBagLayout()
          val constraints = GridBagConstraints().apply {
            fill = GridBagConstraints.SOUTH
            gridx = 0
            gridy = 0
            ipadx = 10
            ipady = 10
          }

          add(JLabel("Name: "), constraints)
          constraints.gridy = 1
          add(JLabel("Popularity: "), constraints)

          constraints.gridy = 2
          add(JPanel().apply {
            contentPane.layout = FlowLayout().apply {
              add(JButton("New"))
              add(JButton("Edit"))
              add(JButton("Delete"))
            }
          }, constraints)
        }

      })
    }

    pack()

  }

  val updateTree = mainTree::applyChanges
}