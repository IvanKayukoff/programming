package xyz.sky731.programming.lab7

import cartesian.coordinate.CCPolygon
import cartesian.coordinate.CCSystem
import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab5.Bredlams
import xyz.sky731.programming.lab5.JsonUser
import xyz.sky731.programming.lab6.ClientMain
import java.awt.*
import javax.swing.*
import sun.plugin.navig.motif.Plugin.start
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseEvent
import java.awt.geom.Point2D
import kotlin.concurrent.timer


class ClientGUI(private val client: ClientMain, nameFrame: String) : JFrame(nameFrame) {

  fun drawCircle(plot: CCSystem, x: Double, y: Double, r: Double, color: Color, fillingColor: Color) {
    val coordinates = Coordinates.caclCircleCoords(x, y, r)
    val polygon = CCPolygon(coordinates.x, coordinates.y, color,
        fillingColor, BasicStroke(1f))
    plot.add(polygon)
  }

  val drawedBredlams = ArrayList<Pair<Bredlam, Color>>()

  val colorRedCheckbox = JCheckBox("Red")
  val colorBlueCheckbox = JCheckBox("Blue")
  val colorPinkCheckbox = JCheckBox("Pink")
  val colorOrangeCheckbox = JCheckBox("Orange")
  val colorYellowCheckbox = JCheckBox("Yellow")
  val colorGreenCheckbox = JCheckBox("Green")

  val fromXSpinner = JSpinner()
  val toXSpinner = JSpinner()
  val fromYSpinner = JSpinner()
  val toYSpinner = JSpinner()

  val nameTextField = JTextField()
  val endOfLightCheckbox = JCheckBox("End of light")

  val fromPopulationSpinner = JSpinner()
  val toPopulationSpinner = JSpinner()

  val filtersCheckBox = JCheckBox("Filters enabled")

  var started = false
  val startButton = JButton("Start")
  val stopButton = JButton("Stop")

  var timer: Timer? = null

