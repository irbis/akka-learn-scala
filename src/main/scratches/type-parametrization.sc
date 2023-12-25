/*

   "leading" - from end to start         <-----------
   "trailing" - from from start to end   ----------->

   leading ::: trailing.reverse

   example
      leading: 4 3 2 1
      trailing: 0 -1 -2 -3

*/

class Queue[+T] private (
              private[this] var leading: List[T],
              private[this] var trailing: List[T]
              ) {

  private def mirror =
    if (leading.isEmpty) {
      while (!trailing.isEmpty) {
        leading = trailing.head :: leading
        trailing = trailing.tail
      }
    }

  def head = {
    mirror
    leading.head
  }

  def tail = {
    mirror
    new Queue(leading.tail, trailing)
  }

  /*
    bottom limiter: "[ T1 >: T2 ]" ~ T2 supertype of T1

         -----------
         |   T2    |
         -----------
             ^
             |
             |
         -----------
         |   T1    |
         -----------

    Usage example:
      class Fruit
      class Apple extends Fruit
      class Orange extends Fruit

      Queue[Apple].enqueue(Apple) => Queue[Apple]
      Queue[Orange].enqueue[Orange] => Queue[Orange]
      Queue[Apple].enqueue(Orange) => Queue[Fruit]
   */
  def enqueue[U >: T](x: U) = new Queue[U](leading, x :: trailing)

  override def toString: String = {
    s"leading: $leading, trailing: $trailing"
  }
}

object Queue {
  def apply[T](xs: T*): Queue[T] = new Queue[T](xs.toList, Nil)
}

val qTest1 = Queue[Int]()
  .enqueue(1)
  .enqueue(2)
  .enqueue(3)
  .enqueue(4)

val qTest2 = qTest1.tail
  .enqueue(5)
  .enqueue(6)
  .enqueue(7)

class Publication(val title: String)
class Book(title: String) extends Publication(title)

object Library {
  val books: Set[Book] =
    Set(
      new Book("Programming in Scala"),
      new Book("Walden")
    )

  def printBookList(info: Book => AnyRef) = {
    for (book <- books) println(info(book))
  }
}

object Customer extends App {
  def getTitle(p: Publication): String = p.title
  Library.printBookList(getTitle)
}

class Person(var firstName: String, val lastName: String)
  extends Ordered[Person] {

  def compare(that: Person) = {
    val lastNameComparison =
      lastName.compareToIgnoreCase(that.lastName)
    if (lastNameComparison != 0)
      lastNameComparison
    else
      firstName.compareToIgnoreCase(this.firstName)
  }

  override def toString: String = firstName + " " + lastName
}

// upper limiter: [T1 <: T2] ~ T1 extends T2
def orderedMergeSort[T <: Ordered[T]](xs: List[T]): List[T] = {
  def merge(xs: List[T], ys: List[T]): List[T] =
    (xs, ys) match {
      case (Nil, _) => ys
      case (_, Nil) => xs
      case (x :: xs1, y :: ys1) =>
        if (x < y) x :: merge(xs1, ys)
        else y :: merge(xs, ys1)
    }

  val n = xs.length / 2
  if (n == 0) xs
  else {
    val (ys, zs) = xs splitAt n
    merge(orderedMergeSort(ys), orderedMergeSort(zs))
  }
}

val people = List(
  new Person("Larry", "Wall"),
  new Person("Anders", "Hejlsberg"),
  new Person("Guido", "van Rossum"),
  new Person("Alan", "Kay"),
  new Person("Yukihiro", "Matsumoto")
)
val sortedPeople = orderedMergeSort(people)