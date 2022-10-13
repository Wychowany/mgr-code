package org.biodatageeks.tbd

import org.scalatest.FunSpec

class TreeMetadataRetrieverSpec extends FunSpec {

  it("should correctly calculate K and N when complete tree") {
    val results: TreeMetadata = TreeMetadataRetriever.resolveKAndArraySizeIncludingImaginaryNodes(15);
    val K: Int = results.K
    val arraySizeIncludingImaginaryNodes: Int = results.arraySizeIncludingImaginaryNodes
    assert(arraySizeIncludingImaginaryNodes == 15)
    assert(K == 3)
  }

  it("should correctly calculate K and N when not complete tree") {
    val results: TreeMetadata = TreeMetadataRetriever.resolveKAndArraySizeIncludingImaginaryNodes(21);
    val K: Int = results.K
    val arraySizeIncludingImaginaryNodes: Int = results.arraySizeIncludingImaginaryNodes
    assert(arraySizeIncludingImaginaryNodes == 31)
    assert(K == 4)
  }

  it("should correctly calculate K and N when small tree") {
    val results: TreeMetadata = TreeMetadataRetriever.resolveKAndArraySizeIncludingImaginaryNodes(4);
    val K: Int = results.K
    assert(K == 2)
  }
}
