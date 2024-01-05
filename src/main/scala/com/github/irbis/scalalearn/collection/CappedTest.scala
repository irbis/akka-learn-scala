package com.github.irbis.scalalearn.collection

object CappedTest extends App {
  val cap = Capped(1, 2, 3)

  println(cap.take(2))
  println(cap.filter(x => x % 2 == 1))
  println(cap.map(x => x * x))
  println(List(1, 2, 3, 4, 5).to(Capped))

}
