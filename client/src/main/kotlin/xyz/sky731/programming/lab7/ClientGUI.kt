package xyz.sky731.programming.lab7

import cartesian.coordinate.CCPolygon
import cartesian.coordinate.CCSystem
import org.intellij.lang.annotations.Flow
import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab5.Bredlams
import xyz.sky731.programming.lab5.JsonUser
import xyz.sky731.programming.lab6.ClientMain
import java.awt.*
import javax.swing.*

class ClientGUI(private val client: ClientMain, nameFrame: String) : JFrame(nameFrame) {

  fun drawCircle(plot: CCSystem, x: Double, y: Double, r: Double, color: Color, isEmpty: Boolean) {
    val coordinates = Coordinates.caclCircleCoords(x, y, r)
    val polygon = CCPolygon(coordinates.x, coordinates.y, color,
        if (isEmpty) Color.white else color, BasicStroke(1f))
    plot.add(polygon)
  }

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
      val graph = CCSystem(minX, minY, maxX, maxY).apply {
        preferredSize = Dimension(460, 460)
      }


      bredlams.bredlam.forEach {
        drawCircle(graph, it.coordinates.x.toDouble(), it.coordinates.y.toDouble(),
            if (it.population > 0) Math.sqrt(it.population.toDouble()) else 1.0,
            it.flagColor.color, if (it.population > 0) false else true)
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
        add(JCheckBox("Filters enabled"), constraints)

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
        add(colorRedCheckbox, constraints)

        constraints.gridy = 4
        add(colorGreenCheckbox, constraints)

        constraints.gridy = 5
        add(colorBlueCheckbox, constraints)

        constraints.gridy = 6
        add(colorPinkCheckbox, constraints)

        constraints.gridy = 7
        add(colorOrangeCheckbox, constraints)

        constraints.gridy = 8
        add(colorYellowCheckbox, constraints)

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

          add(JLabel("to: "))
          add(toPopulationSpinner.apply {
            model = SpinnerNumberModel(0, 0, maxPopulation, 1)
            preferredSize = Dimension(50, 20)
          })
        }, constraints)

      })

    }
    contentPane.add(ownPanel)
    pack()
    isVisible = true
  }
}
