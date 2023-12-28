package com.github.irbis.scalalearn.currency

object CurrencyTest extends App {
  val yen = Japan.Yen from US.Dollar * 100
  println(yen)

  val euro = Europe.Euro from yen
  println(euro)

  val dollar = US.Dollar from euro
  println(dollar)
}