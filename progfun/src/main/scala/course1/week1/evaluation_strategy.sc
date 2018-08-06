// Not every expression reduce to a value in a definite number of steps
def loop: Int = loop

// loop

// Under CBV
def first(x: Int, y: Int) = x
// first(1, loop)

// Under CBN
def constOne(x: Int, y: => Int) = x
constOne(1 + 2, loop)
