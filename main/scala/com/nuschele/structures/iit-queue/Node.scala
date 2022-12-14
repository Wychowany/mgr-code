package com.nuschele.structures.iit

import org.biodatageeks.sequila.rangejoins.methods.base.BaseNode

import java.util

class Node[V1](val start: Int, val end: Int) extends BaseNode[V1] with Serializable {
  var max: Int = end
  private val values: util.ArrayList[V1] = new util.ArrayList[V1]()

  def this(start: Int, end: Int, values: util.ArrayList[V1]) {
    this(start, end)
    this.values.addAll(values)
  }

  override def getStart: Int = start

  override def getEnd: Int = end

  override def getValue: util.ArrayList[V1] = values

  def getMax: Int = max

  def addValue(v: V1): V1 = {
    values.add(v)
    v
  }


  override def equals(other: Any): Boolean = other match {
    case that: Node[V1] =>
      start == that.start &&
        end == that.end &&
        values.equals(that.values)
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(start, end, values.hashCode())
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
