package com.github.irbis.scalalearn

import scala.io.Source

object LongLines {

  private def processFile(filename: String, width: Int): Unit = {

    def processLine(line: String): Unit = {
      if (line.length > width)
        println(s"$filename: ${line.trim}")
    }

    val source = Source.fromFile(filename)
    for (line <- source.getLines())
      processLine(line)
  }

  def main(args: Array[String]): Unit = {
    val width = args(0).toInt

    println("----- Start processing -----")

    for (arg <- args.drop(1))
      LongLines.processFile(arg, width)

    println("----- Stop processing -----")
  }

}
