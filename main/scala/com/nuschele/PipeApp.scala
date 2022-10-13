package com.nuschele

import org.apache.spark.{SparkEnv, TaskContext}
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.{Dataset, Encoders, SparkSession}
import org.apache.spark.sql.types.{IntegerType, StructType}

import java.io.{BufferedWriter, FileWriter}

object PipeApp {
  def main(args: Array[String]): Unit = {
    lazy val spark: SparkSession = {
      SparkSession
        .builder()
        .master("local[1]")
        .getOrCreate()
    }
    val schema = new StructType()
      .add("chr", IntegerType, false)
      .add("start", IntegerType, false)
      .add("end", IntegerType, false)
    val csv = spark.createDataset(spark.read.schema(schema).csv("/Users/olek/Desktop/magisterka/genomes.csv").rdd)(RowEncoder.apply(schema))
    val d = csv.mapPartitions(p => {
      val file = s"/Users/olek/Desktop/magisterka/${TaskContext.getPartitionId()}temporary.bed"
      val writer = new BufferedWriter(new FileWriter(file))
      for (elem <- p.toList) {
        writer.write(s"${elem.getAs[Int](0)}\t${elem.getAs[Int](1)}\t${elem.getAs[Int](2)}\n")
      }
      writer.close()
      val e = "4"
      val lines = sys.process.Process(
        Seq("bash", "-c", s"cd /Users/olek/Desktop/cgranges/test; ./bedcov-cr /Users/olek/Desktop/magisterka/bed_streamed.bed ${file} -c | gawk -F'\\t' '{ sum += $e } END{ print sum }'")
      ).
        lines.
        toIterator
      println(s"${TaskContext.getPartitionId()} on executor")
      p
    })(RowEncoder.apply(schema))
    println(d.count())
  }
}

//case class Tupl(chr: String, begin: Int, end: Int);