  init {
    defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    isResizable = false
    preferredSize = Dimension(800, 600)

    val ownPanel = JPanel().apply {
      border = BorderFactory.createLineBorder(Color.RED)
      preferredSize = Dimension(800, 600)
      layout = FlowLayout()

      val jsonUser = JsonUser()
      val response = client.sendMessage("get_collection")
      val bredlams = if (response != "") jsonUser.unmarshal(response)
      else Bredlams().apply { bredlam = ArrayList<Bredlam>() }

      val maxPopulation = bredlams.bredlam.run {
        var res = 0
        this.forEach {
          res = if (it.population > res) it.population else res
        }
        res
      }

      /** Plot normalization */
      var minX = bredlams.bredlam.run {
        var res = if (this.size > 0) this[0].coordinates.x - Math.sqrt(this[0].population.toDouble()) else -10.0
        this.forEach {
          res = if (it.coordinates.x - Math.sqrt(it.population.toDouble()) < res) it.coordinates.x -
              Math.sqrt(it.population.toDouble()) else res
        }
        res
      }
      var minY = bredlams.bredlam.run {
        var res = if (this.size > 0) this[0].coordinates.y - Math.sqrt(this[0].population.toDouble()) else -10.0
        this.forEach {
          res = if (it.coordinates.y - Math.sqrt(it.population.toDouble()) < res) it.coordinates.y -
              Math.sqrt(it.population.toDouble()) else res
        }
        res
      }
      var maxX = bredlams.bredlam.run {
        var res = if (this.size > 0) this[0].coordinates.x + Math.sqrt(this[0].population.toDouble()) else 10.0
        this.forEach {
          res = if (it.coordinates.x + Math.sqrt(it.population.toDouble()) > res) it.coordinates.x +
              Math.sqrt(it.population.toDouble()) else res
        }
        res
      }
      var maxY = bredlams.bredlam.run {
        var res = if (this.size > 0) this[0].coordinates.y + Math.sqrt(this[0].population.toDouble()) else 10.0
        this.forEach {
          res = if (it.coordinates.y + Math.sqrt(it.population.toDouble()) > res) it.coordinates.y +
              Math.sqrt(it.population.toDouble()) else res
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
        }

        override fun getToolTipText(e: MouseEvent): String? {
          val coordinates = CCSystem::class.java.getDeclaredMethod("translate", Point::class.java).apply {
            isAccessible = true
          }.invoke(this, Point(e.x, this.height - e.y)) as Point2D.Double

          val mx = coordinates.x
          val my = coordinates.y

          bredlams.bredlam.forEach {
            if (Math.sqrt((mx - it.coordinates.x) * (mx - it.coordinates.x) +
                    (my - it.coordinates.y) * (my - it.coordinates.y)) <=
                if (it.population > 0) Math.sqrt(it.population.toDouble()) else 1.0) {
              return it.name
            }
          }

          return null
        }
      }

      drawedBredlams.clear()
      bredlams.bredlam.forEach {
        drawedBredlams.add(Pair(it, it.flagColor.color))
        drawCircle(graph, it.coordinates.x.toDouble(), it.coordinates.y.toDouble(),
            if (it.population > 0) Math.sqrt(it.population.toDouble()) else 1.0,
            it.flagColor.color, if (it.population == 0) Color.WHITE else it.flagColor.color)
      }

      add(graph)


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
        add(JLabel("Colors:"), constraints)

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
        add(JLabel("Coordinates:"), constraints)
        constraints.fill = GridBagConstraints.NONE
        constraints.insets = Insets(5, 15, 0, 0)

        constraints.gridy = 10
        add(JPanel().apply {
          layout = FlowLayout()
          add(JLabel("X  from: "), constraints)

          constraints.insets = Insets(5, 15, 0, 0)

          add(fromXSpinner.apply {
            model = SpinnerNumberModel(minX, minX, maxX, 1)
            preferredSize = Dimension(80, 20)
          })
          add(JLabel(" to: "))
          add(toXSpinner.apply {
            model = SpinnerNumberModel(maxX, minX, maxX, 1)
            preferredSize = Dimension(80, 20)
          })

        }, constraints)

        constraints.gridy = 11
        add(JPanel().apply {
          layout = FlowLayout()

          add(JLabel("Y  from: "))

          constraints.insets = Insets(5, 15, 0, 0)

          add(fromYSpinner.apply {
            model = SpinnerNumberModel(minY, minY, maxY, 1)
            preferredSize = Dimension(80, 20)
          })
          add(JLabel(" to: "))
          add(toYSpinner.apply {
            model = SpinnerNumberModel(maxY, minY, maxY, 1)
            preferredSize = Dimension(80, 20)
          })

        }, constraints)

        constraints.gridy = 12
        add(JPanel().apply {
          layout = FlowLayout()

          add(JLabel("Name starts with: "))
          add(nameTextField.apply {
            preferredSize = Dimension(130, 20)
          })
        }, constraints)

        constraints.gridy = 13
        add(endOfLightCheckbox, constraints)

        constraints.gridy = 14
        add(JPanel().apply {
          layout = FlowLayout()

          add(JLabel("Population from: "))
          add(fromPopulationSpinner.apply {
            model = SpinnerNumberModel(0, 0, maxPopulation, 1)
            preferredSize = Dimension(50, 20)
          })

          add(JLabel(" to: "))
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
                  colorBlueCheckbox.isSelected && it.flagColor.color == Color.BLUE ||
                      colorGreenCheckbox.isSelected && it.flagColor.color == Color.GREEN ||
                      colorOrangeCheckbox.isSelected && it.flagColor.color == Color.ORANGE ||
                      colorPinkCheckbox.isSelected && it.flagColor.color == Color.PINK ||
                      colorRedCheckbox.isSelected && it.flagColor.color == Color.RED ||
                      colorYellowCheckbox.isSelected && it.flagColor.color == Color.YELLOW
                }

                filtered = filtered.filter {
                  it.coordinates.x >= fromXSpinner.value as Double &&
                      it.coordinates.x <= toXSpinner.value as Double &&
                      it.coordinates.y >= fromYSpinner.value as Double &&
                      it.coordinates.y <= toXSpinner.value as Double
                }

                filtered = filtered.filter { endOfLightCheckbox.isSelected == it.endOfLight }

                filtered = filtered.filter {
                  it.population >= fromPopulationSpinner.value as Int &&
                      it.population <= toPopulationSpinner.value as Int
                }

                filtered = filtered.filter {
                  it.name.toLowerCase().startsWith(nameTextField.text.toLowerCase())
                }
                filtered.forEach {
                  println("Picked up: $it")
                }
              }

              var tickCounter = 0
              timer = Timer(50, {
                graph.clear()
                (bredlams.bredlam - filtered).forEach {
                  drawCircle(graph, it.coordinates.x.toDouble(), it.coordinates.y.toDouble(),
                      if (it.population > 0) Math.sqrt(it.population.toDouble()) else 1.0,
                      it.flagColor.color, if (it.population == 0) Color.WHITE else it.flagColor.color)
                }
                filtered.forEach {
                  val color = it.flagColor.color

                  var fillingColor = Color.white

                  if (it.population == 0) {
                    fillingColor = if (tickCounter % 140 < 60)
                      Color(255, 255, 255, (255 - (tickCounter % 140) * 4.25).toInt())
                    else Color(255, 255, 255, (0 + (tickCounter % 140 - 60) * 3.1875).toInt())
                  } else {
                    fillingColor = if (tickCounter % 140 < 60)
                      Color(color.red, color.green, color.blue, (255 - (tickCounter % 140) * 4.25).toInt())
                    else Color(color.red, color.green, color.blue, (0 + (tickCounter % 140 - 60) * 3.1875).toInt())
                  }


                  drawCircle(graph, it.coordinates.x.toDouble(), it.coordinates.y.toDouble(),
                      if (it.population > 0) Math.sqrt(it.population.toDouble()) else 1.0,
                      if (tickCounter % 140 < 60)
                        Color(color.red, color.green, color.blue, (255 - (tickCounter % 140) * 4.25).toInt())
                      else Color(color.red, color.green, color.blue, (0 + (tickCounter % 140 - 60) * 3.1875).toInt()),
                      fillingColor)
                }
                tickCounter++
                // graph.repaint()


              }).apply { start() }

            }
          })

          add(stopButton.apply {
            addActionListener {
              // TODO
              isEnabled = false
              startButton.isEnabled = true
              timer?.stop()

              graph.clear()
              bredlams.bredlam.forEach {
                drawCircle(graph, it.coordinates.x.toDouble(), it.coordinates.y.toDouble(),
                    if (it.population > 0) Math.sqrt(it.population.toDouble()) else 1.0,
                    it.flagColor.color, if (it.population == 0) Color.WHITE else it.flagColor.color)
              }

            }
          })
        }, constraints)

        constraints.gridy = 17
        add(JLabel(" "), constraints)

        constraints.gridy = 18
        add(JPanel().apply {
          layout = FlowLayout()

          add(JLabel("Reload collection "))

          add(JButton("Refresh").apply {
            addActionListener {
              val gui = ClientGUI(client, nameFrame)
              this@ClientGUI.isVisible = false
              gui.isVisible = true
              this@ClientGUI.dispose()
            }
          })
        }, constraints)

      })

    }
    contentPane.add(ownPanel)
    pack()
    isVisible = true
  }
}
