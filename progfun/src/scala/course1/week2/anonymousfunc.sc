// Anonymous Functions

def id(x: Int): Int = x

def sumInts(a: Int, b: Int): Int =
  if (a > b) 0 else a + sumInts(a + 1, b)

def cube(x: Int): Int = x * x * x

def sumCubes(a: Int, b: Int): Int =
  if (a > b) 0 else cube(a) + sumCubes(a + 1, b)

def sum(f: Int => Int, a: Int, b: Int): Int =
  if (a > b) 0
  else f(a) + sum(f, a + 1, b)

assert(sumInts(2, 5) == sum(x => x, 2 ,5))

assert(sumCubes(2, 5) == sum(x => x * x * x, 2, 5))
