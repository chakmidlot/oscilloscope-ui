package com.chakmidlot.data

trait Data {
  def getSignal(period: Double, frequency: Double): Seq[Double]
}
