abstract class Expr
case class Var(name: String) extends Expr
case class Number(num: Double) extends Expr
case class UnOp(operator: String, arg: Expr) extends Expr
case class BinOp(operator: String, left: Expr, right: Expr) extends Expr

val v = Var("x")
val op = BinOp("+", Number(1.0), v)

v.name
op.left

print(op)
op.right == Var("x")

op.copy(operator = "-")

def simplifyTop(expr: Expr): Expr = expr match {
  case UnOp("-", UnOp("-", e)) => e
  case BinOp("+", e, Number(0)) => e
  case BinOp("*", e, Number(1)) => e
  case _ => expr
}

simplifyTop(UnOp("-", UnOp("-", Var("x"))))

val (a, b, c) = ("a", "b", "c")
println(a)
println(b)
println(c)

def simplifyAdd(e: Expr) = e match {
  case BinOp("+", x, y) if x == y =>
    BinOp("*", x, Number(2))
  case _ => e
}

val capitals = Map("France" -> "Paris", "Japan" -> "Tokio")
for ((country, city) <- capitals)
  println(s"Capital of $country is $city")

val fruits = List(Some("apple"), None, Some("orange"))
for (Some(fruit) <- fruits) println(fruit)