package org.biodatageeks.tbd

import org.scalatest.FunSpec


//level 4:                       15
//level 3:            7                    ?
//level 2:       3        11           19     ?
//level 1:     1   5    9    13     17    ?      ?
//level 0:    0 2 4 6  8 10 12 14 16 18 20  ?  ?   ?


class LevelResolverSpec extends FunSpec {
  it("should compute level correctly") {
    assert(LevelResolver.levelForIndex(0) == 0)
    assert(LevelResolver.levelForIndex(10) == 0)
    assert(LevelResolver.levelForIndex(5) == 1)
    assert(LevelResolver.levelForIndex(7) == 3)
    assert(LevelResolver.levelForIndex(13) == 1)
    assert(LevelResolver.levelForIndex(19) == 2)
    assert(LevelResolver.levelForIndex(15) == 4)
  }
}
