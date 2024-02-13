package com.github.irbis.scalalearn.scell

trait Arithmetic { this: Evaluator =>
  operations ++= List (
    "add" -> { case List(x, y) => x + y },
    "sub" -> { case List(x, y) => x - y },
    "div" -> { case List(x, y) => x / y },
    "mul" -> { case List(x, y) => x * y },
    "mod" -> { case List(x, y) => x % y },
    "sum" -> { xs => xs.foldLeft(0.0)(_ + _) },
    "prod" -> { xs => xs.foldLeft(1.0)(_ * _)}
  )

}
