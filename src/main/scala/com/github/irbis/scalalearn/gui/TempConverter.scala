package com.github.irbis.scalalearn.gui

import scala.swing.{FlowPanel, Frame, Label, MainFrame, SimpleSwingApplication, Swing, TextField}
import scala.swing.event.EditDone

object TempConverter extends SimpleSwingApplication {
  override def top: Frame = new MainFrame {
    title = "Celsius/Fahrenheit Converter"
    object celsius extends TextField { columns = 5 }
    object fahrenheit extends TextField { columns = 6 }

    contents = new FlowPanel {
      contents += celsius
      contents += new Label(" Celsius = ")
      contents += fahrenheit
      contents += new Label(" Fahrenheit")
      border = Swing.EmptyBorder(15, 10, 10, 10)
    }

    listenTo(celsius, fahrenheit)
    reactions += {
      case EditDone(`fahrenheit`) =>
        val f = fahrenheit.text.toInt
        val c = (f - 32) * 5 / 9
        celsius.text = c.toString
      case EditDone(`celsius`) =>
        val c = celsius.text.toInt
        val f = c * 9 / 5 + 32
        fahrenheit.text = f.toString
    }
  }
}
