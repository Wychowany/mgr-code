//package com.nuschele
//
//import com.nuschele.random.UniformDistribution
//import org.apache.spark.sql.catalyst.InternalRow
//import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
//import org.apache.spark.sql.{SequilaSession, SparkSession}
//import org.apache.spark.util.SizeEstimator
//import org.biodatageeks.sequila.rangejoins.IntervalTree.Interval
//import org.biodatageeks.sequila.rangejoins.methods.IntervalTree.IntervalHolderChromosome
//import org.rogach.scallop.{ScallopConf, ScallopOption}
//
//import scala.collection.convert.ImplicitConversions.{`collection AsScalaIterable`, `iterator asScala`}
//
//object GenomeIntersection {
//
//  class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
//    val fraction: ScallopOption[String] = opt[String](required = true)
//    val clazz: ScallopOption[String] = opt[String](required = true)
//    verify()
//  }
//
//  def main(args: Array[String]): Unit = {
//    val conf = new Conf(args)
//    lazy val spark: SparkSession = {
//      SparkSession
//        .builder()
//        .config("spark.driver.memory", "9g")
//        .master("local[1]")
//        .getOrCreate()
//    }
//    val fraction: Double = conf.fraction().toDouble
//    val ss = SequilaSession(spark)
//    spark.sparkContext.setLogLevel("INFO")
//    val schema = StructType(Seq(
//      StructField("chr", StringType),
//      StructField("begin", IntegerType),
//      StructField("end", IntegerType),
//    ))
//    import spark.implicits._
//    val treeDataset = spark.read.schema(schema).csv("/Users/olek/Desktop/magisterka/genomes.csv").sample(fraction, 1).as[Tupl].collect()
//    val min = treeDataset(0).begin
//    val max = treeDataset(treeDataset.length - 1).end
//    val mapped = treeDataset.map(p => ("chr", Interval[Int](p.begin, p.end), InternalRow.empty))
//    val tree = ss.time {
//      new IntervalHolderChromosome[InternalRow](mapped, conf.clazz())
//    }
//    println("SizeEstimator=" + SizeEstimator.estimate(tree))
//    val streamedRandomDataset = spark.read.schema(schema).csv("/Users/olek/Desktop/magisterka/genomes.csv").map(p => Tupl("chr", p.getAs[Int](1), p.getAs[Int](2))).as[Tupl].sample(fraction * 0.25, 1)
//    val uniformlyDistributed = UniformDistribution.generate((5000000 * fraction).toInt, min, max)
//    val uniformlyDistributedDataset = spark.sparkContext.parallelize(uniformlyDistributed).map(begin => Tupl("chr", begin, begin + 10)).toDS()
//    val testStreamedDataset = streamedRandomDataset.union(uniformlyDistributedDataset)
//    testStreamedDataset.cache().count()
//    val count = ss.time {
//      val cnt = testStreamedDataset.map(rec => tree.getIntervalTreeByChromosome(rec.chr) match {
//        case Some(t) =>
//          val tuple = t.overlappers_custom(rec.begin, rec.end)
//          tuple.it.toList
//            .flatMap(k => k.getValue.map(_ => 1))
//        case _ => List.empty
//      }).flatMap(k => k).count()
//        cnt
//    }
//    println(s"Returned elements: $count")
//  }
//}
//
//case class Tupl(chr: String, begin: Int, end: Int)
