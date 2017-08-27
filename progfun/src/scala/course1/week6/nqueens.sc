object nqueens {
  /**
    *
    * @param n the number of rows of the chessboard
    * @return a set of solutions, each solution is a list of integers
    */
  def queens(n: Int): Set[List[Int]] = {
    /**
      *
      * @param k the number of queens
      * @return a set of solutions, each solution is a list of integers
      */
    def placeQueens(k: Int): Set[List[Int]] =
      if (k == 0) Set(List())
      else
        for {
          // partial solutions for k - 1 queens
          queens <- placeQueens(k - 1)
          // try all possible columns
          col <- 0 until n
          // check if it doesn't threaten the other queen
          if isSafe(col, queens)
        } yield col :: queens

    placeQueens(n)
  }

  /**
    *
    * @param col column for a new queen
    * @param queens existing solutions
    * @return whether it is safe to put the new queen in the given column
    */
  def isSafe(col: Int, queens: List[Int]): Boolean = {
    val row = queens.length
    val queensWithRow = (row - 1 to 0 by -1) zip queens
    queensWithRow forall {
      case (r, c) => col != c && math.abs(col - c) != row - r
    }
  }

  def show(queens: List[Int]) = {
    val lines =
      for (col <- queens.reverse)
        yield Vector.fill(queens.length)("* ").updated(col, "Q ").mkString
    "\n" + (lines mkString "\n")
  }

  (queens(4) map show) mkString "\n"
}
