/** Higher Order Functions */

def id(x: Int): Int = x

def cube(x: Int): Int = x * x * x

def factorial(x: Int): Int =
  if (x == 0) 1 else factorial(x - 1)

/**
  * Take the sum of the integers between a and b.
  * @param a the starting integer.
  * @param b the ending integer.
  * @return the result.
  */
def sumInts(a: Int, b: Int): Int =
  if (a > b) 0 else id(a) + sumInts(a + 1, b)

/**
  * Take the sum of the cubes of all the integers between a and b.
  * @param a the starting integer.
  * @param b the ending integer.
  * @return the result.
  */
def sumCubes(a: Int, b: Int): Int =
  if (a > b) 0 else cube(a) + sumCubes(a + 1, b)

/**
  * Take the sum of the factorials of all the integers between a and b.
  * @param a the starting integer.
  * @param b the ending integer.
  * @return the result.
  */
def sumFactorials(a: Int, b: Int): Int =
  if (a > b) 0 else factorial(a) + sumFactorials(a + 1, b)

/**
  * Take the sum of the function values of all the integers between a and b.
  * @param func a function that takes an argument of type Int and returns a result of type Int.
  * @param a the starting integer.
  * @param b the ending integer.
  * @return the result.
  */
def sum(func: Int => Int, a: Int, b: Int): Int =
  if (a > b) 0
  else func(a) + sum(func, a + 1, b)

assert(sumInts(1, 3) == sum(id, 1, 3))

assert(sumCubes(1, 3) == sum(cube, 1, 3))

assert(sumFactorials(1, 3) == sum(factorial, 1, 3))
