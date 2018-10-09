package xyz.sky731.programming.lab7

import cartesian.coordinate.CCPolygon
import cartesian.coordinate.CCSystem
import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab5.Bredlams
import xyz.sky731.programming.lab5.JsonUser
import xyz.sky731.programming.lab6.ClientMain
import xyz.sky731.programming.lab8.UTF8Control
import java.awt.*
import javax.swing.*
import java.awt.event.ActionListener
import java.awt.event.ItemEvent
import java.awt.event.MouseEvent
import java.awt.geom.Point2D
import javax.swing.ImageIcon
import javax.swing.JLabel
import java.io.File
import javax.imageio.ImageIO
import java.util.*
import javax.imageio.IIOException
import javax.swing.Timer
import kotlin.system.exitProcess
import java.util.Locale
import java.time.format.FormatStyle
import java.time.format.DateTimeFormatter



class ClientGUI(private val client: ClientMain, nameFrame: String,
                private var curLocale: Locale = Locale.forLanguageTag("en-AU"),
                private var initialStart: Boolean = true) : JFrame(nameFrame) {

  private fun drawCircle(plot: CCSystem, x: Double, y: Double, r: Double, color: Color, fillingColor: Color) {
    val coordinates = Coordinates.caclCircleCoords(x, y, r)
    val polygon = CCPolygon(coordinates.x, coordinates.y, color,
        fillingColor, BasicStroke(1f))
    plot.add(polygon)
  }

  private val languageComboBox = JComboBox<String>(arrayOf("English(AU)", "Russian", "Hungarian", "Estonian")).apply {
    if (initialStart) {
      selectedItem = localeToName(Locale.getDefault())
      curLocale = localeFromName(selectedItem as String)
      initialStart = false
    } else {
      selectedItem = localeToName(curLocale)
    }
  }
  private val rb = ResourceBundle.getBundle("Resources", curLocale, UTF8Control())

  private val colorRedCheckbox = JCheckBox(rb.getString("color_red"))
  private val colorBlueCheckbox = JCheckBox(rb.getString("color_blue"))
  private val colorPinkCheckbox = JCheckBox(rb.getString("color_pink"))
  private val colorOrangeCheckbox = JCheckBox(rb.getString("color_orange"))
  private val colorYellowCheckbox = JCheckBox(rb.getString("color_yellow"))
  private val colorGreenCheckbox = JCheckBox(rb.getString("color_green"))

  private val fromXSpinner = JSpinner()
  private val toXSpinner = JSpinner()
  private val fromYSpinner = JSpinner()
  private val toYSpinner = JSpinner()

  private val nameTextField = JTextField()
  private val endOfLightCheckbox = JCheckBox(rb.getString("end_of_light"))

  private val fromPopulationSpinner = JSpinner()
  private val toPopulationSpinner = JSpinner()

  private val filtersCheckBox = JCheckBox(rb.getString("filters_enabled"))

  private var started = false
  private val startButton = JButton(rb.getString("start"))
  private val stopButton = JButton(rb.getString("stop"))

  private val colorsLabel = JLabel(rb.getString("colors"))
  private val coordinatesLabel = JLabel(rb.getString("coordinates"))
  private val xFromLabel = JLabel(rb.getString("x_from"))

  private val toLabel = JLabel(rb.getString("to"))
  private val toLabel1 = JLabel(rb.getString("to"))
  private val toLabel2 = JLabel(rb.getString("to"))
  private val yFromLabel = JLabel(rb.getString("y_from"))
  private val nameStartsWithLabel = JLabel(rb.getString("name_starts_with"))
  private val populationFromLabel = JLabel(rb.getString("population_from"))
  private val reloadCollectionLabel = JLabel(rb.getString("reload_collection"))
  private val refreshButton = JButton(rb.getString("refresh"))
  private val languageLabel = JLabel(rb.getString("language"))


  private var timer: Timer? = null

  init {
    defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    isResizable = false
    preferredSize = Dimension(800, 600)
    title = rb.getString("main_title")

    val ownPanel = JPanel().apply {
      preferredSize = Dimension(800, 600)
      layout = GridBagLayout()

      val c = GridBagConstraints().apply {
        gridx = GridBagConstraints.RELATIVE
        gridy = GridBagConstraints.RELATIVE
        anchor = GridBagConstraints.NORTHWEST
        insets = Insets(0, 0, 0, 0)
        fill = GridBagConstraints.HORIZONTAL
      }

      try {
        val myPicture = ImageIO.read(File("logo320x80.png"))
        val picLabel = JLabel(ImageIcon(myPicture)).apply {
          preferredSize = Dimension(320, 80)
        }
        add(picLabel, c)
      } catch (e: IIOException) {
        println("Oops, the program requires logo320x80.png at the same directory")
        exitProcess(1)
      }


      c.fill = GridBagConstraints.NONE
      c.gridy = 1

      val jsonUser = JsonUser()
      val response = client.sendMessage("get_collection", null)
      val bredlams = if (response != "") jsonUser.unmarshal(response).getBredlams()
      else Bredlams().apply { bredlam = ArrayList<Bredlam>() }

      val maxPopulation = bredlams.bredlam.run {
        var res = 0
        this.forEach {
          res = if (it.people.size > res) it.people.size else res
        }
        res
      }

      /** Plot normalization */
      var minX = bredlams.bredlam.run {
        var res = if (this.size > 0) this[0].x - Math.sqrt(this[0].people.size.toDouble()) else -10.0
        this.forEach {
          res = if (it.x - Math.sqrt(it.people.size.toDouble()) < res) it.x -
              Math.sqrt(it.people.size.toDouble()) else res
        }
        res
      }
      var minY = bredlams.bredlam.run {
        var res = if (this.size > 0) this[0].y - Math.sqrt(this[0].people.size.toDouble()) else -10.0
        this.forEach {
          res = if (it.y - Math.sqrt(it.people.size.toDouble()) < res) it.y -
              Math.sqrt(it.people.size.toDouble()) else res
        }
        res
      }
      var maxX = bredlams.bredlam.run {
        var res = if (this.size > 0) this[0].x + Math.sqrt(this[0].people.size.toDouble()) else 10.0
        this.forEach {
          res = if (it.x + Math.sqrt(it.people.size.toDouble()) > res) it.x +
              Math.sqrt(it.people.size.toDouble()) else res
        }
        res
      }
      var maxY = bredlams.bredlam.run {
        var res = if (this.size > 0) this[0].y + Math.sqrt(this[0].people.size.toDouble()) else 10.0
        this.forEach {
          res = if (it.y + Math.sqrt(it.people.size.toDouble()) > res) it.y +
              Math.sqrt(it.people.size.toDouble()) else res
        }
        res
      }

      val diffX = maxX - minX
      val diffY = maxY - minY

      val avgX = (maxX + minX) / 2
      val avgY = (maxY + minY) / 2

      if (diffX > diffY) {
        maxY = avgY + diffX / 2
        minY = avgY - diffX / 2
      } else {
        maxX = avgX + diffY / 2
        minX = avgX - diffY / 2
      }

      /** Graph panel */
      val graph = object : CCSystem(minX, minY, maxX, maxY) {
        init {
          preferredSize = Dimension(460, 460)
          toolTipText = "help me"
          // ToolTipManager.sharedInstance().registerComponent(this@apply)
        }

        override fun getToolTipText(e: MouseEvent): String? {
          val coordinates = CCSystem::class.java.getDeclaredMethod("translate", Point::class.java).apply {
            isAccessible = true
          }.invoke(this, Point(e.x, this.height - e.y)) as Point2D.Double

          val mx = coordinates.x
          val my = coordinates.y

          bredlams.bredlam.forEach {
            if (Math.sqrt(((mx - it.x.toDouble()) * (mx - it.x.toDouble()) + (my - it.y.toDouble()) * (my - it.y.toDouble()))) <=
                if (it.people.size > 0) Math.sqrt(it.people.size.toDouble()) else 1.0) {
              val pattern = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(curLocale)
              return "${it.name}  ${it.creation.format(pattern)}"
            }
          }

          return null
        }

      }

      bredlams.bredlam.forEach {
        drawCircle(graph, it.x.toDouble(), it.y.toDouble(),
            if (it.people.size > 0) Math.sqrt(it.people.size.toDouble()) else 1.0,
            ColorWithName(it.flagColor).color, if (it.people.size == 0) Color.WHITE else ColorWithName(it.flagColor).color)
      }

      add(graph, c)


      /** Panel with filters */
      add(JPanel().apply {
        border = BorderFactory.createLineBorder(Color.BLUE)
        preferredSize = Dimension(320, 580)
        layout = GridBagLayout()
        val constraints = GridBagConstraints().apply {
          gridx = GridBagConstraints.RELATIVE
          gridy = GridBagConstraints.RELATIVE
          gridwidth = 1
          gridheight = 1
          anchor = GridBagConstraints.NORTHWEST
          fill = GridBagConstraints.HORIZONTAL
          insets = Insets(10, 10, 0, 0)

        }
        add(filtersCheckBox, constraints)

        // constraints.ipadx = 32 // stretches component
        // constraints.gridwidth = GridBagConstraints.REMAINDER // takes the remaining space
        constraints.apply {
          gridy = 2
          insets = Insets(5, 15, 0, 0)
        }
        add(colorsLabel, constraints)

        // pushes all components to the left
        constraints.weightx = 1.0
        constraints.gridx = 5
        add(JLabel(), constraints)
        constraints.weightx = 0.0
        constraints.gridx = 0

        constraints.insets = Insets(5, 60, 0, 0)

        constraints.gridy = 3
        add(colorRedCheckbox.apply { isSelected = true }, constraints)

        constraints.gridy = 4
        add(colorGreenCheckbox.apply { isSelected = true }, constraints)

        constraints.gridy = 5
        add(colorBlueCheckbox.apply { isSelected = true }, constraints)

        constraints.gridy = 6
        add(colorPinkCheckbox.apply { isSelected = true }, constraints)

        constraints.gridy = 7
        add(colorOrangeCheckbox.apply { isSelected = true }, constraints)

        constraints.gridy = 8
        add(colorYellowCheckbox.apply { isSelected = true }, constraints)

        constraints.insets = Insets(5, 15, 0, 0)
        constraints.gridy = 9
        add(coordinatesLabel, constraints)
        constraints.fill = GridBagConstraints.NONE
        constraints.insets = Insets(5, 15, 0, 0)

        constraints.gridy = 10
        add(JPanel().apply {
          layout = FlowLayout()
          add(xFromLabel, constraints)

          constraints.insets = Insets(5, 15, 0, 0)

          add(fromXSpinner.apply {
            model = SpinnerNumberModel(minX, minX, maxX, 1)
            preferredSize = Dimension(50, 20)
          })
          add(toLabel)
          add(toXSpinner.apply {
            model = SpinnerNumberModel(maxX, minX, maxX, 1)
            preferredSize = Dimension(50, 20)
          })

        }, constraints)

        constraints.gridy = 11
        add(JPanel().apply {
          layout = FlowLayout()

          add(yFromLabel)

          constraints.insets = Insets(5, 15, 0, 0)

          add(fromYSpinner.apply {
            model = SpinnerNumberModel(minY, minY, maxY, 1)
            preferredSize = Dimension(50, 20)
          })
          add(toLabel1)
          add(toYSpinner.apply {
            model = SpinnerNumberModel(maxY, minY, maxY, 1)
            preferredSize = Dimension(50, 20)
          })

        }, constraints)

        constraints.gridy = 12
        add(JPanel().apply {
          layout = FlowLayout()

          add(nameStartsWithLabel)
          add(nameTextField.apply {
            preferredSize = Dimension(130, 20)
          })
        }, constraints)

        constraints.gridy = 13
        add(endOfLightCheckbox, constraints)

        constraints.gridy = 14
        add(JPanel().apply {
          layout = FlowLayout()

          add(populationFromLabel)
          add(fromPopulationSpinner.apply {
            model = SpinnerNumberModel(0, 0, maxPopulation, 1)
            preferredSize = Dimension(50, 20)
          })

          add(toLabel2)
          add(toPopulationSpinner.apply {
            model = SpinnerNumberModel(maxPopulation, 0, maxPopulation, 1)
            preferredSize = Dimension(50, 20)
          })
        }, constraints)

        constraints.gridy = 15
        add(JLabel(), constraints)

        constraints.gridy = 16
        constraints.fill = GridBagConstraints.HORIZONTAL
        add(JPanel().apply {
          layout = FlowLayout()

          add(startButton.apply {
            addActionListener {
              isEnabled = false
              stopButton.isEnabled = true
              /** Selection by filters */
              var filtered = bredlams.bredlam
              if (filtersCheckBox.isSelected) {
                filtered = filtered.filter {
                  colorBlueCheckbox.isSelected && ColorWithName(it.flagColor).color == Color.BLUE ||
                      colorGreenCheckbox.isSelected && ColorWithName(it.flagColor).color == Color.GREEN ||
                      colorOrangeCheckbox.isSelected && ColorWithName(it.flagColor).color == Color.ORANGE ||
                      colorPinkCheckbox.isSelected && ColorWithName(it.flagColor).color == Color.PINK ||
                      colorRedCheckbox.isSelected && ColorWithName(it.flagColor).color == Color.RED ||
                      colorYellowCheckbox.isSelected && ColorWithName(it.flagColor).color == Color.YELLOW
                }

                filtered = filtered.filter {
                  it.x >= fromXSpinner.value as Double &&
                      it.x <= toXSpinner.value as Double &&
                      it.y >= fromYSpinner.value as Double &&
                      it.y <= toXSpinner.value as Double
                }

                filtered = filtered.filter { endOfLightCheckbox.isSelected == it.endOfLight }

                filtered = filtered.filter {
                  it.people.size >= fromPopulationSpinner.value as Int &&
                      it.people.size <= toPopulationSpinner.value as Int
                }

                filtered = filtered.filter {
                  it.name.toLowerCase().startsWith(nameTextField.text.toLowerCase())
                }
                filtered.forEach {
                  println("Picked up: $it")
                }
              }

              var tickCounter = 0

              val timerEvent = ActionListener { _ ->
                graph.clear()
                (bredlams.bredlam - filtered).forEach {
                  drawCircle(graph, it.x.toDouble(), it.y.toDouble(),
                      if (it.people.size > 0) Math.sqrt(it.people.size.toDouble()) else 1.0,
                      ColorWithName(it.flagColor).color, if (it.people.size == 0) Color.WHITE else ColorWithName(it.flagColor).color)
                }
                filtered.forEach {
                  val color = ColorWithName(it.flagColor).color

                  var fillingColor = Color.white

                  if (it.people.size == 0) {
                    fillingColor = if (tickCounter % 140 < 60)
                      Color(255, 255, 255, (255 - (tickCounter % 140) * 4.25).toInt())
                    else Color(255, 255, 255, (0 + (tickCounter % 140 - 60) * 3.1875).toInt())
                  } else {
                    fillingColor = if (tickCounter % 140 < 60)
                      Color(color.red, color.green, color.blue, (255 - (tickCounter % 140) * 4.25).toInt())
                    else Color(color.red, color.green, color.blue, (0 + (tickCounter % 140 - 60) * 3.1875).toInt())
                  }


                  drawCircle(graph, it.x.toDouble(), it.y.toDouble(),
                      if (it.people.size > 0) Math.sqrt(it.people.size.toDouble()) else 1.0,
                      if (tickCounter % 140 < 60)
                        Color(color.red, color.green, color.blue, (255 - (tickCounter % 140) * 4.25).toInt())
                      else Color(color.red, color.green, color.blue, (0 + (tickCounter % 140 - 60) * 3.1875).toInt()),
                      fillingColor)
                }
                tickCounter++

              }

              timer = Timer(50, timerEvent).apply { start() }

            }
          })

          add(stopButton.apply {
            addActionListener {
              isEnabled = false
              startButton.isEnabled = true
              timer?.stop()

              graph.clear()
              bredlams.bredlam.forEach {
                drawCircle(graph, it.x.toDouble(), it.y.toDouble(),
                    if (it.people.size > 0) Math.sqrt(it.people.size.toDouble()) else 1.0,
                    ColorWithName(it.flagColor).color, if (it.people.size == 0) Color.WHITE else ColorWithName(it.flagColor).color)
              }

            }
          })
        }, constraints)

        constraints.gridy = 17
        add(JLabel(" "), constraints)

        constraints.gridy = 18
        add(JPanel().apply {
          layout = FlowLayout()

          add(reloadCollectionLabel)

          add(refreshButton.apply {
            addActionListener {
              val gui = ClientGUI(client, nameFrame, curLocale, initialStart)
              this@ClientGUI.isVisible = false
              gui.isVisible = true
              this@ClientGUI.dispose()
            }
          })
        }, constraints)

        constraints.gridy = 19
        add(JPanel().apply {
          layout = FlowLayout()

          add(languageLabel)

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

        }, constraints)

        c.gridx = 1
        c.gridy = GridBagConstraints.RELATIVE
        c.gridheight = 2
      }, c)

    }
    contentPane.add(ownPanel)
    pack()
    isVisible = false
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

  private fun applyLocale(locale: Locale) {
    curLocale = locale
    val rb = ResourceBundle.getBundle("Resources", locale, UTF8Control())
    title = rb.getString("main_title")

    coordinatesLabel.text = rb.getString("coordinates")
    colorsLabel.text = rb.getString("colors")
    colorRedCheckbox.text = rb.getString("color_red")
    colorBlueCheckbox.text = rb.getString("color_blue")
    colorPinkCheckbox.text = rb.getString("color_pink")
    colorOrangeCheckbox.text = rb.getString("color_orange")
    colorYellowCheckbox.text = rb.getString("color_yellow")
    colorGreenCheckbox.text = rb.getString("color_green")

    endOfLightCheckbox.text = rb.getString("end_of_light")
    filtersCheckBox.text = rb.getString("filters_enabled")
    startButton.text = rb.getString("start")
    stopButton.text = rb.getString("stop")
    xFromLabel.text = rb.getString("x_from")
    toLabel.text = rb.getString("to")
    toLabel1.text = rb.getString("to")
    toLabel2.text = rb.getString("to")
    yFromLabel.text = rb.getString("y_from")
    nameStartsWithLabel.text = rb.getString("name_starts_with")
    populationFromLabel.text = rb.getString("population_from")
    reloadCollectionLabel.text = rb.getString("reload_collection")
    refreshButton.text = rb.getString("refresh")
    languageLabel.text = rb.getString("language")

  }
}
