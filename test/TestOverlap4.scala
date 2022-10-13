package org.biodatageeks.tbd.cgranges
import org.biodatageeks.sequila.rangejoins.methods.base.BaseNode
import org.biodatageeks.tbd.ImplicitIntervalTreeWithInterpolationIndex
import org.scalatest.FunSpec

import scala.collection.mutable.ArrayBuffer

class TestOverlap4 extends FunSpec {
  it("should work on other examples") {
    val tree = new ImplicitIntervalTreeWithInterpolationIndex[Int]
    val dummyValue = -1
    tree.put(60, 100, dummyValue)
    tree.postConstruct
    val i = tree.overlappers(40, 60)
    val j = tree.overlappers(99, 101)
    val resultList = new ArrayBuffer[BaseNode[Int]]()
    i.forEachRemaining(x => resultList.append(x))
    j.forEachRemaining(x => resultList.append(x))
    assert(resultList.size == 1)
  }
}
