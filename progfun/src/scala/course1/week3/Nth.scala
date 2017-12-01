import course1.week3._

object Nth extends App {
  /**
    * Take an integer n and a list and select the n'th element of the list.
    * @param n the integer.
    * @param xs the list.
    * @tparam T type parameter.
    * @return the n'th element of the list.
    */
  def nth[T](n: Int, xs: List[T]): T =
    if (xs.isEmpty) throw new IndexOutOfBoundsException
    else if (n == 0) xs.head
    else nth(n - 1, xs.tail)

  val list = new Cons(1, new Cons(2, new Cons(3, new Nil)))

  println(nth(2, list))
  println(nth(-1, list))
}
