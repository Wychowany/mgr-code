package com.nuschele.random

object TreeDatasetGenerator {
  def generateIntervals(n: Int): List[(Int, Int)] = {
    val begins = (10 to n * 10 by 10).toList
    val ranges = GeometricDistribution.generate(n, 20)
    val zipped = begins.zip(ranges)
    val intervals = zipped.map(x => (x._1, x._1 + x._2))
    intervals
  }
}
