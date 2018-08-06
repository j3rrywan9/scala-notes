object listfun {
  val nums = List(2, -4, 5, 7, 1)
  val fruits = List("apple", "pineapple", "orange", "banana")

  nums filter (x => x > 0)
  nums filterNot (x => x > 0)
  nums partition (x => x > 0)

  nums takeWhile (x => x > 0)
  nums dropWhile (x => x > 0)
  nums span (x => x > 0)

  val data = List("a", "a", "a", "b", "c", "c", "a")

  /**
    * Pack consecutive duplicates of list elements into sub-lists.
    * @param xs the list.
    * @tparam T
    * @return
    */
  def pack[T](xs: List[T]): List[List[T]] = xs match {
    case Nil => Nil
    case x :: _ =>
      val (first, rest) = xs span (y => y == x)
      first :: pack(rest)
  }

  pack(data)

  /**
    * Produce the run-length encoding of a list.
    * @param xs the list.
    * @tparam T
    * @return
    */
  def encode[T](xs: List[T]): List[(T, Int)] =
    pack(xs) map (ys => (ys.head, ys.length))

  encode(data)

  def concat[T](xs: List[T], ys: List[T]): List[T] =
    (xs foldRight ys)(_ :: _)
}
