trait Abstract {
  type T
  def transform(x: T): T
  val initial: T
  var current: T
}

class Concrete extends Abstract {
  type T = String
  override def transform(x: String): String = x + x
  override val initial: String = "hi"
  override var current: String = initial
}

abstract class Fruit {
  val v: String // 'v' - value
  def m: String // 'm' - method
}

abstract class Apple extends Fruit {
  val v: String
  val m: String // def -> val
}

abstract class BadApple extends Fruit {
// def v: String // error: unable to val -> def
  def m: String
}

trait RationalTrait {
  val numberArg: Int
  val denomArg: Int
  require(denomArg != 0)
  private val g = gcd(numberArg, denomArg)
  val numer = numberArg / g
  val denom = denomArg / g
  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)

  override def toString: String = s"$numer/$denom"
}

val x = 2
// the following initialization does not work because
// of initial initialization was passed and denomArg = 0
/*
new RationalTrait {
  override val numberArg: Int = 1 * x
  override val denomArg: Int = 2 * x
}
*/

// the following will work because numberArg and denomArg
// initializes before RationalTrait initialization
new {
  val numberArg = 1 * x
  val denomArg = 2 * x
} with RationalTrait

object twoThings extends {
  val numberArg = 2
  val denomArg = 3
} with RationalTrait

class RationalClass(n: Int, d: Int) extends {
  val numberArg = n
  val denomArg = d
} with RationalTrait {
  def +(that: RationalTrait) = new RationalClass(
    numer * that.denom + that.numer * denom,
    denom * that.denom
  )
}

// lazy variables
object Demo {
  val x = { println("initializing x"); "done" }
}
Demo
Demo.x

object LazyDemo {
  lazy val x = { println("initializing x"); "done" }
}
LazyDemo
LazyDemo.x

trait LazyRationalTest {
  val numerArg: Int
  val denomArg: Int
  lazy val numer = numerArg / g
  lazy val denom = denomArg / g

  override def toString: String = s"$numer/$denom"
  private lazy val g = {
    require(denomArg != 0)
    gcd(numerArg, denomArg)
  }
  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)
}

// now it works, because of lazy initialization:
val x = 2
new LazyRationalTest {
  override val numerArg: Int = 1 * x
  override val denomArg: Int = 2 * x
}

class Food
abstract class Animal {
  type SuitableFood <: Food
  def eat(food: SuitableFood): Unit
}

class Grass extends Food
class Cow extends Animal {
  override type SuitableFood = Grass
  override def eat(food: Grass): Unit = {}
}

class Pasture {
  var animals: List[Animal { type SuitableFood = Grass }] = Nil
}
