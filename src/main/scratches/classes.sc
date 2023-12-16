class Rational(n: Int, d: Int) { // n and d are parameters of class
  // parameter check - throws IllegalArgumentException in case
  // of require returns 'true'
  require(d != 0)

  // private object parameter
  private val g = gcd(n.abs, d.abs)

  // object parameters are public by default
  val numer: Int = n / g
  val denom: Int = d / g

  // additional constructor, main is in class declaration
  def this(n: Int) = this(n, 1)

  // toString override
  override def toString = s"$numer/$denom"

  // private function definition
  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)

  // basic math operations with Rational
  def +(that: Rational): Rational =
    new Rational (
      numer * that.denom + that.numer * denom,
      denom * that.denom )

  def *(that: Rational): Rational =
    new Rational(numer * that.numer, denom * that.denom)

  def -(that: Rational): Rational =
    new Rational(
      numer * that.denom - that.numer * denom,
      denom * that.denom )

  def /(that: Rational): Rational =
    new Rational(numer * that.denom, denom * that.numer)

  // basic math operations with Int
  def +(i: Int): Rational =
    new Rational(numer + i * denom, denom)

  def -(i: Int): Rational =
    new Rational(numer - i * denom, denom)

  def *(i: Int): Rational =
    new Rational(numer * i, denom)

  def /(i: Int): Rational =
    new Rational(numer, denom * i)

  // other operations with Rational
  def lessThan(that: Rational) =
    this.numer * that.denom < that.numer * this.denom

  def max(that: Rational) =
    if (this.lessThan(that)) that else this
}

// Tests and use-case scenarios

val a = new Rational(1, 3)
val b = new Rational(5, 7)

val oneHalf = new Rational(1, 2)
val twoThirds = new Rational(2, 3)

// overloaded operations
oneHalf + twoThirds
oneHalf * twoThirds

// fields are public by default and available
a.numer
a.denom

// ".method" can be omitted - any method can looks like an operation
b lessThan a
a max b

// additional constructor usage
val c = new Rational(3)

val d = new Rational(66, 42)

// overloaded operation with arguments differs than Rational
a * 2

// Неявное преобразование: this implicit declaration has to
// be added in an execution scope to allow 2 * a
implicit def intToRational(x: Int): Rational = new Rational(x)

2 * a