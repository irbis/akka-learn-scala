package com.github.irbis.scalalearn.digitalsimulation

object MySimulation extends CircuitSimulation {
  override def InverterDelay: Int = 1
  override def AndGateDelay: Int = 3
  override def OrGateDelay: Int = 5
}