// lists
val colors = List("red", "blue", "green")
colors.head
colors.tail

//arrays
val fiveInts = new Array[Int](5)
val fiveToOne = Array(5,4,3,2,1)

fiveInts(0) = fiveToOne(4)
fiveInts

// buffers of lists
// !!! mutable !!!
import scala.collection.mutable.ListBuffer
val buf = new ListBuffer[Int]

buf += 1
buf += 2
buf
3 +=: buf
buf.toList

// buffers of arrays
import scala.collection.mutable.ArrayBuffer
val arrBuf = new ArrayBuffer[Int]()
arrBuf += 12
arrBuf += 15

// Strings via StringOps implementation
def hasUpperCase(s: String) = s.exists(_.isUpper)
hasUpperCase("Robert Frost")
hasUpperCase("e e cummings")

// Set and Map
val text = "See Spot run. Run, Spot. Run!"
val wordsArray = text.split("[ !,.]+")

import scala.collection.mutable
val words = mutable.Set.empty[String]
for (word <- wordsArray)
  words += word.toLowerCase
words

val map = mutable.Map.empty[String, Int]
map("hello") = 1
map("there") = 2

def countWords(text: String) = {
  val counts = mutable.Map.empty[String, Int]
  for (rawWord <- text.split("[ ,!.]+")) {
    val word = rawWord.toLowerCase
    val oldCount =
      if (counts.contains(word)) counts(word)
      else 0
    counts += (word -> (oldCount + 1))
  }
  counts
}

countWords("See Spot run! Run, Spot. Run!")

// mutable and immutable collections
val valPeople = Set("Nancy", "Jane")
// valPeople += "Bob" <-- does not work because of val

var varPeople = Set("Nancy", "Jane")
varPeople += "Bob" // <-- works because of var, and object will be created in the background

// collections with different type of elements
val stuff = mutable.Set[Any](42)
stuff += "abcdefg"

// from one collection to other
import scala.collection.immutable.TreeSet
val colors = List("blue", "yellow", "red", "green")
val treeSet = colors to TreeSet

// to List or Array
treeSet.toList
treeSet.toArray