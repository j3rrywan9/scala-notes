// Higher Order Functions

def id(x: Int): Int = x

def cube(x: Int): Int = x * x * x

def factorial(x: Int): Int =
  if (x == 0) 1 else factorial(x - 1)

def sumInts(a: Int, b: Int): Int =
  if (a > b) 0 else a + sumInts(a + 1, b)

def sumCubes(a: Int, b: Int): Int =
  if (a > b) 0 else cube(a) + sumCubes(a + 1, b)

def sumFactorials(a: Int, b: Int): Int =
  if (a > b) 0 else factorial(a) + sumFactorials(a + 1, b)

// The type "Int => Int" is the type of a function "func" that take an argument of type Int and returns a result of type Int
def sum(func: Int => Int, a: Int, b: Int): Int =
  if (a > b) 0
  else func(a) + sum(func, a + 1, b)

assert(sumInts(1, 3) == sum(id, 1, 3))

assert(sumCubes(1, 3) == sum(cube, 1, 3))

assert(sumFactorials(1, 3) == sum(factorial, 1, 3))
