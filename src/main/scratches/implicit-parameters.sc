class PreferredPromt(val preference: String)
class PreferredDrink(val preference: String)

object Greeter {
  def greet(name: String)(implicit prompt: PreferredPromt, drink: PreferredDrink) = {
    println("Welcome, " + name + ". The system is ready.")
    print("But while you work, ")
    println("why no enjoy a cup of " + drink.preference + "?")
    println(prompt.preference)
  }
}

object MyPrefs {
  implicit val prompt: PreferredPromt = new PreferredPromt("Yes, master> ")
  implicit val drink: PreferredDrink = new PreferredDrink("tea")
}

import MyPrefs._
Greeter.greet("Joe")

def maxListImpPart[T](elements: List[T])
                     (implicit ordering: Ordering[T]): T =
  elements match {
    case List() =>
      throw new IllegalArgumentException("empty list!")
    case List(x) => x
    case x :: rest =>
      val maxRest = maxListImpPart(rest)(ordering)
      if (ordering.gt(x, maxRest)) x
      else maxRest
  }

maxListImpPart(List(1, 5, 10, 3))

maxListImpPart(List(1.5, 5.2, 10.7, 3.1415926))

maxListImpPart(List("one", "two", "three"))

// modified and more common version of maxListImpPart
def maxListImpPartImproved[T : Ordering](elements: List[T]): T =
  elements match {
    case List() =>
      throw new IllegalArgumentException("empty list!")
    case List(x) => x
    case x :: rest =>
      val maxRest = maxListImpPartImproved(rest)
      if (implicitly[Ordering[T]].gt(x, maxRest)) x
      else maxRest
  }

maxListImpPartImproved(List(1, 5, 10, 3))

maxListImpPartImproved(List(1.5, 5.2, 10.7, 3.1415926))

maxListImpPartImproved(List("one", "two", "three"))

