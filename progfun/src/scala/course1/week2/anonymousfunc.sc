// Anonymous Functions

def id(x: Int): Int = x

def cube(x: Int): Int = x * x * x

def sumInts(a: Int, b: Int): Int =
  if (a > b) 0 else id(a) + sumInts(a + 1, b)

def sumCubes(a: Int, b: Int): Int =
  if (a > b) 0 else cube(a) + sumCubes(a + 1, b)

def sum(func: Int => Int, a: Int, b: Int): Int =
  if (a > b) 0
  else func(a) + sum(func, a + 1, b)

assert(sumInts(2, 5) == sum(x => x, 2 ,5))

assert(sumCubes(2, 5) == sum(x => x * x * x, 2, 5))
