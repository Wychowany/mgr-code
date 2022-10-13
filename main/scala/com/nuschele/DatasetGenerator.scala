package com.nuschele

import com.nuschele.random.UniformDistribution
import org.apache.spark.sql.types.{IntegerType, StructType}

object DatasetGenerator {
  def main(args: Array[String]): Unit = {
    //    val fractions = List(0.05, 0.1, 0.15, 0.2, 0.25, 0.3, 0.35, 0.4, 0.45, 0.5, 0.55, 0.6)
    val intervalLengths = List(10, 50, 100, 200, 500, 1000, 2000, 4000, 8000, 10000)
    import org.apache.spark.sql.SparkSession
    lazy val spark: SparkSession = {
      SparkSession
        .builder()
        .config("spark.driver.memory", "9g")
        .master("local[*]")
        .getOrCreate()
    }
    import spark.implicits._

    val schema = new StructType()
      .add("chr", IntegerType, false)
      .add("start", IntegerType, false)
      .add("end", IntegerType, false)
    val treeDataset = spark.read.schema(schema).csv("/Users/aleksandernuszel/Desktop/private/magisterka/genomes.csv")
    println(treeDataset.printSchema())
    val min = treeDataset.reduce((r1, r2) => {
      if (r1.getAs[Int](1) < r2.getAs[Int](1)) {
        r1
      } else {
        r2
      }
    }).getAs[Int](1)

    val max = treeDataset.reduce((r1, r2) => {
      if (r1.getAs[Int](2) < r2.getAs[Int](2)) {
        r2
      } else {
        r1
      }
    }).getAs[Int](2)
    val uniformlyDistributedData = UniformDistribution.generate((20000000 * 0.3).toInt, min, max)
    for (intervalLength <- intervalLengths) {
      val uniformlyDistributedRDD = spark.sparkContext.parallelize(uniformlyDistributedData).map(p => (2, p, p + intervalLength))
      val uniformlyDistributedDataset = spark.createDataset(uniformlyDistributedRDD).repartition(1)
      uniformlyDistributedDataset.write.csv(s"/Users/aleksandernuszel/Desktop/private/magisterka/interval-length/$intervalLength")
    }
  }
}

