package course1.week2

class Rational(n: Int, d: Int) {
  /** Precondition */
  require(d != 0, "denominator must be non-zero")

  /**
    * Auxiliary constructor.
    * @param n the numerator.
    * @return
    */
  def this(n: Int) = this(n, 1)

  /**
    * Calculates the greatest common divisor of two integers.
    * @param a first integer.
    * @param b second integer.
    * @return the greatest common divisor of a and b.
    */
  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)

  private val g = gcd(n.abs, d.abs)

  val numer = n / g
  val denom = d / g

  def add(that: Rational): Rational =
    new Rational(numer * that.denom + that.numer * denom, denom * that.denom)

  def less(that: Rational): Boolean =
    this.numer * that.denom < that.numer * this.denom

  def max(that: Rational): Rational =
    if (this.less(that)) that else this

  def neg: Rational =
    new Rational(-numer, denom)

  def sub(that: Rational): Rational =
    add(that.neg)

  override def toString = s"$numer/$denom"
}

object Rational extends App {
  val x = new Rational(1, 3)
  val y = new Rational(5, 7)

  println(y.add(y))
  println(x.neg)
  println(x.sub(y))
  println(x.less(y))
  println(x.max(y))
}
