package course1.week1

import scala.annotation.tailrec

object Exercise extends App {

  def factorial(n: Int): Int = {
    @tailrec
    def loop(accumulator: Int, n: Int): Int =
      if (n == 0) accumulator
      else loop(accumulator * n, n - 1)

    loop(1, n)
  }

  print(factorial(4))
}
