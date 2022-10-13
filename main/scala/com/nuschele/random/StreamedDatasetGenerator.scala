package com.nuschele.random

object StreamedDatasetGenerator {
  def generateIntervals(n: Int, beginRange: Int, endRange: Int): List[(Int, Int)] = {
    val begins = UniformDistribution.generate(n, beginRange, endRange)
    val ranges = GeometricDistribution.generate(n, 10)
    val dataset = begins.zip(ranges).map(x => (x._1, x._1 + x._2))
    dataset.toList
  }
}
