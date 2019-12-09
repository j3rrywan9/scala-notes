package course1.week2

import scala.annotation.tailrec

object Exercise2 extends App {

  def sum(func: Int => Int, a: Int, b: Int): Int = {
    @tailrec
    def loop(a: Int, accumulator: Int): Int =
      if (a > b) accumulator
      else loop(a + 1, func(a) + accumulator)
    loop(a, 0)
  }

  print(sum(x => x * x, 3, 5))
}
