package xyz.sky731.programming.lab7

import xyz.sky731.programming.lab3.Bredlam
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import javax.swing.*
import javax.swing.border.EmptyBorder
import javax.swing.tree.DefaultMutableTreeNode

class ServerGUI(name: String = "Server GUI") : JFrame(name) {
  val mainTree = BredlamTree(this)
  val colorComboBox = JComboBox<ColorWithName>()
  val posXSpinner = JSpinner()
  val posYSpinner = JSpinner()
  val nameBredlamTextField = JTextField(15)
  val populationTextField = JTextField(15)
  val tabbedPane = JTabbedPane(JTabbedPane.TOP)
  val isEndOfLightCheckbox = JCheckBox("End of light")

  val moneySpinner = JSpinner()
  val nameHumanTextField = JTextField(15)

  init {
    isResizable = true
    minimumSize = Dimension(700, 400)
    defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    isVisible = false

    createMenuBar()

    tabbedPane.addTab("Bredlam", JPanel().apply {
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
            add(Box.createHorizontalStrut(20))
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
            add(nameBredlamTextField)
          }
          nameBredlamTextField.preferredSize = populationTextField.preferredSize
          val populationBox = Box.createHorizontalBox().apply {
            add(JLabel("Population:"))
            add(Box.createHorizontalStrut(12))
            add(populationTextField.apply { isEditable = false })
          }

          val endOfLightBox = Box.createHorizontalBox().apply {
            add(Box.createHorizontalGlue())
            add(isEndOfLightCheckbox)
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
            add(Box.createVerticalStrut(12))
            add(endOfLightBox)
          }

          add(mainBox, constraints)

          constraints.gridy = 3
          add(JPanel().apply {
            contentPane.layout = FlowLayout().apply {
              add(JButton("New").apply { addActionListener {
                // FIXME
              } })
              add(JButton("Edit").apply { addActionListener {
                val selected = mainTree.selection
                when (selected) {
                  is Bredlam -> {
                    selected.name = nameBredlamTextField.text
                    selected.isEndOfLight = isEndOfLightCheckbox.isSelected
                    when (colorComboBox.selectedItem) {
                      is ColorWithName -> selected.flagColor = colorComboBox.selectedItem as ColorWithName
                    }
                    selected.coordinates = Point(posXSpinner.value as Int, posYSpinner.value as Int)
                  }
                }
              } })
              add(JButton("Delete"))
            }
          }, constraints)

        }

      })

    })

    tabbedPane.addTab("Human", JPanel().apply {
      val nameBox = Box.createHorizontalBox().apply {
        add(JLabel("Name:"))
        add(Box.createHorizontalStrut(56))
        nameHumanTextField.preferredSize = Dimension(180, 20)
        add(nameHumanTextField)
      }

      val moneyBox = Box.createHorizontalBox().apply {
        add(JLabel("Money:"))
        add(Box.createHorizontalStrut(50))
        moneySpinner.preferredSize = Dimension(180, 20)
        add(moneySpinner)
      }

      val buttonsBox = Box.createHorizontalBox().apply {
        contentPane.layout = FlowLayout().apply {
          add(JButton("New"))
          add(Box.createHorizontalStrut(5))
          add(JButton("Edit"))
          add(Box.createHorizontalStrut(5))
          add(JButton("Delete"))
        }
      }

      val mainBox = Box.createVerticalBox().apply {
        border = EmptyBorder(6, 6, 6, 6)
        add(nameBox)
        add(Box.createVerticalStrut(12))
        add(moneyBox)
        add(Box.createVerticalStrut(12))
        add(buttonsBox)
      }

      add(mainBox)
    })

    contentPane.layout = GridLayout().apply {
      add(mainTree)
      add(tabbedPane)
    }

    pack()

  }

  private fun createMenuBar() {
    val menubar = JMenuBar()
    val file = JMenu("File")
    file.mnemonic = KeyEvent.VK_F

    val loadCollectionMenuItem = JMenuItem("Load collection").apply {
      mnemonic = KeyEvent.VK_L
      toolTipText = "Load collection from file"
      addActionListener {
        // FIXME
      }
    }

    val saveCollectionMenuItem = JMenuItem("Save collection").apply {
      mnemonic = KeyEvent.VK_S
      toolTipText = "Save collection to file"
      addActionListener {
        // FIXME
      }
    }

    val exitMenuItem = JMenuItem("Exit").apply {
      mnemonic = KeyEvent.VK_E
      toolTipText = "Exit application"
      addActionListener { _: ActionEvent -> System.exit(0) }
    }

    file.apply {
      add(saveCollectionMenuItem)
      add(loadCollectionMenuItem)
      add(exitMenuItem)
    }
    menubar.add(file)

    jMenuBar = menubar
  }

  val updateTree = mainTree::applyChanges
}