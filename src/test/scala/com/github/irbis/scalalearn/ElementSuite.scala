package com.github.irbis.scalalearn

import org.scalatest.funsuite.AnyFunSuite
import Element.elem

class ElementSuite extends AnyFunSuite {

  test("elem result should have passed width") {
    val ele = elem('x', 2, 3)
    assert(ele.width == 2)
  }

  test("elem result should have passed width, variant2") {
    val ele = elem('x', 2, 3)
    assertResult(2) {
      ele.width
    }
  }

  // uncomment the following to check how test of expected exception is working
  test("elem result should have passed with IllegalArgumentException") {
    assertThrows[IllegalArgumentException] {
      elem('x', -2, 3)
    }
  }
}
