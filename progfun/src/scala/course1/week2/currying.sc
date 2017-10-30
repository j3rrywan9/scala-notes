/** Currying */

def factorial(x: Int): Int =
  if (x == 0) 1
  else factorial(x - 1)

// Let's rewrite
//
// def sum(func: Int => Int, a: Int, b: Int): Int =
//   if (a > b) 0
//   else func(a) + sum(func, a + 1, b)
//
// as follows.
// sum is now a function that returns another function of type "(Int, Int) => Int"
def sum(func: Int => Int): (Int, Int) => Int = {
  def sumFunc(a: Int, b: Int): Int =
    if (a > b) 0
    else func(a) + sumFunc(a + 1, b)
  sumFunc
}

/** sumInts is a function of type "(Int, Int) => Int" */
def sumInts = sum(x => x)

assert(sumInts(2, 5) == sum(x => x)(2, 5))

/** sumCubes is a function of type "(Int, Int) => Int" */
def sumCubes = sum(x => x * x * x)

sumCubes(1, 3)

/** sumFactorials is a function of type "(Int, Int) => Int" */
def sumFactorials = sum(factorial)

sumFactorials(1, 3)

/** Multiple parameter lists */
def sum2(func: Int => Int)(a: Int, b: Int): Int =
  if (a > b) 0
  else func(a) + sum(func)(a + 1, b)

sum2(x => x * x * x)(1, 3)

assert(sumCubes(2, 4) == sum2(x => x * x * x)(2, 4))

/**
  * Calculates the product of the values of a function for the integers on a given interval.
  * @param func the function.
  * @param a starting integer.
  * @param b ending integer.
  * @return the result.
  */
def product(func: Int => Int)(a: Int, b: Int): Int =
  if (a > b) 1
  else func(a) * product(func)(a + 1, b)

product(x => x * x)(3, 4)

/** Write factorial in terms of product */
def fact(n: Int) = product(x => x)(1, n)

assert(product(x => x)(1, 5) == fact(5))

/**
  * Generalizes both sum and product.
  * @param func the mapping function.
  * @param combine the combining function.
  * @param zero the unit value.
  * @param a starting integer.
  * @param b ending integer.
  * @return the result.
  */
def mapReduce(func: Int => Int, combine: (Int, Int) => Int, zero: Int)(a: Int, b: Int): Int =
  if (a > b) zero
  else combine(func(a), mapReduce(func, combine, zero)(a + 1, b))

/** Write sum in terms of mapReduce */
def sum3(func: Int => Int)(a: Int, b: Int): Int =
  mapReduce(func, (x, y) => x + y, 0)(a, b)

assert(sum2(x => x * x * x)(1, 3) == sum3(x => x * x * x)(1, 3))

/** Write product in terms of mapReduce */
def product2(func: Int => Int)(a: Int, b: Int): Int =
  mapReduce(func, (x, y) => x * y, 1)(a, b)

assert(product(x => x)(3, 4) == product2(x => x)(3, 4))

/** Write factorial in terms of mapReduce */
def fact2(n: Int) = product2(x => x)(1, n)

assert(fact(5) == fact2(5))
