// ---------- basic operations ----------
val fruit = List("apples", "oranges", "pears")
val nums = List(1,2,3,4)
val diag3 = List(
  List(1,0,0),
  List(0,1,0),
  List(0,0,1)
)
val empty: List[Nothing] = List()

val longFruit = "apples" :: "oranges" :: "pears" :: "pineapple" :: Nil

def insert(x: Int, xs: List[Int]): List[Int] =
  if (xs.isEmpty || x <= xs.head) x :: xs
  else xs.head :: insert(x, xs.tail)

def isort(xs: List[Int]): List[Int] =
  if (xs.isEmpty) Nil
  else insert(xs.head, isort(xs.tail))

isort(3 :: 4 :: -1 :: 10 :: -255 :: Nil)

val a :: b :: rest = longFruit

// concatination
List(1, 2) ::: List(3, 4, 5)

def append[T](xs: List[T], ys: List[T]): List[T] =
  xs match {
    case List() => ys
    case x :: xs1 => x :: append(xs1, ys)
  }

append(
  10 :: 20 :: 30 :: Nil,
  40 :: 50 :: 60 :: Nil
)

// last and init
val abcde = List('a', 'b', 'c', 'd', 'e')
abcde.last
abcde.init

// reverse
abcde.reverse

def rev[T](xs: List[T]): List[T] = xs match {
  case List() => xs
  case x :: xs1 => rev(xs1) ::: List(x)
}

// drop, take and splitAt
abcde take 2
abcde drop 2
abcde splitAt 2
abcde.indices

def msort[T](less: (T, T) => Boolean)
            (xs: List[T]): List[T] = {
  def merge(xs: List[T], ys: List[T]): List[T] =
    (xs, ys) match {
      case (Nil, _) => ys
      case (_, Nil) => xs
      case (x :: xs1, y :: ys1) =>
        if (less(x, y)) x :: merge(xs1, ys)
        else y :: merge(xs, ys1)
    }

  val n = xs.length / 2
  if (n == 0) xs
  else {
    val (ys, zs) = xs splitAt n
    merge(msort(less)(ys), msort(less)(zs))
  }
}

msort((x: Int, y: Int) => x < y) (List(3, 4, -1, 10, -255))

// map, flatMap and foreach
val words = List("the", "quick", "brown", "fox")
words map (_.length)
words map (_.toList.reverse.mkString)

// map returns new list with operation for every element
words map (_.toList)

// run function for every element and concatanate result
// works only with iterable data types: list, map, ...
words flatMap (_.toList)

// filter, partition, find, takeWhile, dropWhile, span
List(1, 2, 3, 4, 5) filter (_ % 2 == 0)
val (l1, l2) = List(1, 2, 3, 4, 5) partition (_ % 2 == 0)
l1 zip l2

List(1, 2, 3, -4, 5) takeWhile (_ > 0)
List(1, 2, 3, -4, 5) dropWhile (_ > 0)

List("a", "b", "c").foldLeft("d")(_ + _)
List("a", "b", "c").foldRight("d")(_ + _)
