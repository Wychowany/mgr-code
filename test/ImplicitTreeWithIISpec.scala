package org.biodatageeks.tbd

import org.biodatageeks.sequila.rangejoins.methods.base.BaseNode
import org.scalatest.FunSpec

import scala.collection.mutable.ArrayBuffer

class ImplicitTreeWithIISpec extends FunSpec {
  it("should work on cgranges examples") {
    val tree = new ImplicitIntervalTreeWithInterpolationIndex[Int]
    val dummyValue = -1
    tree.put(12, 34, dummyValue)
    tree.put(0, 23, dummyValue)
    tree.put(34, 56, dummyValue)
    tree.postConstruct
    val i = tree.overlappers(22, 25)
    val resultList = new ArrayBuffer[BaseNode[Int]]()
    i.forEachRemaining(x => resultList.append(x))
    assert(resultList.size == 2)
  }

  it("should work on other examples") {
    val tree = new ImplicitIntervalTreeWithInterpolationIndex[Int]
    val dummyValue = -1
    tree.put(0, 7, dummyValue)
    tree.put(1, 2, dummyValue)
    tree.put(2, 4, dummyValue)
    tree.put(3, 6, dummyValue)
    tree.put(4, 9, dummyValue)
    tree.postConstruct
    val i = tree.overlappers(7, 10)
    val resultList = new ArrayBuffer[BaseNode[Int]]()
    i.forEachRemaining(x => resultList.append(x))
    assert(resultList.size == 1)
  }
}
