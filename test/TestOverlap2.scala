package org.biodatageeks.tbd.cgranges

import org.biodatageeks.sequila.rangejoins.methods.base.BaseNode
import org.biodatageeks.tbd.ImplicitIntervalTreeWithInterpolationIndex
import org.scalatest.FunSpec

import scala.collection.mutable.ArrayBuffer

class TestOverlap2 extends FunSpec {
  it("should work on other examples") {
    val tree = new ImplicitIntervalTreeWithInterpolationIndex[Int]
    val dummyValue = -1
    tree.put(10, 20, dummyValue)
    tree.put(40, 60, dummyValue)
    tree.put(45, 55, dummyValue)
    tree.put(49, 50, dummyValue)
    tree.postConstruct
    val i = tree.overlappers(40, 60) // 3
    val resultList = new ArrayBuffer[BaseNode[Int]]()
    i.forEachRemaining(x => resultList.append(x))
    assert(resultList.size == 3)
  }
}
