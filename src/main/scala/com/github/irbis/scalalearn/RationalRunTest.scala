package com.github.irbis.scalalearn

object RationalRunTest extends App {
  val half = new Rational(1, 2)
  val third = new Rational(1, 3)

  println(s"$half < $third = ${half < third}")
  println(s"$half > $third = ${half > third}")

  println(1 + half)
}
