val simpleArray = Array(1,2,3,4,5)

println(simpleArray.mkString(","))

simpleArray(0) = 0
println(simpleArray.mkString(","))

val simpleTuple = (1, 1.0, "testString")
println(simpleTuple._1)
println(simpleTuple._2)
println(simpleTuple._3)

val simpleSet = Set("Hello", "world", "from", "Scala")
println(simpleSet.mkString(" "))

val simpleList = List(1,2,3,4,5)
val secondSimpleList = List(-3, -2, -1, 0)
val thirdSimpleList = secondSimpleList ::: simpleList
println( thirdSimpleList.mkString(" :: ") )

println ( (-4 :: thirdSimpleList).mkString(" :: ") )
println ( (thirdSimpleList :+ 6).mkString(" :: ") )

val simpleMap = Map ( 1 -> "111", 2 -> "222", 3 -> "333")
println ( (simpleMap + (4 -> "4444")).mkString(";") )

-2.0
(2.0).unary_-