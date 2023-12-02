package com.github.irbis.scalalearn

import scala.annotation.tailrec

class Rational(n: Int, d: Int) extends Ordered [Rational] { // n and d are parameters of class
  // parameter check - throws IllegalArgumentException in case
  // of require returns 'true'
  require(d != 0)

  // private object parameter
  private val g = gcd(n.abs, d.abs)

  // object parameters are public by default
  val numer: Int = n / g
  val denom: Int = d / g

  // additional constructor, main is in class declaration
  def this(n: Int) = this(n, 1)

  // toString override
  override def toString = s"$numer/$denom"

  // private function definition
  @tailrec
  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)

  // basic math operations with Rational
  def +(that: Rational): Rational =
    new Rational (
      numer * that.denom + that.numer * denom,
      denom * that.denom )

  def *(that: Rational): Rational =
    new Rational(numer * that.numer, denom * that.denom)

  def -(that: Rational): Rational =
    new Rational(
      numer * that.denom - that.numer * denom,
      denom * that.denom )

  def /(that: Rational): Rational =
    new Rational(numer * that.denom, denom * that.numer)

  // basic math operations with Int
  def +(i: Int): Rational =
    new Rational(numer + i * denom, denom)

  def -(i: Int): Rational =
    new Rational(numer - i * denom, denom)

  def *(i: Int): Rational =
    new Rational(numer * i, denom)

  def /(i: Int): Rational =
    new Rational(numer, denom * i)

  // other operations with Rational
  def lessThan(that: Rational): Boolean =
    this.numer * that.denom < that.numer * this.denom

  def max(that: Rational): Rational =
    if (this.lessThan(that)) that else this

  override def compare(that: Rational): Int =
    (this.numer * that.denom) - (that.numer * this.denom)
}
