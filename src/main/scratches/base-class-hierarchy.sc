/*
Base class Any defines the following methods:

final def ==(that: Any): Boolean
final def !=(that: Any): Boolean
def equals(that: Any): Boolean
def ##: Int
def hashCode: Int
def toString: String

AnyVal superclass for all types that are values, etc Int, Double, Boolean

AnyRef superclass for all types that are references (alternative to java.lang.Object)

*/

val intValue: Int = 5
println(intValue.##)
println(intValue.hashCode)

/*
Null - extends of AnyRef to have null

Nothing - subtype of any class (including AnyVal)
*/

def divide(x: Int, y: Int): Int =
  if (y != 0) x / y
  else sys.error("Division by zero!") // main scenario to use Nothing

val res1 = divide(4, 2) // returns Int


// Define class-value
// - one parameter
// only def inside class
class Dollars(val amount: Int) extends AnyVal {
  override def toString: String = "$" + amount
}

val money = new Dollars(10000)

// away from mono culture types
// instead of
def title(text: String, anchor: String, style: String): String =
  s"<a id='$anchor'><h1 class='$style'>$text</h1></a>"

// define 4 class values
class Anchor(val value: String) extends AnyVal
class Style(val value: String) extends AnyVal
class Text(val value: String) extends AnyVal
class Html(val value: String) extends AnyVal

// and method that uses them
def title(text: Text, anchor: Anchor, style: Style): Html =
  new Html(
    s"<a id='${anchor.value}'>" +
      s"<h1 class='${style.value}'>" +
      text.value +
    "</h1><a>")
