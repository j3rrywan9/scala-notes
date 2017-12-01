package course1.week5

object ParameterizedMergeSort extends App {
  /**
    * Merge sort a list of T.
    * @param xs the list of T.
    * @param ord
    * @tparam T
    * @return
    */
  def mergeSort[T](xs: List[T])(implicit ord: Ordering[T]): List[T] = {
    val n = xs.length / 2
    if (n == 0) xs
    else {
      /**
        * Merge two sorted sub-lists of T into a single sorted list of T.
        * @param xs the first sorted list of T.
        * @param ys the second sorted list of T.
        * @return
        */
      def merge(xs: List[T], ys: List[T]): List[T] = (xs, ys) match {
        case (Nil, ys) => ys
        case (xs, Nil) => xs
        case (x :: xs1, y :: ys1) =>
          if (ord.lt(x, y)) x :: merge(xs1, ys)
          else y :: merge(xs, ys1)
      }

      val (first, second) = xs splitAt(n)
      merge(mergeSort(first), mergeSort(second))
    }
  }

  val nums = List(2, -4, 5, 7, 1)
  val fruits = List("apple", "pineapple", "orange", "banana")

  println("Before sorting: " + nums.mkString("(", ", ", ")"))
  println("After sorting: " + mergeSort(nums).mkString("(", ", ", ")"))
  println("Before sorting: " + fruits.mkString("(", ", ", ")"))
  println("After sorting: " + mergeSort(fruits).mkString("(", ", ", ")"))
}
