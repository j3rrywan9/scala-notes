// Currying

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

// sumInts is a function of type "(Int, Int) => Int"
def sumInts = sum(x => x)

assert(sumInts(2, 5) == sum(x => x)(2, 5))

// sumCubes is a function of type "(Int, Int) => Int"
def sumCubes = sum(x => x * x * x)

sumCubes(1, 3)

// sumFactorials is a function of type "(Int, Int) => Int"
def sumFactorials = sum(factorial)

sumFactorials(1, 3)

// Multiple parameter lists
def sum2(func: Int => Int)(a: Int, b: Int): Int =
  if (a > b) 0
  else func(a) + sum(func)(a + 1, b)

sum2(x => x * x * x)(1, 3)

assert(sumCubes(2, 4) == sum2(x => x * x * x)(2, 4))

// A function that calculates the product of the values of a function for the points on a given interval
def product(func: Int => Int)(a: Int, b: Int): Int =
  if (a > b) 1
  else func(a) * product(func)(a + 1, b)

product(x => x * x)(3, 4)

// Write factorial in terms of product
def fact(n: Int) = product(x => x)(1, n)

assert(product(x => x)(1, 5) == fact(5))

// A more general function which generalizes both sum and product
def mapReduce(func: Int => Int, combine: (Int, Int) => Int, zero: Int)(a: Int, b: Int): Int =
  if (a > b) zero
  else combine(func(a), mapReduce(func, combine, zero)(a + 1, b))

def sum3(func: Int => Int)(a: Int, b: Int): Int =
  mapReduce(func, (x, y) => x + y, 0)(a, b)

assert(sum2(x => x * x * x)(1, 3) == sum3(x => x * x * x)(1, 3))

def product2(func: Int => Int)(a: Int, b: Int): Int =
  mapReduce(func, (x, y) => x * y, 1)(a, b)

def fact2(n: Int) = product2(x => x)(1, n)

assert(fact(5) == fact2(5))
