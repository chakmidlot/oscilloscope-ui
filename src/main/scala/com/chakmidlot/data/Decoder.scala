package com.chakmidlot.data

trait Decoder {
  def decode(input: Array[Byte]): Seq[Double]
}
