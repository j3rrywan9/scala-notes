package course1.week4

/**
  * Provide an implementation of the abstract class Nat that represents non-negative integers.
  */
abstract class Nat {
  /**
    * Test whether a given natural number is zero.
    * @return
    */
  def isZero: Boolean

  /**
    * Return the natural number before a positive natural number.
    * @return
    */
  def predecessor: Nat

  /**
    * Return the next natural number after the current natural number.
    * @return
    */
  def successor: Nat = new Succ(this)

  /**
    * Addition.
    * @param that
    * @return
    */
  def +(that: Nat): Nat

  /**
    * Subtraction.
    * @param that
    * @return
    */
  def -(that: Nat): Nat
}

/**
  * The object represents number zero.
  */
object Zero extends Nat {
  override def isZero: Boolean = true

  override def predecessor: Nat = throw new Error("0.predecessor")

  override def +(that: Nat): Nat = that

  override def -(that: Nat): Nat = if (that.isZero) this else throw new Error("negative number")
}

class Succ(n: Nat) extends Nat {
  override def isZero: Boolean = false

  override def predecessor: Nat = n

  override def +(that: Nat): Nat = new Succ(n + that)

  override def -(that: Nat): Nat = if (that.isZero) this else n - that.predecessor
}
