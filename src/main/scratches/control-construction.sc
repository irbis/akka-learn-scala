import java.io.{File, PrintWriter}

val filesHere = (new java.io.File(
  "/Users/Oleksii_Nadtochyi/Workspaces/" +
    "study-and-tests/akka-learn-scala/" +
    "src/main/scala/com/github/irbis/akkalearnapp/")).listFiles

val scalaSourceFiles = for (file <- filesHere if file.getName.endsWith(".scala"))
  yield file.getName

scalaSourceFiles.foreach(println)

def makeRowSeq(row: Int) =
  for (col <- 1 to 10) yield {
    val prod = (row * col).toString
    val padding = " " * (4 - prod.length)
    padding + prod
  }

def makeRow(row: Int) = makeRowSeq(row).mkString

def multiTable() = {
  val tableSeq = for (row <- 1 to 10) yield makeRow(row)

  tableSeq.mkString("\n")
}

println(multiTable())

// functional: literal and valued
var increase = (x: Int) => x + 1
increase(10)

val someNumbers = List(-11, -10, -5, 0, 5, 10)
someNumbers.filter( _ > 0)

def sum(a: Int, b: Int, c: Int) = a + b + c

val sum12n = sum(1, 2, _: Int)

sum12n(4)

val sumAll = sum _
sumAll(1,2,3)

def echo(args: String*) =
  for (arg <- args) println(arg)

val seq = Seq("What's", "up", "doc")
echo(seq: _*)

def bang(x: Int): Int =
  if (x == 0) throw new Exception("bang!")
  else bang(x - 1)

try {
  bang(5)
} catch {
  case _ : Throwable => None
}

// control abstractions

object FileMatcher {
  private def filesHere = (new java.io.File(".")).listFiles()

  def filesEnding(query: String) =
    for (file <- filesHere; if file.getName.endsWith(query))
      yield file

  def filesContaining(query: String) = // very similar with the previous one
    for (file <- filesHere; if file.getName.contains(query)) // except usage of contains
      yield file

  def filesRegex(query: String) = // again, very similar with the previous one
    for (file <- filesHere; if file.getName.matches(query))
      yield file
}

// alternative object
object FileMatcherAlt {
  private def filesHere = (new java.io.File(".")).listFiles()

  def filesMatching(query: String,
                   matcher: (String, String) => Boolean) = {
    for (file <- filesHere; if matcher(file.getName, query))
      yield file
  }

  def filesEnding(query: String) =
    filesMatching(query, _.endsWith(_))

  def filesContaing(query: String) =
    filesMatching(query, _.contains(_))

  def filesRegexp(query: String) =
    filesMatching(query, _.matches(_))
}

object FileMatcherAltShort {
  private def filesHere = (new java.io.File(".")).listFiles()

  def filesMatching(matcher: String => Boolean) = {
    for (file <- filesHere; if matcher(file.getName))
      yield file
  }

  def filesEnding(query: String) =
    filesMatching(_.endsWith(query))

  def filesContaing(query: String) =
    filesMatching(_.contains(query))

  def filesRegexp(query: String) =
    filesMatching(_.matches(query))
}

// curring
def plainOldSum(x: Int, y: Int) = x + y
plainOldSum(1, 2)

def curriedSum(x: Int)(y: Int) = x + y
curriedSum(1)(2)

val onePlus = curriedSum(1)_
onePlus(2)

def withPrintWriter(file: File, op: PrintWriter => Unit) = {
  val writer = new PrintWriter(file)
  try {
    op(writer)
  } finally {
    writer.close()
  }
}

withPrintWriter (
  new File("/Users/Oleksii_Nadtochyi/Library/Application Support/JetBrains/IntelliJIdea2023.2/scratches/date.txt"),
  writer => writer.println(new java.util.Date)
)

def withPrintWriterFuncLiteral(file: File)(op: PrintWriter => Unit) = {
  val writer = new PrintWriter(file)
  try {
    op(writer)
  } finally {
    writer.close()
  }
}

val file = new File("/Users/Oleksii_Nadtochyi/Library/Application Support/JetBrains/IntelliJIdea2023.2/scratches/date1.txt")
withPrintWriterFuncLiteral(file) { writer =>
  writer.println(new java.util.Date)
}

var assertionEnabled = true
def byNameAssert(predicate: => Boolean) =  // by name parameter
  if (assertionEnabled && !predicate) // predicate calculates during this line instead of function call
    throw new AssertionError

byNameAssert(5 > 3)

assertionEnabled = false
byNameAssert( 3 / 0 == 0 ) // no divide by zero exception