package org.biodatageeks.tbd

import org.scalatest.FunSpec

import scala.collection.mutable.ArrayBuffer

class ImplicitTreeAugmenterSpec extends FunSpec {

  it("should augment complete tree correctly") {
    var tree = new ArrayBuffer[AugmentedNode[String]]()
    tree += new AugmentedNode[String](3, 20, "a")
    tree += new AugmentedNode[String](5, 12, "b")
    tree += new AugmentedNode[String](6, 8, "c")
    tree += new AugmentedNode[String](9, 11, "d")
    tree += new AugmentedNode[String](12, 18, "e")
    tree += new AugmentedNode[String](14, 22, "f")
    tree += new AugmentedNode[String](23, 55, "g")
    tree = tree.sortBy(p => p.getStart)
    val metadata = TreeMetadataRetriever.resolveKAndArraySizeIncludingImaginaryNodes(tree.size)
    ImplicitTreeAugmenter.augmentInsideMax(tree, metadata.K)
    assert(tree(0).insideMaxEnd == 20)
    assert(tree(1).insideMaxEnd == 20)
    assert(tree(2).insideMaxEnd == 8)
    assert(tree(3).insideMaxEnd == 55)
    assert(tree(4).insideMaxEnd == 18)
    assert(tree(5).insideMaxEnd == 55)
    assert(tree(6).insideMaxEnd == 55)
  }

  it("should augment incomplete tree correctly") {
    var tree = new ArrayBuffer[AugmentedNode[String]]()
    tree += new AugmentedNode[String](3, 20, "a")
    tree += new AugmentedNode[String](5, 12, "b")
    tree += new AugmentedNode[String](6, 8, "c")
    tree += new AugmentedNode[String](9, 11, "d")
    tree += new AugmentedNode[String](12, 18, "e")
    tree += new AugmentedNode[String](14, 22, "f")
    tree += new AugmentedNode[String](23, 55, "g")
    tree += new AugmentedNode[String](24, 28, "i")
    tree += new AugmentedNode[String](29, 32, value = "j")
    tree += new AugmentedNode[String](33, 40, value = "k")
    tree = tree.sortBy(p => p.getStart)
    val metadata = TreeMetadataRetriever.resolveKAndArraySizeIncludingImaginaryNodes(tree.size)
    ImplicitTreeAugmenter.augmentInsideMax(tree, metadata.K)
    assert(tree(0).insideMaxEnd == 20)
    assert(tree(1).insideMaxEnd == 20)
    assert(tree(2).insideMaxEnd == 8)
    assert(tree(3).insideMaxEnd == 55)
    assert(tree(4).insideMaxEnd == 18)
    assert(tree(5).insideMaxEnd == 55)
    assert(tree(6).insideMaxEnd == 55)
    assert(tree(7).insideMaxEnd == 55)
    assert(tree(8).insideMaxEnd == 32)
    assert(tree(9).insideMaxEnd == 40)
  }
}
