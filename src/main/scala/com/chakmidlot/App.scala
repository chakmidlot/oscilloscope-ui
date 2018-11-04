package com.chakmidlot

import com.chakmidlot.data.SocketData
import com.chakmidlot.dsp.Frequency
import scalafx.application.JFXApp
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Side
import scalafx.scene.{Cursor, Scene}
import scalafx.scene.chart.{AreaChart, NumberAxis, XYChart}
import scalafx.scene.control.Button
import scalafx.scene.layout.{HBox, Region, VBox}
import scalafx.scene.shape.{Circle, Shape}

import scala.util.Random

object App extends JFXApp {

  val dataSource = SocketData("localhost", 5003)

  val rnd = Random
  val signalChart = new AreaChart(NumberAxis("time"), NumberAxis("voltage")) {
    title = "Signal"
    legendSide = Side.Right
    legendVisible = false
//    shape = new Circle {radius = 0.1}
  }

  val frequencyChart = new AreaChart(NumberAxis("time"), NumberAxis("voltage")) {
    title = "Frequency"
    legendSide = Side.Right
  }
  frequencyChart.legendVisible = false

  stage = new JFXApp.PrimaryStage {
    title = "AreaChartDemo"
    scene = new Scene {
      content = new HBox {
        children = Seq(
          new VBox {
            children = Seq(
              signalChart,
              frequencyChart
            )
          },
          new Button {
            text = "Recalculate"
            onAction = _ => {
              updateData()
            }
          }
        )
      }
    }
  }

  def updateData(): Unit = {
    val period = 1
    val samplingFrequency = 10000
    val data = dataSource.getSignal(period, samplingFrequency)
    val frequencies = Frequency.analyze(data, period)

    val sampleLength = (samplingFrequency / frequencies.mainFrequency * 2).toInt
    val chartSample = data.slice(getStartingItem(data), getStartingItem(data) + sampleLength)

    val chartData = chartSample.indices.map(_.toDouble / samplingFrequency).zip(chartSample)

    signalChart.data = ObservableBuffer(
      xySeries("Series 1", chartData)
    )

    frequencyChart.data = ObservableBuffer(
      xySeries("Series 1", frequencies.frequencyGraph)
    )
  }

  def getStartingItem(data: Seq[Double]): Int = {
    val max = data.max
    val min = data.min

    if (max == min) {
      return 0
    }

    val average = (max + min) / 2
    var under = false

    for (i <- data.indices) {
      if (data(i) < average) {
        under = true
      }
      else if (under) {
        println(i)
        return i
      }
    }
    0
  }

  def xySeries(name: String, data: Seq[(Double, Double)]) = {
    val series = XYChart.Series[Number, Number](
      name,
      ObservableBuffer(data.map { case (x, y) => XYChart.Data[Number, Number](x, y) })
    )
    series
  }
}
