//package com.nuschele
//
//import com.nuschele.random.{StreamedDatasetGenerator, TreeDatasetGenerator}
//import org.apache.spark.sql.catalyst.InternalRow
//import org.apache.spark.sql.{SequilaSession, SparkSession}
//import org.biodatageeks.formats.BrowserExtensibleData
//import org.biodatageeks.sequila.rangejoins.IntervalTree.Interval
//import org.biodatageeks.sequila.rangejoins.methods.IntervalTree.IntervalHolderChromosome
//import org.rogach.scallop.{ScallopConf, ScallopOption}
//
//object IntersectionByMap {
//
//  class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
//    val intervalHolderClass: ScallopOption[String] = opt[String](required = false,
//      default = Some("com.nuschele.structures.ailist.AIList"))
//    val treeTablePath: ScallopOption[String] = opt[String](required = true)
//    val datasetTablePath: ScallopOption[String] = opt[String](required = true)
//    verify()
//  }
//
//  def main(args: Array[String]): Unit = {
//    val conf = new Conf(args)
//    lazy val spark: SparkSession = {
//      SparkSession
//        .builder()
//        .master("local[*]")
//        .getOrCreate()
//    }
//    val ss = SequilaSession(spark)
//    ss.sql(
//      s"""
//         |CREATE TABLE IF NOT EXISTS dataset_tab
//         |USING org.biodatageeks.sequila.datasources.BED.BEDDataSource
//         |OPTIONS(path "${conf.datasetTablePath()}")
//         |
//      """.stripMargin)
//
//    ss.sql(
//      s"""
//         |CREATE TABLE IF NOT EXISTS tree_tab
//         |USING org.biodatageeks.sequila.datasources.BED.BEDDataSource
//         |OPTIONS(path "${conf.treeTablePath()}")
//         |
//      """.stripMargin)
//
//    import spark.implicits._
//
//    val dataset = ss
//      .sql(s"SELECT * FROM dataset_tab")
//      .as[BrowserExtensibleData]
//
//    val treeDataset = ss
//      .sql(s"SELECT * FROM tree_tab")
//      .as[BrowserExtensibleData]
//
//    val n = 10000000
//    val intervals = TreeDatasetGenerator.generateIntervals(n).map(p => ("chr1", Interval[Int](p._1, p._2), InternalRow.empty)).toArray
//    val query = StreamedDatasetGenerator.generateIntervals(10000, 0, n * 10).map(p => ("chr1", Interval[Int](p._1, p._2), InternalRow.empty)).toArray
//    val tree = new IntervalHolderChromosome[InternalRow](intervals, conf.intervalHolderClass())
//    val intervalTree = spark.sparkContext.broadcast(tree)
//    val cnt = query.map(rec => intervalTree.value.getIntervalTreeByChromosome(rec._1) match {
//      case Some(t) =>
//        val tuple = t.overlappers_custom(rec._2.start, rec._2.end)
//        val cost = tuple.costForQuery
//        cost
//      case _ => 0
//    }).reduce((a, b) => a + b)
//    println(cnt)
//  }
//}
