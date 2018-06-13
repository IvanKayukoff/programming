package xyz.sky731.programming.lab7

import java.awt.*
import javax.swing.*
import javax.swing.border.EmptyBorder

class ServerGUI(name: String = "Server GUI") : JFrame(name) {
  private val mainTree = BredlamTree()
  val colorComboBox = JComboBox<ColorWithName>()
  val posXSpinner = JSpinner()
  val posYSpinner = JSpinner()
  val nameTextField = JTextField()
  val populationTextField = JTextField()

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

          colorComboBox.apply {
            addItem(ColorWithName(Color.RED, "Red"))
            addItem(ColorWithName(Color.GREEN, "Green"))
            addItem(ColorWithName(Color.BLUE, "Blue"))
            addItem(ColorWithName(Color.PINK, "Pink"))
            addItem(ColorWithName(Color.YELLOW, "Yellow"))
            addItem(ColorWithName(Color.ORANGE, "Orange"))
          }

          val colorLabel = JLabel("Flag color:")
          val colorBox = Box.createHorizontalBox().apply {
            add(colorLabel)
            add(Box.createHorizontalStrut(12))
            add(colorComboBox)
          }

          val positionLabel = JLabel("Position:")
          val positionBox = Box.createHorizontalBox().apply {
            add(positionLabel)
            add(Box.createHorizontalStrut(16))
            add(JLabel("x"))
            add(Box.createHorizontalStrut(8))
            posXSpinner.preferredSize = Dimension(75, 20)
            add(posXSpinner)
            add(Box.createHorizontalStrut(12))
            add(JLabel("y"))
            add(Box.createHorizontalStrut(8))
            posYSpinner.preferredSize = Dimension(75, 20)
            add(posYSpinner)
          }

          val nameBox = Box.createHorizontalBox().apply {
            add(JLabel("Name:"))
            add(Box.createHorizontalStrut(50))
            add(nameTextField)
          }
          nameTextField.preferredSize = populationTextField.preferredSize
          val populationBox = Box.createHorizontalBox().apply {
            add(JLabel("Population:"))
            add(Box.createHorizontalStrut(12))
            add(populationTextField)
          }

          val mainBox = Box.createVerticalBox().apply {
            border = EmptyBorder(6, 6, 6, 6)
            add(colorBox)
            add(Box.createVerticalStrut(12))
            add(positionBox)
            add(Box.createVerticalStrut(12))
            add(nameBox)
            add(Box.createVerticalStrut(12))
            add(populationBox)
          }

          add(mainBox, constraints)

          constraints.gridy = 3
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