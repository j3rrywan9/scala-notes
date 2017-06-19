package course2.week2

abstract class IntSet {
  def incl(x: Int): IntSet
  def contains(x: Int): Boolean
  def union(other: IntSet): IntSet
}

object Empty extends IntSet {
  def incl(x: Int): IntSet = NonEmpty(x, Empty, Empty)
  def contains(x: Int): Boolean = false
  def union(other: IntSet): IntSet = other
}

case class NonEmpty(elem: Int, left: IntSet, right: IntSet) extends IntSet {
  def incl(x: Int): IntSet =
    if (x < elem) NonEmpty(elem, left incl x, right)
    else if (x > elem) NonEmpty(elem, left, right incl x)
    else this

  def contains(x: Int): Boolean =
    if (x < elem) left contains x
    else if (x > elem) right contains x
    else true

  def union(other: IntSet): IntSet = (left union (right union (other))) incl elem
}

object IntSetDemo {
  def main(args: Array[String]) = {
    // Empty contains x = false
    assert((Empty contains 1) == false)
    // (s incl x) contains x = true
    assert((Empty incl 1 contains 1) == true)
    // (s incl x) contains y = s contains y if x != y
    assert(((Empty incl 1) contains 2) == (Empty contains 2))
  }
}
