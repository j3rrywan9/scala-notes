# Course 1 Functional Programming Principles in Scala

## Week 1

### Programming Paradigms

Paradigm: In science, a *paradigm* describes distinct concepts or thought patterns in some scientific discipline.

Main programming paradigms:
* imperative programming
* functional programming
* logic programming

Orthogonal to it:
* object-oriented programming

### Consequences for Programming

If we want to implement high-level concepts following their mathematical theories, there's no place for mutation.

### Functional Programming

* In a *restricted* sense, functional programming (FP) means programming without mutable variables, assignments, loops, and other imperative control structures.
* In a *wider* sense, functional programming means focusing on the functions.
* In particular, functions can be values that are produced, consumed, and composed.
* All this becomes easier in a functional language.

### Evaluation of Function Applications

Applications of parameterized functions are evaluated in a similar way as operators:
1. Evaluate all function arguments, from left to right
2. Replace the function application by the function's right-hand side, and, at the same time
3. Replace the formal parameters of the function by the actual arguments

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

CBN has the advantage that a function argument is not evaluated at all if the corresponding parameter is not used in the evaluation of the function's body.

### CBN, CBV and termination

We have:
* If CBV evaluation of an expression *e* terminates, then CBN evaluation of *e* terminates too.
* The other direction is not true.

### Scala's evaluation strategy

Scala normally uses call-by-value.

But if the type of a function parameter starts with `=>` it uses call-by-name.
```scala
def constOne(x: Int, y: => Int) = x
```

### Conditional Expressions

To express choosing between two alternatives, Scala has a conditional expression `if-else`.

Example:
```scala
def abs(x: Int) = if (x > 0) x else -x
```

### Rewrite rules for Booleans

### Value Definitions

The `def` form is "by-name", its right hand side is evaluated on each use.

There is also a `val` form, which is "by-value".

The right hand side of a `val` definition is evaluated at the point of the definition itself.

Afterwards, the name refers to the value.

### Value Definitions and Termination

The difference between `val` and `def` becomes apparent when the right hand side does not terminate.

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

## Week 2

### Higher Order Functions

Functional languages treat functions as *first-class values*.

Like any other value, a function can be passed as a parameter and returned as a result.

Functions that take other functions as parameters or that return functions as results are called *higher order functions*.

#### Function Types

The type `A => B` is the type of a function that takes an argument of type A and returns a result of type B.

So, `Int => Int` is the type of functions that map integers to integers.

#### Anonymous Functions

Passing functions as parameters leads to the creation of many small functions.
Sometimes it is tedious to have to define (and name) these functions using `def`.

```scala
(x: Int, y: Int) => x + y
```
The type of the parameter can be omitted if it can be inferred by the compiler from the context.

If there are several parameters, they are separated by commas.

#### Anonymous Functions are Syntactic Sugar

### Currying

Generally, function application associates to the left:
```scala
def cube(x: Int): Int = x * x * x

def sum(func: Int => Int): (Int, Int) => Int = {
  def sumFunc(a: Int, b: Int): Int =
    if (a > b) 0
    else func(a) + sumFunc(a + 1, b)
  sumFunc
}

sum(cube)(1, 10) == (sum(cube))(1, 10)
```

### Multiple Parameter Lists

The definition of functions that return functions is so useful in functional programming that there is a special syntax for it in Scala.

### Functions and Data

#### Classes

#### Objects

We call the elements of a class type *objects*.

We create an object by prefixing an application of the constructor of the class with the operator `new`.

#### Methods

One can go further and also package functions operating on a data abstraction in the data abstraction itself.
Such functions are called *methods*.

#### Self Reference

On the inside of a class, the name `this` represents the object on which the current method is executed.

#### Constructors

In Scala, a class implicitly introduces a constructor.
This one is called the *primary constructor* of the class.

## Week 3

### Abstract Classes

Abstract classes can contain members which are missing an implementation.

Consequently, no instances of an abstract class can be created with the operator `new`.

### Packages

Classes and objects are organized in packages.

To place a class or object inside a package, use a package clause at the top of your source file.

### Forms of Imports

```scala
import week3.Rational
import week3.{Rational, Hello}
import week3._
```

### Automatic Imports

Some entities are automatically imported in any Scala program.

These are:
* All members of package `scala`
* All members of package `java.lang`
* All members of the singleton object `scala.Predef`

### Traits

A trait is declared like an abstract class, just with `trait` instead of `abstract class`.

Traits resemble interfaces in Java, but are more powerful because they can contain fields and concrete methods.

### Scala's Class Hierarchy

scala.Any

The base type of all types.

scala.AnyVal

The base type of all primitive types.

scala.AnyRef

The base type of all reference types. Alias of `java.lang.Object`.

scala.Nothing

scala.Null

### Value Parameters

### Type Parameters

### Polymorphism

## Week 4

