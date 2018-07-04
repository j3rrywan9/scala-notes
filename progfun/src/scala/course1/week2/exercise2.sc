object exercise2 {
  def sum(func: Int => Int, a: Int, b: Int) = {
    def loop(a: Int, accumulator: Int): Int =
      if (a > b) accumulator
      else loop(a + 1, func(a) + accumulator)
    loop(a, 0)
  }

  sum(x => x * x, 3, 5)
}
