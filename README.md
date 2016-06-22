# Coursera Scala Specialization Notes

## Course 1 Week 1

If we want to implement high-level concepts following their mathematical theories, there's no place for mutation.

Evaluation of Function Applications

* Evaluate all funciton arguments, from left to right
* Replace the function application by the funciton's right-hand side, and, at the same time
* Replace the formal parameters of the fuction by the actual arguments

### The substitution model

This scheme of expression evaluation is called the *substitution model*.

The idea underlying this model is that all evaluation does is *reduce an expression to a value*.

It can be applied to all expressions, as long as they have no side effects.

The substitution model is formalized in the lambda calculus, which gives a foundation for functional programming.

### Call-by-name and call-by-value

Both strategies reduce to the same final value as long as
* the reduced expression consists of pure functions, and
* both evaluations terminate

CBV has the advantage that every function argument is evaluated only once.

CBN has the advantage that a function argument is not evaluated at all if the corresponding parameter is not used in the
evaluation of the function's body.

If CBV evaluation of an expression *e* terminates, then CBN evaluation of *e* terminates too.

The other direction is not true.

Scala normally uses call-by-value.

But if the type of a function parameter starts with `=>` it uses call-by-name.

### Rewrite rules for Booleans

### Value Definitions

The `def` form is "by-name", its right hand side is evaluated on each use.

There is also a `val` form, which is "by-value".
The right hand side of a `val` definition is evaluated at the point of the definition itself.
Afterwards, the name refers to the value.

Recursive functions need an explicit return type in Scala.

For non-recursive functions, the return type is optional.

### Blocks in Scala

A block is delimited by curly braces.

The last statement of a block is an expression defines its value.

Blocks are themselves expressions; a block may appear everywhere an expression can.

The definitions inside a block are only visible from within the block.

The definitions inside a block *shadow* definitions of the same names outside the block.

### Semicolons

In Scala, semicolons at the end of lines are in most cases optional.

On the other hand, if there are more than one statements on a line, they need to be separated by semicolons:
```scala
val y = x + 1; y * y
```

### Tail Recursion

What's the difference between these two sequences?
```scala
def gcd(a: Int, b: Int): Int =
  if (b == 0) a else gcd(b, a % b)
```

```scala
def factorial(n: Int): Int =
  if (n == 0) 1 else n * factorial(n - 1)
```
If a function calls itself as its last action, the function's stack frame can be reused.
This is called *tail recursion*.

## Course 1 Week 2

### Higher Order Functions

Functional languages treat functions as *first-class values*.

Like any other value, a function can be passed as a parameter and returned as a result.

Functions that take other functions as parameters or that return functions as results are called *higher order
functions*.

#### Function Types

The type `A => B` is the type of a function that takes an argument of type A and returns a result of type B.

So, `Int => Int` is the type of functions that map integers to integers.

#### Anonymous Functions

```scala
(x: Int, y: Int) => x + y
```
The type of the parameter can be omitted if it can be inferred by the compiler from the context.

### Currying

Generally, function application associates to the left:
```scala
sum(cube)(1, 10) == (sum(cube))(1, 10)
```
