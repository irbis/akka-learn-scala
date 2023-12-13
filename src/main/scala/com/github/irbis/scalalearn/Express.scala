package com.github.irbis.scalalearn

object Express extends App {

  private val f = new ExprFormatter

  private val e1 = BinOp("*", BinOp("/", Number(1), Number(2)),
                      BinOp("+", Var("x"), Number(1)))

  private val e2 = BinOp("+", BinOp("/", Var("x"), Number(2)),
                      BinOp("/", Number(1.5), Var("x")))

  private val e3 = BinOp("/", e1, e2)

  private def show(e: Expr) = s"${println(f.format(e))}\n\n"

  for (e <- Array(e1, e2, e3)) show(e)
}
