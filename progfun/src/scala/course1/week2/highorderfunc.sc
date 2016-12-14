// Higher Order Functions

def id(x: Int): Int = x

def sumInts(a: Int, b: Int): Int =
  if (a > b) 0 else a + sumInts(a + 1, b)

def cube(x: Int): Int = x * x * x

def sumCubes(a: Int, b: Int): Int =
  if (a > b) 0 else cube(a) + sumCubes(a + 1, b)

def sum(f: Int => Int, a: Int, b: Int): Int =
  if (a > b) 0
  else f(a) + sum(f, a + 1, b)

assert(sumInts(1, 3) == sum(id, 1, 3))

assert(sumCubes(1, 3) == sum(cube, 1, 3))
