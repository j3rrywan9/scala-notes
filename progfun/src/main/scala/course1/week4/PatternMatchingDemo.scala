package course1.week4

trait Expr
case class Var(n: Int) extends Expr
case class Sum(e1: Expr, e2: Expr) extends Expr

object PatternMatchingDemo {
  def eval(e: Expr): Int = e match {
    case Var(n) => n
    case Sum(e1, e2) => eval(e1) + eval(e2)
  }

  def show(e: Expr): String = e match {
    case Var(x) => x.toString
    case Sum(l, r) => show(l) + " + " + show(r)
  }

  def main(args: Array[String]) = {
    println(eval(Sum(Var(1), Var(2))))
    println(show(Sum(Var(1), Var(4))))
  }
}
