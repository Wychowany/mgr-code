package com.nuschele.random

import scala.collection.mutable.ArrayBuffer

object GeometricDistribution {
  // mean is expected value E[X] = 1/p
  def generate(n: Int, mean: Int): ArrayBuffer[Int] = {
    val dist = new ArrayBuffer[Int]()
    val p: Double = 1.toDouble / mean
    1 to n foreach (
      _ => dist.append(math.ceil(math.log(1 - math.random()) / math.log(1 - p)).toInt)
      )
    dist
  }
}
