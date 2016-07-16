class Rational(x: Int, y: Int) {
  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)
  def numer = x / gcd(x, y)
  def denom = y / gcd(x, y)

  def less(that: Rational) =
    numer * that.denom < that.numer * denom
}

val x = new Rational(1, 3)
val y = new Rational(5, 7)

x.less(y)
