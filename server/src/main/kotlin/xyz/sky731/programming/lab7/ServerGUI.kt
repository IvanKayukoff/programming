package xyz.sky731.programming.lab7

import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab5.CmdExecutor
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.util.concurrent.PriorityBlockingQueue
import javax.swing.*
import javax.swing.border.EmptyBorder
import kotlin.system.exitProcess

class ServerGUI(val queue: PriorityBlockingQueue<Bredlam>,
                val fileName: String,
                name: String = "Server GUI") : JFrame(name) {
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

              fun peekSelected() = Bredlam(nameBredlamTextField.text).apply {
                coordinates = Point(posXSpinner.value as Int, posYSpinner.value as Int)
                isEndOfLight = isEndOfLightCheckbox.isSelected
                flagColor = if (colorComboBox.selectedItem is ColorWithName)
                  colorComboBox.selectedItem as ColorWithName
                else null
              }

              val executor = CmdExecutor(queue, fileName) // FIXME need to test with reloading from file

              fun executeCmdWithPeeked(cmd: String) {
                val bredlam = peekSelected()
                val (response, changes) = executor.execute(cmd, bredlam)
                updateTree(changes)
                println(response)
              }

              add(JButton("New").apply {
                addActionListener {
                  executeCmdWithPeeked("add")
                }
              })
              add(JButton("Edit").apply {
                addActionListener { // FIXME JTree does not update instantly
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
                }
              })
              add(JButton("Delete").apply {
                addActionListener {
                  val selected = mainTree.selection
                  when (selected) {
                    is Bredlam -> {
                      val (response, changes) = executor.execute("remove", selected)
                      updateTree(changes)
                      println(response)
                    }
                  }
                }
              })
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
          add(JButton("New")) // TODO: add implementation of action listener
          add(Box.createHorizontalStrut(5))
          add(JButton("Edit")) // TODO: add implementation of action listener
          add(Box.createHorizontalStrut(5))
          add(JButton("Delete")) // TODO: add implementation of action listener
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
        //TODO: add implementation of action listener
      }
    }

    val saveCollectionMenuItem = JMenuItem("Save collection").apply {
      mnemonic = KeyEvent.VK_S
      toolTipText = "Save collection to file"
      addActionListener {
        // TODO: add implementation of action listener
      }
    }

    val exitMenuItem = JMenuItem("Exit").apply {
      mnemonic = KeyEvent.VK_E
      toolTipText = "Exit application"
      addActionListener { _: ActionEvent -> exitProcess(0) }
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