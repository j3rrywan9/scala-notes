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

An anonymous function `(x1: T1, ..., xn: Tn) => E` can always be expressed using `def` as follows:
```scala
def f(x1: T1, ..., xn: Tn) = E; f
```
where `f` is an arbitrary, fresh name(that's not ye used in the program).
One can therefore say that anonymous functions are `syntactic sugar`.

### Currying

Functions may define multiple parameter lists.
When a function is called with a fewer number of parameter lists, then this will yield a function taking the missing parameter lists as its arguments.

Currying transforms a function that takes multiple parameter lists into a chain of functions, each taking a single parameter.

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

For example, the following definition of `sum` is equivalent to the one with the nested `sumFunc` function, but shorter:
```scala
def sum(f: Int => Int)(a: Int, b: Int): Int =
  if (a > b) 0 else f(a) + sum(f)(a + 1, b)
```

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

The Scala compiler will compile any code you place in the class body, which isn't part of a field or a method definition, into the primary constructor.

#### Auxiliary Constructors

In Scala, constructors other than the primary constructor are called *auxiliary constructors*.

## Week 3

### Abstract Classes

Abstract classes can contain members which are missing an implementation.

Consequently, no instances of an abstract class can be created with the operator `new`.

### Base Classes and Subclasses

In Scala, any user-defined class extends another class.

If no superclass is given, the standard class `Object` in the Java package `java.lang` is assumed.

### Programs

It is possible to create standalone applications in Scala.

Each such application contains an object with a `main` method.

### Packages

Classes and objects are organized in packages.

To place a class or object inside a package, use a package clause at the top of your source file.

### Forms of Imports

```scala
import week3.Rational
import week3.{Rational, Hello}
import week3._
```
The first two forms are called `named imports`.

The last form is called a `wildcard import`.

You can import from either a package or an object.

### Automatic Imports

Some entities are automatically imported in any Scala program.

These are:
* All members of package `scala`
* All members of package `java.lang`
* All members of the singleton object `scala.Predef`

### Traits

In Java, as well as in Scala, a class can only have one superclass.

A trait is declared like an abstract class, just with `trait` instead of `abstract class`.

Classes, objects and traits can inherit from at most one class but arbitrary many traits.

Traits resemble interfaces in Java, but are more powerful because they can contain fields and concrete methods.

Once a trait is defined, it can be *mixed in* to a class using either the `extends` or `with` keyword.

If you wish to mix a trait into a class that explicitly extends a superclass, you use `extends` to indicate the superclass and `with` to mix in the trait.
If you want to mix in multiple traits, you add more `with` clauses.

### Scala's Class Hierarchy

* `scala.Any`

The base type of all types.

* `scala.AnyVal`

The base type of all primitive types.

* `scala.AnyRef`

Class **AnyRef** is the root class of all *reference types*.
All types except the value types descend from this class.
Alias of `java.lang.Object`.

* `scala.Nothing`

**Nothing** is a subtype of every other type (including scala.Null); there exist *no instances* of this type.

* `scala.Null`

**Null** is a subtype of all reference types; its only instance is the **null** reference.

### Value Parameters

### Type Parameters

Type parameters are written in square brackets.

### Types and Evaluation

Type parameters do not affect evaluation in Scala.

We can assume that all type parameters and type arguments are removed before evaluating the program.

This is also called `type erasure`.

### Polymorphism

Polymorphism means that a function type comes "in many forms".

In programming it means that
* the function can be applied to arguments of many types
* the type can have instances of many types

We have seen two principal forms of polymorphism:
* subtyping: instances of a subclass can be passed to a base class
* generics: instances of a function or class are created by type parameterization

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
{ class AnonFun extends Function1[Int, Int] {
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

Generally, the notation
* `S <: T` means: `S` is a subtype of `T`, and
* `S >: T` means: `S` is a supertype of `T`

#### Upper Bounds

```scala
def assertAllPos[S <: IntSet](r: S): S = ...
```
Here, `<: IntSet` is an *upper bound* of the type parameter `S`.

It means that `S` can be instantiated only to types that conform to the bound i.e. `IntSet`.

#### Lower Bounds

#### Mixed Bounds

Finally, it is also possible to mix a lower bound with an upper bound.

### Covariance

### Variance

Roughly speaking, a type that accepts mutations of its elements should not be covariant.

But immutable types can be covariant, if some conditions on methods are met.

### Definition of Variance

Scala lets you declare the variance of a type by annotating the type parameter:
```scala
class C[+T] { ... }  // C is covariant

class C[-T] { ... }  // C is contravariant

class C[T] { ... }  // C is nonvariant
```

### Function Trait Declaration

So functions are *contravariant* in their argument type(s) and *covariant* in their result type.

This lead to the following revised definition of the `Function1` trait:
```scala
trait Function1[-T, +U] {
  def apply(x: T): U
}
```

### Variance Check

### Making Classes Covariant 

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
It also implicitly defines companion objects with `apply` methods.

So you can write `Number(1)` instead of `new Number(1)`.

#### Pattern Matching

*Pattern matching* is a generalization of `switch` from C/Java to class hierarchies.

It's expressed in Scala using the keyword `match`.
```scala
def eval(e: Expr): Int = e match {
  case Number(n) => n
  case Sum(e1, e2) => eval(e1) + eval(e2)
}
```

#### Match Syntax

Rules:
* `match` is followed by a sequence of `case`s
* Each `case` associates an *expression* `expr` with a *pattern* `pat`
* A `MatchError` exception is thrown if no pattern matches the value of the selector

#### Forms of Patterns

Patterns are constructed from:
* constructors
* variables
* wildcard patterns
* constants

#### Evaluating Match Expressions

The whole match expression is rewritten to the right-hand side of the first case where the pattern matches the selector.

References to pattern variables are replaced by the corresponding parts in the selector.

### Lists

#### The List Type

Like arrays, lists are **homogeneous**: the elements of a list must all have the same type.

The type of a list that has elements of type `T` is written `List[T]`.

#### Operations on Lists

All operations on lists can be expressed in terms of the following three operations:
* `head`: the first element of the list
* `tail`: the list composed of all the elements except the first
* `isEmpty`: `true` if the list is empty, `false` otherwise

#### List Patterns

It is also possible to decompose lists with pattern matching.

#### List Methods

### Pairs and Tuples

The pair consisting of `x` and `y` is written `(x, y)` in Scala.
```scala
val pair = ("answer", 42)
```
The type of `pair` above is `(String, Int)`.

Pairs can also be used as patterns:
```scala
val (lable, value) = pair
```
This works analogously for tuples with more than two elements.

#### Translation of Tuples

#### Parameterization with Ordered

There is already a class in the standard library that represents orderings.
```scala
scala.math.Ordering[T]
```
provides ways to compare elements of type `T`.

#### Implicit Parameters

The compiler will figure out the right implicit to pass based on the demanded type.

#### Rules for Implicit Parameters

### Higher-order List Functions

#### Applying a Function to Elements of a List

A common operation is to transform each element of a list and then return the list of results.

#### Map

This scheme can be generalized to the method `map` of the `List` class.

#### Filtering

Another common operation on lists is the selection of all elements satisfying a given condition.

This pattern is generalized by the method `filter` of the `List` class.

#### Reduction of Lists

Another common operation on lists is to combine the elements of a list using a given operator.

##### reduceLeft

This pattern can be abstracted out using the generic method `reduceLeft`.

`reduceLeft` inserts a given binary operator between adjacent elements of a list.

##### foldLeft

##### foldRight and reduceRight

## Week 6

### Other Sequences

#### Vectors

The Scala library also defines an alternative sequence implementation, `Vector`.

This one has more evenly balanced access patterns than `List`.

#### Operations on Vectors

#### Collection Hierarchy

A common base class of `List` and `Vector` is `Seq`, the class of all *sequences*.

`Seq` itself is a subclass of `Iterable`.

#### Ranges

Another simple kind of sequence is the *range*.

It represents a sequence of evenly spaced integers.

#### Some more Sequence Operations

```scala
xs exists p
```

```scala
xs forall p
```
`true` if `p(x)` holds for all elements `x` of `xs`, `false` otherwise.

```scala
xs zip ys

xs.unzip
```

```scala
xs.flatMap f
```
Applies collection-valued function `f` to all elements of `xs` and concatenates the results.

```scala
xs.sum

xs.product

xs.max

xs.min
```

### Combinatorial Search and for Comprehensions

#### Handling Nested Sequences

We can extend the usage of higher order functions on sequences to many calculations which are usually expressed using nested loops.

#### for Expression

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

To query a map without knowing beforehand whether it contains a given key, you can use the `get` operation:
```scala
capitalOfCountry get "US"
capitalOfCountry get "Andorra"
```
The result of a `get` operation is an `Option` value.

### The `Option` Type

The `Option` type is defined as:
```scala
trait Option[+A]
case class Some[+A](value: A) extends Option[A]
object None extends Option[Nothing]
```

### Decomposing Option

Since options are defined as case classes, they can be decomposed using pattern matching:
```scala
def showCapital(country: String) = capitalOfCountry.get(country) match {
	case Some(capital) => captial
	caes None => "missing data"
}
```

### Sorted and GroupBy

`groupBy` is available on Scala collections.
It partitions a collection into a map of collections according to a *discriminator function* `f`.
