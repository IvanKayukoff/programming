package xyz.sky731.programming.lab7

import cartesian.coordinate.CCPolygon
import cartesian.coordinate.CCSystem
import xyz.sky731.programming.lab3.Bredlam
import xyz.sky731.programming.lab5.Bredlams
import xyz.sky731.programming.lab5.JsonUser
import xyz.sky731.programming.lab6.ClientMain
import java.awt.*
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel

class ClientGUI(private val client: ClientMain, nameFrame: String) : JFrame(nameFrame) {

  fun drawCircle(plot: CCSystem, x: Double, y: Double, r: Double, color: Color) {
    val coordinates = Coordinates.caclCircleCoords(x, y, r)
    val polygon = CCPolygon(coordinates.x, coordinates.y, color, color, BasicStroke(1f))
    plot.add(polygon)
  }

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

      // Poop plot
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
        preferredSize = Dimension(480, 480)
      }


      bredlams.bredlam.forEach {
        drawCircle(graph, it.coordinates.x.toDouble(), it.coordinates.y.toDouble(),
            Math.sqrt(it.population.toDouble()), it.flagColor.color)
      }

      add(graph)


      /** Panel with filters */
      add(JPanel().apply {
        border = BorderFactory.createLineBorder(Color.BLUE)
        preferredSize = Dimension(300, 580)
        layout = GridBagLayout()
        val constraints = GridBagConstraints().apply {
          gridx = 0
          gridy = 0
          gridwidth = 2
          gridheight = 1
        }
        add(JButton("req0"), constraints)
        constraints.gridy = 1
        add(JButton("req1"), constraints)
      })

    }
    contentPane.add(ownPanel)
    pack()
    isVisible = true
  }
}