// Currying

// sum is now a function that returns another function of type (Int, Int) => Int
def sum(f: Int => Int): (Int, Int) => Int = {
  def sumF(a: Int, b: Int): Int =
    if (a > b) 0
    else f(a) + sumF(a + 1, b)
  sumF
}

// sumInts is a function of type (Int, Int) => Int
def sumInts = sum(x => x)

sumInts(2, 5)

// sumCubes is a function of type (Int, Int) => Int
def sumCubes = sum(x => x * x * x)

sumCubes(1, 3)

// Multiple parameter lists
def sum2(f: Int => Int)(a: Int, b: Int): Int =
  if (a > b) 0 else f(a) + sum(f)(a + 1, b)

sum2(x => x * x * x)(1, 3)

assert(sumCubes(2, 4) == sum2(x => x * x * x)(2, 4))

def product(f: Int => Int)(a: Int, b: Int): Int =
  if (a > b) 1
  else f(a) * product(f)(a + 1, b)

product(x => x * x)(3, 4)

def fact(n: Int) = product(x => x)(1, n)

assert(product(x => x)(1, 5) == fact(5))

// A more general function which generalizes both sum and product
def mapReduce(f: Int => Int, combine: (Int, Int) => Int, zero: Int)(a: Int, b: Int): Int =
  if (a > b) zero
  else combine(f(a), mapReduce(f, combine, zero)(a + 1,b ))

def sum3(f: Int => Int)(a: Int, b: Int): Int =
  mapReduce(f, (x, y) => x + y, 0)(a, b)

assert(sum2(x => x * x * x)(1,3) == sum3(x => x * x * x)(1, 3))

def product2(f: Int => Int)(a: Int, b: Int): Int =
  mapReduce(f, (x, y) => x * y, 1)(a, b)

def fact2(n: Int) = product2(x => x)(1, n)

assert(fact(5) == fact2(5))
