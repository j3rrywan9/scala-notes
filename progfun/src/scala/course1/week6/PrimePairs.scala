package course1.week6

/**
  * Given a positive integer n, find all pairs of positive integers i and j, with 1 <= j < i < n such that i + j is prime.
  */
object PrimePairs extends App {
  /**
    * Check if an integer is a prime.
    * @param n the integer.
    * @return
    */
  def isPrime(n: Int) = (2 until n) forall (n % _ != 0)

  val n = 7

  val xss = (1 until n) map (i =>
    (1 until i) map (j => (i, j)))
  println(xss)

  /**
    * The previous step gave a sequence of sequences.
    * We can combine all the sub-sequences using foldRight with ++.
    */
  println((xss foldRight Seq[(Int, Int)]())(_ ++ _))

  /**
    * Or, equivalently, we use the built-in method flatten.
    */
  println(xss.flatten)

  /**
    * xs flatMap f = (xs map f).flatten
    */
  val yss = (1 until n) flatMap (i =>
    (1 until i) map (j => (i, j)))
  println(yss)

  println(yss filter (pair => isPrime(pair._1 + pair._2)))

  /**
    * Use for expression.
    */
  val zss = for {
    i <- 1 until n
    j <- 1 until i
    if isPrime(i + j)
  } yield (i, j)
  println(zss)
}