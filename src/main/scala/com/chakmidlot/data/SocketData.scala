package com.chakmidlot.data

import java.net.Socket


class SocketData(ip: String, port: Int) extends Data{

  val socket = new Socket("localhost", 5003)
  val inputStream = socket.getInputStream
  val outputStream = socket.getOutputStream

  override def getSignal(period: Double, frequency: Double): Seq[Double] = {
    outputStream.write(f"sample,$period,$frequency".getBytes)

    val buffer = new Array[Byte]((frequency * period).toInt)

    var totalConsumed = 0

    while (totalConsumed < 6) {
      val ret = inputStream.read(buffer, totalConsumed, buffer.length - totalConsumed)
      if (ret == -1) {
        throw new Exception("Data source is disconnected")
      }
      totalConsumed += ret
    }

    var sample = Seq[Double]() ++ buffer.slice(6, buffer.length).map(x => uByteToInt(x).toDouble)

    val sampleLength =
      (uByteToInt(buffer(0)) << 24) +
      (uByteToInt(buffer(1)) << 16) +
      (uByteToInt(buffer(2)) << 8) +
      uByteToInt(buffer(3))

    while (totalConsumed < sampleLength + 4) {
      val ret = inputStream.read(buffer, 0, buffer.length)
      if (ret == -1) {
        throw new Exception("Data source is disconnected")
      }
      totalConsumed += ret
      sample ++= buffer.slice(0, ret).map(x => uByteToInt(x).toDouble)
    }

    sample
  }

  def uByteToInt(value: Byte): Int = {
    if (value < 0) {
      value + 256
    }
    else {
      value
    }
  }
}

object SocketData {
  def apply(ip: String, port: Int): SocketData = new SocketData(ip, port)
}