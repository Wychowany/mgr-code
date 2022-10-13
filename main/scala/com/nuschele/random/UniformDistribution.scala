package com.nuschele.random

import scala.collection.mutable.ArrayBuffer

object UniformDistribution {
  def generate(n: Int, begin: Int, end: Int): ArrayBuffer[Int] = {
    val random = new scala.util.Random(1)
    val dist = new ArrayBuffer[Int]()
    1 to n foreach (
      _ => dist.append(begin + math.floor(random.nextDouble()* (end-begin)).toInt)
      )
    dist
  }
}
