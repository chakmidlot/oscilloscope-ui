package scalafx.scene.chart

import scalafx.application.JFXApp
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Side
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.VBox

import scala.util.Random


object ChartsUI extends JFXApp {

  val rnd = Random
  val chart = new AreaChart(NumberAxis("time"), NumberAxis("voltage")) {
    title = "Area Chart"
    legendSide = Side.Right
  }
  chart.legendVisible = false

  stage = new JFXApp.PrimaryStage {
    title = "AreaChartDemo"
    scene = new Scene {
      content = new VBox {
        children = Seq(
          chart,
          new Button {
            text = "Recalculate"
            onAction = event => { updateData() }
          }
        )
      }
    }
  }

  def updateData(): Unit = {
    val voltage = (0 until 1024).map(x => (x, math.sin(x * math.Pi / 180) + rnd.nextDouble()))
    chart.data = ObservableBuffer(
      xySeries("Series 1", voltage)
    )
  }

  /** Create XYChart.Series from a sequence of number pairs. */
  def xySeries(name: String, data: Seq[(Int, Double)]) =
    XYChart.Series[Number, Number](
      name,
      ObservableBuffer(data.map {case (x, y) => XYChart.Data[Number, Number](x, y)})
    )
}
