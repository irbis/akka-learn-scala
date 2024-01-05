// foreach, grouped and sliding

val xs = List(1, 2, 3, 4, 5)

val git = xs grouped 3
git.next() // <- List(1, 2, 3)
git.next() // <- List(4, 5)

val sit = xs sliding 3
sit.next() // <- List(1, 2, 3)
sit.next() // <- List(2, 3, 4)
sit.next() // <- List(3, 4, 5)

// lazy fibonachi list
def fibFrom(a: Int, b: Int): LazyList[Int] =
  a #:: fibFrom(b, a + b)

val fibs = fibFrom(1, 1).take(7)
fibs.toList

// vector
val vec = scala.collection.immutable.Vector.empty
val vec2 = vec :+ 1 :+ 2
val vec3 = 100 +: vec2
vec3(0)

collection.immutable.IndexedSeq(1, 2, 3)

// immutable queues
val emptyQ = scala.collection.immutable.Queue[Int]()
val has1 = emptyQ.enqueue(1)
val has123 = has1.enqueueAll(List(2, 3))
val (element, has23) = has123.dequeue // returns element and modified queue

// ranges
1 to 3
5 to 14 by 3
1 until 3

// from Scala to Java and back forward
import scala.jdk.CollectionConverters._
import collection.mutable._
val jul: java.util.List[Int] = ArrayBuffer(1, 2, 3).asJava
val buf: Seq[Int] = jul.asScala
val m: java.util.Map[String, Int] = HashMap("abc" -> 1, "hello" -> 2).asJava
