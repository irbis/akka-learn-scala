package com.github.irbis.scalalearn.scell

import scala.swing.event.ValueChanged
import swing._

class Model(val height: Int, val width: Int)
  extends Evaluator with Arithmetic {
  case class Cell(row: Int, column: Int) extends Publisher {
    private var f: Formula = Empty

    def formula: Formula = f
    def formula_=(f: Formula) = {
      for (c <- reference(formula)) deafTo(c)
      this.f = f
      for (c <- reference(formula)) listenTo(c)
      value = evaluate(f)
    }

    private var v: Double = 0
    def value: Double = v
    def value_=(w: Double) = {
      if (!(v == w || v.isNaN && w.isNaN)) {
        v = w
        publish(ValueChanged(this))
      }
    }

    reactions += {
      case ValueChanged(_) => value = evaluate(formula)
    }

    override def toString: String = formula match {
      case Textual(s) => s
      case _ => value.toString
    }
  }

  case class ValueChanged(cell: Cell) extends event.Event

  val cells = Array.ofDim[Cell](height, width)

  for(i <- 0 until height; j <- 0 until width)
    cells(i)(j) = new Cell(i, j)
}
