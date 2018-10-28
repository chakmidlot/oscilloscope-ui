import scalafx.application.JFXApp
import scalafx.scene.{Scene, chart}
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}

object UI extends JFXApp {
  val xAxis = new NumberAxis()
  val yAxis = new NumberAxis()
  val graph = LineChart(xAxis, yAxis)
  val series = new XYChart.Series[Number, Number]()
  graph.getData.add(series)
  for (i <- 0 to 100) {
    series.getData.add(new XYChart.Data[Number, Number]())
  }

  stage = new JFXApp.PrimaryStage {
    title.value = "Hello Stage"
    width = 600
    height = 450
    scene = new Scene {
      content = graph
    }
  }
}
