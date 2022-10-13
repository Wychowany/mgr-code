package com.nuschele

import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.types.{LongType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}
import org.biodatageeks.sequila.rangejoins.methods.IntervalTree.IntervalTreeRedBlack


object ParseData {


  def main(args: Array[String]): Unit = {
    lazy val spark: SparkSession = {
      SparkSession
        .builder()
        .master("local[*]")
        .getOrCreate()
    }
    val schema = StructType(Seq(
      StructField("chr", StringType),
      StructField("begin", LongType),
      StructField("end", LongType),
    ))
    val df = spark.read.format("vcf").load("/Users/olek/Downloads/gnomad.genomes.r2.1.1.sites.2.vcf.gz")
    val mapped = df.map(row => {
      Row(row.getAs[String](0), row.getAs[Long](1), row.getAs[Long](2))
    })(RowEncoder.apply(schema)).repartition(1)
    mapped.write.csv("/Users/olek/Desktop/magisterka/one_part_genomes/unpacked_genomes.csv")
  }
}
