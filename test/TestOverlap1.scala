package org.biodatageeks.tbd.cgranges

import org.biodatageeks.sequila.rangejoins.methods.base.BaseNode
import org.biodatageeks.tbd.ImplicitIntervalTreeWithInterpolationIndex
import org.scalatest.FunSpec

import scala.collection.mutable.ArrayBuffer

class TestOverlap1 extends FunSpec {
  it("should work on other examples") {
    val tree = new ImplicitIntervalTreeWithInterpolationIndex[Int]
    val dummyValue = -1
    tree.put(1, 5, dummyValue)
    tree.put(6, 10, dummyValue)
    tree.put(11, 15, dummyValue)
    tree.put(16, 20, dummyValue)
    tree.put(21, 25, dummyValue)
    tree.put(26, 30, dummyValue)
    tree.put(31, 35, dummyValue)
    tree.put(36, 40, dummyValue)
    tree.put(41, 45, dummyValue)
    tree.put(46, 50, dummyValue)
    tree.put(51, 55, dummyValue)
    tree.put(55, 60, dummyValue)
    tree.put(61, 65, dummyValue)
    tree.put(66, 70, dummyValue)
    tree.put(71, 75, dummyValue)
    tree.put(76, 80, dummyValue)
    tree.put(81, 85, dummyValue)
    tree.put(86, 90, dummyValue)
    tree.put(91, 95, dummyValue)
    tree.put(96, 100, dummyValue)
    tree.postConstruct
    val i = tree.overlappers(1, 100) // 3
    val resultList = new ArrayBuffer[BaseNode[Int]]()
    i.forEachRemaining(x => resultList.append(x))
    assert(resultList.size == 20)
  }
}
