package com.chakmidlot.dsp

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D

case class OscilloscopeData(
    signalGraph: Seq[(Double, Double)],
    frequencyGraph: Seq[(Double, Double)],
    mainFrequency: Double
)


object Frequency {

  def analyze(data: Seq[Double], period: Double): OscilloscopeData = {
    val dataArray = data.toArray
    val frequenciesArray = new Array[Double](dataArray.length / 2)

    val dataPeriod = period / data.length
    val frequencyStep = 1 / period

    val transformer = new DoubleFFT_1D(dataArray.length)
    transformer.realForward(dataArray)

    for (i <- frequenciesArray.indices) {
      frequenciesArray(i) = dataArray(2*i) * dataArray(2*i) + dataArray(2*i+1) * dataArray(2*i+1)
    }

    val frequencies = frequenciesArray.indices.map(_ * frequencyStep).zip(frequenciesArray)

    OscilloscopeData(
      data.indices.map(_ * dataPeriod).zip(data),
      frequencies,
      frequencies.slice(1, frequencies.length).maxBy(_._2)._1,
    )
  }
}