### Pure Object Orientation

A pure object-orientated language is one in which every value is an object.

If the language is based on classes, this means that the type of each value is a class.

### Functions as Objects

In fact function values are treated as objects in Scala.

The function type `A => B` is just an abbreviation for the class `scala.Function1[A, B]`, which is roughly defined as follows:
```scala
package scala

trait Function1[A, B] {
  def apply(x: A): B
}
```
So functions are objects with `apply` method.

There are also traits `Function2`, `Function3`, ... for functions which take more arguments (currently up to 22).

### Expansion of Function Values

An anonymous function such as
```scala
(x: Int) => x * x
```
is expanded to:
```scala
{ class AnnoFun extends Function1[Int, Int] {
    def apply(x: Int) = x * x
  }
  new AnonFun
}
```
or, shorter, using *anonymous class syntax*:
```scala
new Function1[Int, Int] {
  def apply(x: Int) = x * x
}
```

### Expansion of Function Calls

A function call, such as `f(a, b)`, is expanded to
```scala
f.apply(a, b)
```
So the object-oriented translation of
```scala
val f = (x: Int) => x * x
f(7)
```
would be
```scala
val f = new Function1[Int, Int] {
  def apply(x: Int) = x * x
}
f.apply(7)
```
### Functions and Methods

This is called *eta expansion* in lambda calculus.

### Polymorphism

Two principal forms of polymorphism:
* subtyping
* generics

Interactions between the two concepts:
* bounds
* variance

### Type Bounds

#### Upper Bounds

```scala
def assertAllPos[S <: IntSet](r: S): S = ...
```
Here, `<: IntSet` is an *upper bound* of the type parameter `S`.

It means that `S` can be instantiated only to types that conform to the bound i.e. `IntSet`.

#### Lower Bounds

#### Mixed Bounds

### Covariance

### Decomposition

Scala lets you do type tests and type casts using methods defined in class `Any`:
```scala
def isInstanceOf[T]: Boolean
def asInstanceOf[T]: T
```
These correspond to Java's type tests and casts.
But their use in Scala is discouraged, because there are better alternatives.

### Pattern Matching

#### Case Classes

A *case class* definition is similar to a normal class definition, except that it is preceded by the modifier `case`.
```scala
trait Expr
case class Number(n: Int) extends Expr
case class Sum(e1: Expr, e2: Expr) extends Expr
```
*Pattern matching* is a generalization of `switch` from C/Java to class hierarchies.

It's expressed in Scala using the keyword `match`.
```scala
def eval(e: Expr): Int = e match {
  case Number(n) => n
  case Sum(e1, e2) => eval(e1) + eval(e2)
}
```

### Forms of Patterns

Patterns are constructed from:
* constructors
* variables
* wildcard patterns
* constants

### Lists

Like arrays, lists are **homogeneous**: the elements of a list must all have the same type.

All operations on lists can be expressed in terms of the following three operations:
* `head`: the first element of the list
* `tail`: the list composed of all the elements except the first
* `isEmpty`: `true` if the list is empty, `false` otherwise

## Week 5

### Pairs and Tuples

### Higher-order List Functions

#### Map

#### Filtering

Another common operation on lists is the selection of all elements satisfying a given condition.

#### Reduction of Lists

Another common operation on lists is to combine the elements of a list using a given operator.

## Week 6

### Collection Hierarchy

A common base class of `List` and `Vector` is `Seq`, the class of all *sequences*.

`Seq` itself is a subclass of `Iterable`.

#### Ranges

#### Some more Sequence Operations

### Combinatorial Search and for Comprehensions

### Sets

Sets are another basic abstraction in the Scala collections.

### Sets vs Sequences

The principal differences between sets and sequences are:
1. Sets are unordered; the elements of a set do not have a predefined order in which they appear in the set
2. Sets do not have duplicate elements
3. The fundamental operation on sets is `contains`

### Map

A map of type `Mapp[Key, Value]` is a data structure that associates keys of type `Key` with values of type `Value`.

### Maps are Iterables

Class `Map[Key, Value]` extends the collection type `Iterable[(Key, Value)]`.

Therefore, maps support the same collection operations as other iterables do.

Note that maps extend iterables of key/value *pairs*.

In fact, the syntax key -> value is just an alternative way to write the pair (key, value).

### Maps are Functions

Class `Map[Key, Value]` also extends the function type `Key => Value`, so maps can be used everywhere functions can.

### Querying Map

To query a map without knowing beforehand whether it contains a given key, you can use the `get` operation.

The result of a `get` operation is an `Option` value.

### The `Option` Type

### Decomposing Option

Since options are defined as case classes, they can be decomposed using pattern matching:
```scala
def showCapital(country: String) = capitalOfCountry.get(country) match {
	case Some(capital) => captial
	caes None => "missing data"
}
```

### Sorted and GroupBy
