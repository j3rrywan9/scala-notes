package course1.week5

object MergeSort extends App {
  /**
    * Merge sort a list of integers.
    * @param xs the list of integers.
    * @return
    */
  def mergeSort(xs: List[Int]): List[Int] = {
    val n = xs.length / 2
    if (n == 0) xs
    else {
      /**
        * Merge two sorted sub-lists of integers into a single sorted list of integers.
        * @param xs the first sorted list of integers.
        * @param ys the second sorted list of integers.
        * @return
        */
      def merge(xs: List[Int], ys: List[Int]): List[Int] = (xs, ys) match {
        case (Nil, ys) => ys
        case (xs, Nil) => xs
        case (x :: xs1, y :: ys1) =>
          if (x < y) x :: merge(xs1, ys)
          else y :: merge(xs, ys1)
      }

      val (first, second) = xs splitAt(n)
      merge(mergeSort(first), mergeSort(second))
    }
  }

  val nums = List(2, -4, 5, 7, 1)
  println("Before sorting: " + nums.mkString("(", ", ", ")"))
  println("After sorting: " + mergeSort(nums).mkString("(", ", ", ")"))
}
