package com.github.irbis.scalalearn.currency

object Japan extends CurrencyZone {
  abstract class Yen extends AbstractCurrency {
    def designation = "JPY"
  }

  override type Currency = Yen
  def make(yen: Long) = new Yen { val amount = yen }
  val Yen = make(1)
  override val CurrencyUnit: Yen = Yen
}
