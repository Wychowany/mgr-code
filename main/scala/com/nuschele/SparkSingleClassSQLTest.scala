package com.nuschele

import org.apache.spark.sql.{SequilaSession, SparkSession}
import org.biodatageeks.sequila.utils.InternalParams
import org.rogach.scallop.{ScallopConf, ScallopOption}

object SparkSingleClassSQLTest {

  class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
    val intervalHolderClass: ScallopOption[String] = opt[String](required = true)
    val treeTablePath: ScallopOption[String] = opt[String](required = true)
    val datasetTablePath: ScallopOption[String] = opt[String](required = true)
    verify()
  }

  def main(args: Array[String]): Unit = {
    val conf = new Conf(args)
    lazy val spark: SparkSession = {
      SparkSession
        .builder()
        .master("local[*]")
        .config("spark.driver.memory", "14g")
        .config("spark.driver.maxResultSize", "6G")
        .getOrCreate()
    }
    println(s"applicationId=${spark.sparkContext.applicationId}")
    val ss = SequilaSession(spark)
    spark.sparkContext.setLogLevel("DEBUG")
    SequilaSession.register(ss)
    ss.sql(
      s"""
         |CREATE TABLE IF NOT EXISTS dataset_tab (contig STRING, pos_start INT, pos_end INT)
         |USING CSV LOCATION '${conf.datasetTablePath()}'
      """.stripMargin)

    ss.sql(
      s"""
         |CREATE TABLE IF NOT EXISTS tree_tab (contig STRING, pos_start INT, pos_end INT)
         |USING CSV LOCATION '${conf.treeTablePath()}'
      """.stripMargin)
    ss.sqlContext.setConf(InternalParams.useJoinOrder, "true")
    if (conf.intervalHolderClass.isDefined) {
      ss.sqlContext.setConf(InternalParams.intervalHolderClass, conf.intervalHolderClass())
    }
    ss.sqlContext.setConf(InternalParams.maxBroadCastSize, (8000L * 1024 * 1024).toString)
    val query =
      s"""
         | SELECT count(*) as cnt
         | FROM dataset_tab AS t1
         | JOIN tree_tab AS t2 ON
         | t1.contig = t2.contig AND
         | t2.pos_end >= t1.pos_start AND
         | t2.pos_start <= t1.pos_end""".stripMargin

   ss.time {
     val q = ss
       .sql(query)
     q.show
   }
  }
}


















//		@Override
//		public CustomNode<V> next() {
//			counter++;
//			int previous = qNext;
//			qNext = nextOverlapper();
//			return intervals.get(previous);
//		}
//
//
//		private boolean isImaginary(int nodeIndex) {
//			return nodeIndex > intervals.size() - 1;
//		}
//
//		@Override
//		public boolean hasNext() {
//			return qNext != -1 && counter < 2;
//		}

//		private int nextOverlapper() {
//			int nodeIndex = qNext;
//			if (nodeIndex == -1) {
//				throw new RuntimeException("Something wrong");
//			}
//			// w ogole jest sens sprawdzania prawego dziecka
//			if (intervals.get(nodeIndex).max >= qStart) {
//				int rightIndex = TreeOperations.right(nodeIndex, globalLevel);
//				globalLevel--;
//				if (globalLevel == -1) {
//					return -1;
//				} else if (isImaginary(rightIndex)) {
//					nodeIndex = rightIndex;
//					while (isImaginary(nodeIndex)) {
//						nodeIndex = TreeOperations.left(nodeIndex, globalLevel);
//						globalLevel--;
//					}
//					// that function updates globalLevel inside
//					return leastOverlap(nodeIndex, globalLevel);
//				} else {
//					return -1;
////					return leastOverlap(nodeIndex, globalLevel);
//				}
//				// nie ma sensu, idziemy w gore
//			} else {
//				// Jesli to prawe dziecko to znaczy ze rodzic juz byl odwiedzony i sprawdzony, idziemy w gore TODO, moze cos byc zle
//				while (TreeOperations.isRightChild(nodeIndex, globalLevel)) {
//					nodeIndex = TreeOperations.parent(nodeIndex, globalLevel);
//					globalLevel++;
//				}
//				if (nodeIndex == rootIndex) {
//					return -1;
//				}
//
//				// lewe dziecko
//				nodeIndex = TreeOperations.parent(nodeIndex, globalLevel);
//				globalLevel++;
//				if (isImaginary(nodeIndex)) {
//					return -1;
//				} else {
//					// ten node overlapuje wiec jest nastepny
//					if (qStart <= intervals.get(nodeIndex).getEnd() && qEnd >= intervals.get(nodeIndex).getStart()) {
//						return nodeIndex;
//
//						// nie jest ten node, ale jest sens isc w glab
//					} else if (intervals.get(nodeIndex).max >= qStart) {
//						nodeIndex = TreeOperations.right(nodeIndex,globalLevel);
//						globalLevel--;
//						while (isImaginary(nodeIndex)) {
//							nodeIndex = TreeOperations.left(nodeIndex, globalLevel);
//							globalLevel--;
//						}
//						return leastOverlap(nodeIndex, globalLevel);
//					} else {
//						return -1;
//					}
//				}
//			}
//		}
