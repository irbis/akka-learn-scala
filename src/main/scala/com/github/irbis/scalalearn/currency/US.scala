package com.github.irbis.scalalearn.currency

object US extends CurrencyZone {

  abstract class Dollar extends AbstractCurrency {
    override def designation: String = "USD"
  }

  override type Currency = Dollar
  def make(cents: Long) = new Dollar { val amount = cents }
  val Cent = make(1)
  val Dollar = make(100)
  val CurrencyUnit = Dollar
}

