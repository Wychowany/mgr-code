package org.biodatageeks.tbd

import org.scalatest.FunSpec

//level 4:                       15
//level 3:            7                    ?
//level 2:       3        11           19     ?
//level 1:     1   5    9    13     17    ?      ?
//level 0:    0 2 4 6  8 10 12 14 16 18 20  ?  ?   ?


class IndexLevelResolverSpec extends FunSpec {
  it("should compute level's index for index correctly") {
    assert(IndexLevelResolver.levelIndexForIndex(5) == 1)
    assert(IndexLevelResolver.levelIndexForIndex(10) == 5)
    assert(IndexLevelResolver.levelIndexForIndex(13) == 3)
    assert(IndexLevelResolver.levelIndexForIndex(15) == 0)
    assert(IndexLevelResolver.levelIndexForIndex(19) == 2)
  }

  it("should compute index for level index and level") {
    assert(IndexLevelResolver.indexForIndexLevelAndLevel(1, 1) == 5)
    assert(IndexLevelResolver.indexForIndexLevelAndLevel(2, 1) == 9)
    assert(IndexLevelResolver.indexForIndexLevelAndLevel(0, 4) == 15)
    assert(IndexLevelResolver.indexForIndexLevelAndLevel(0, 0) == 0)
    assert(IndexLevelResolver.indexForIndexLevelAndLevel(3, 1) == 13)
    assert(IndexLevelResolver.indexForIndexLevelAndLevel(1,3) == 23)
  }
}


//level 4:                       15
//level 3:            7                    ?
//level 2:       3        11           ?     ?
//level 1:     1   5    9    13     17    ?      ?
//level 0:    0 2 4 6  8 10 12 14 16 ?  ?   ?  ?   ?
