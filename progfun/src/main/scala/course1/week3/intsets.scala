package course1.week3

/**
  * Integer set.
  */
abstract class IntSet {
  /**
    * Check if integer set contains a given integer.
    * @param x the integer.
    * @return
    */
  def contains(x: Int): Boolean

  /**
    * Add a given integer to integer set.
    * @param x the integer.
    * @return
    */
  def incl(x: Int): IntSet

  /**
    * Form the union of two integer sets.
    * @param other the other integer set.
    * @return
    */
  def union(other: IntSet): IntSet
}

/**
  * An object for empty integer set.
  */
object Empty extends IntSet {
  def contains(x: Int): Boolean = false
  def incl(x: Int): IntSet = new NonEmpty(x, Empty, Empty)
  def union(other: IntSet): IntSet = other
  override def toString = "."
}

/**
  * A class for non-empty integer set.
  * @param elem the integer stored in the node.
  * @param left the left subtree.
  * @param right the right subtree.
  */
class NonEmpty(elem: Int, left: IntSet, right: IntSet) extends IntSet {
  def contains(x: Int): Boolean =
    if (x < elem) left contains x
    else if (x > elem) right contains x
    else true

  def incl(x: Int): IntSet =
    if (x < elem) new NonEmpty(elem, left incl x, right)
    else if (x > elem) new NonEmpty(elem, left, right incl x)
    else this

  def union(other: IntSet): IntSet =
    ((left union right) union other) incl elem

  override def toString = "{" + left + elem + right + "}"
}

object IntSets extends App {
  val t1 = new NonEmpty(3, Empty, Empty)
  val t2 = t1 incl 4
}
