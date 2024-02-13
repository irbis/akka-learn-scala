package com.github.irbis.scalalearn.scell

import swing._
object SCellApp extends SimpleSwingApplication {

  override def top: Frame = new MainFrame {
    title = "ScalaSheet"
    contents = new Spreadsheet(100, 26)
  }

}
