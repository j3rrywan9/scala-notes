package course2.week1

object Generators extends App {

  /**
    * A trait that generates random values of type T.
    * @tparam T
    */
  trait Generator[+T] {
    /**
      * An alias for "this".
      */
    self =>

    def generate: T

    def map[S](f: T => S): Generator[S] = new Generator[S] {
      def generate = f(self.generate)
    }

    def flatMap[S](f: T => Generator[S]): Generator[S] = new Generator[S] {
      def generate = f(self.generate).generate
    }
  }

  val integers = new Generator[Int] {
    val rand = new java.util.Random
    def generate = rand.nextInt()
  }

  /**
    * val booleans = integers map { x => x > 0 }
    *
    * val booleans = new Generator[Boolean] { def generate = (x: Int => x > 0)(integers.generate) }
    *
    * Above can be simplified by doing the so called beta reduction.
    *
    * val booleans = new Generator[Boolean] { def generate = integers.generate > 0 }
    */
  val booleans = for (x <- integers) yield x > 0

  /**
    * def pairs[T, U](t: Generator[T], u: Generator[U]) = t flatMap { x => u map { y => (x, y) } }
    *
    * def pairs[T, U](t: Generator[T], u: Generator[U]) = t flatMap { x => new Generator[(T, U)] { def generate = (x, u.generate) } }
    *
    * def pairs[T, U](t: Generator[T], u: Generator[U]) = new Generator[(T, U)] { def generate = (new Generator[(T, U)] { def generate = (t.generate, u.generate) }).generate }
    */
  def pairs[T, U](t: Generator[T], u: Generator[U]) = for {
    x <- t
    y <- u
  } yield (x, y)

  def single[T](x: T): Generator[T] = new Generator[T] {
    def generate = x
  }

  def choose(lo: Int, hi: Int): Generator[Int] = for (x <- integers) yield lo + x % (hi - lo)

  def oneOf[T](xs: T*): Generator[T] = for (index <- choose(0, xs.length)) yield xs(index)
}
