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

### Functional Programming Languages

* In a *restricted* sense, a functional programming language is one which does not have mutable variables, assignments, or imperative control structures.
* In a *wider* sense, a functional programming language enables the construction of elegant programs that focus on functions.
* In particular, functions in a functional programming language are first-class citizens.

This means
* they can be defined anywhere, including inside other functions
* like any other value, they can be passed as parameters to functions and returned as results
* as for other values, there exists a set of operators to compose functions

### Parameter and Return Types

Function parameters come with their type, which is given after a colon.

If a return type is given, it follows the parameter list.

Primitive types are as in Java, but are written capitalized.

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

So one of the motivations for ruling out side effects in functional programming is that we can keep to the simple model of evaluation.

### Termination

Once we have the substitution model, another question comes up.
Does every expression reduce to a value (in a finite number of steps)?
In fact, the answer is no.
```scala
def loop: Int = loop

loop
```

### Call-by-name and call-by-value

Both strategies reduce to the same final value as long as
* the reduced expression consists of pure functions, and
* both evaluations terminate

CBV has the advantage that every function argument is evaluated only once.

CBN has the advantage that a function argument is not evaluated at all if the corresponding parameter is not used in the evaluation of the function's body.

### CBN, CBV and termination

CBN and CBV evaluation strategies reduce an expression to the same value, as long as both evaluations terminate.

We have:
* If CBV evaluation of an expression *e* terminates, then CBN evaluation of *e* terminates too.
* The other direction is not true.

### Scala's evaluation strategy

Scala normally uses call-by-value.

But if the type of a function parameter starts with `=>`, it uses call-by-name.
```scala
def constOne(x: Int, y: => Int) = x
```

### Conditional Expressions

To express choosing between two alternatives, Scala has a conditional expression `if-else`.

It looks like a `if-else` in Java, but is used for expressions, not statements.

Example:
```scala
def abs(x: Int) = if (x > 0) x else -x
```
What you see here is that the `if-else` is actually an expression.
It's not a statement where you have to set a variable and then return a variable.
It's an expression.

### Rewrite rules for Booleans

### Value Definitions

We have seen that function parameters can be passed by value or by name and in fact the same distinction applies to definition.

The `def` form is "by-name", its right hand side is evaluated on each use.

There is also a `val` form, which is "by-value".

The right hand side of a `val` definition is evaluated at the point of the definition itself.

Afterwards, the name refers to the value.

### Value Definitions and Termination

The difference between `val` and `def` becomes apparent when the right hand side does not terminate.

Recursive functions need an explicit return type in Scala.

For non-recursive functions, the return type is optional.

### Nested functions

It's good functional programming style to split up a task into many small functions.

### Blocks in Scala

* A block is delimited by curly braces.
* It contains a sequence of definitions or expressions.
* The last statement of a block is an expression defines its value.
* This return expression can be preceded by auxiliary definitions.
* Blocks are themselves expressions; a block may appear everywhere an expression can.

### Blocks and Visibility

* The definitions inside a block are only visible from within the block.
* The definitions inside a block *shadow* definitions of the same names outside the block.

### Lexical Scoping

Definitions of outer blocks are visible inside a block unless they are shadowed.

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

In general, if the last action of a function consists of calling a function (which may be the same), one stack frame would be sufficient for both functions.
Such calls are called *tail-calls*.

### Tail Recursion in Scala

In Scala, only directly recursive calls to the current function are optimized.

## Week 2

### Higher Order Functions

Functional languages treat functions as *first-class values*.

This means that, like any other value, a function can be passed as a parameter and returned as a result.

Functions that take other functions as parameters or that return functions as results are called *higher order functions*.

#### Function Types

The type `A => B` is the type of a *function* that takes an argument of type A and returns a result of type B.

So, `Int => Int` is the type of functions that map integers to integers.

#### Anonymous Functions

Passing functions as parameters leads to the creation of many small functions.

Sometimes it is tedious to have to define (and name) these functions using `def`.

Since functions are important in our language, it makes sense to think of introducing literals for functions as well, and that's what we do next.
These literals are called *anonymous functions*, because they do not have a name.

#### Anonymous Function Syntax

Example: A function that raises its argument to a cube:
```scala
(x: Int) => x * x * x
```
Here, `(x: Int)` is the *parameter* of the function, and `x * x * x` is it's *body*.

The type of the parameter can be omitted if it can be inferred by the compiler from the context.

If there are several parameters, they are separated by commas:
```scala
(x: Int, y: Int) => x + y
```

#### Anonymous Functions are Syntactic Sugar

An anonymous function `(x1: T1, ..., xn: Tn) => E` can always be expressed using `def` as follows:
```scala
def f(x1: T1, ..., xn: Tn) = E; f
```
where `f` is an arbitrary, fresh name (that's not yet used in the program).
One can therefore say that anonymous functions are *syntactic sugar*.

### Currying

Functions may define multiple parameter lists.
When a function is called with a fewer number of parameter lists, then this will yield a function taking the missing parameter lists as its arguments.

Currying transforms a function that takes multiple parameter lists into a chain of functions, each taking a single parameter.

#### Consecutive Stepwise Applications

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

### Expansion of Multiple Parameter Lists

Functions with multiple parameter lists are syntax sugar.

In general, a definition of a function with multiple parameter lists
```scala
def f(args1)...(argsn) = E
```
where `n>1`, is equivalent to
```scala
def f(args1)...(argsn-1) = {def g(argsn) = E; g}
```
where `g` is a fresh identifier.
Or for short:
```scala
def f(args1)...(argsn-1) = (argsn => E)
```

### Functions and Data

In this section, we'll learn how functions create and encapsulate data structures.

#### Classes

#### Objects

We call the elements of a class type *objects*.

We create an object by prefixing an application of the constructor of the class with the operator `new`.

#### Members of an Object

#### Methods

One can go further and also package functions operating on a data abstraction in the data abstraction itself.
Such functions are called *methods*.

#### Self Reference

On the inside of a class, the name `this` represents the object on which the current method is executed.

#### Constructors

In Scala, a class implicitly introduces a constructor.
This one is called the *primary constructor* of the class.

The primary constructor
* takes the parameters of the class
* and executes all statements in the class body

The Scala compiler will compile any code you place in the class body, which isn't part of a field or a method definition, into the primary constructor.

#### Auxiliary Constructors

In Scala, constructors other than the primary constructor are called *auxiliary constructors*.

Auxiliary constructors in Scala start with `def this(...)`.

In Scala, every auxiliary constructor must invoke another constructor of the same class as its first action.
In other words, the first statement in every auxiliary constructor in every Scala class will have the form `this(...)`.
The invoked constructor is either the primary constructor, or another auxiliary constructor that comes textually before the calling constructor.

### Evaluation and Operators

#### Classes and Substitutions

How is an instantiation of the class `new C(e1, ..., em)` evaluated?

The expression arguments `e1, ..., em` are evaluated like the arguments of a normal function.
That's it.

The resulting expression, say, `new C(e1, ..., em)` is already a value.

#### Operators

##### Infix Notation

Any method with a parameter can be used like an infix operator.

It is therefore possible to write `r add s` in place of `r.add(s)`.

##### Relaxed Identifiers

Operators can be used as identifiers.

#### Precedence Rules

The *precedence* of an operator is determined by its first character.

## Week 3

### Class Hierarchies

#### Abstract Classes

Abstract classes can contain members which are missing an implementation.

Consequently, no instances of an abstract class can be created with the operator `new`.

#### Class Extensions

#### Base Classes and Subclasses 

In Scala, any user-defined class extends another class.

If no superclass is given, the standard class `Object` in the Java package `java.lang` is assumed.

The direct or indirect superclasses of a class `C` are called *base classes* of `C`.

#### Implementation and Overriding

It is also possible to *redefine* an existing, non-abstract definition in a subclass by using `override`.

For methods that implement definitions in base classes the `override` is optional.

#### Object Definitions

So this object definition is exactly the same as the class definition, except that instead of the keyword `class`, you use `object`.

In terms of evaluation, singleton objects are values.

#### Programs

It is possible to create standalone applications in Scala.
Each such application would contain an object and that object contains a `main` method.

#### Dynamic Binding

Object-oriented programming languages (including Scala) implement *dynamic method dispatch*.

This means that the code invoked by a method call depends on the runtime type of the object that contains the method.

Each such application contains an object with a `main` method.

### How Classes Are Organized

#### Packages

Classes and objects are organized in packages.

To place a class or object inside a package, use a `package` clause at the top of your source file.

### Forms of Imports

```scala
import week3.Rational
import week3.{Rational, Hello}
import week3._
```
The first two forms are called *named imports*.

The last form is called a *wildcard import*.

You can import from either a package or an object.

#### Automatic Imports

Some entities are automatically imported in any Scala program.

These are:
* All members of package `scala`
* All members of package `java.lang`
* All members of the singleton object `scala.Predef`

#### Scaladoc

#### Traits

In Java, as well as in Scala, a class can only have one superclass.

A trait is declared like an abstract class, just with `trait` instead of `abstract class`.

Classes, objects and traits can inherit from at most one class but arbitrary many traits.

Traits resemble interfaces in Java, but are more powerful because they can contain fields and concrete methods.
On the other hand, traits cannot have (value) parameters,l only classes can.

Once a trait is defined, it can be *mixed in* to a class using either the `extends` or `with` keyword.

If you wish to mix a trait into a class that explicitly extends a superclass, you use `extends` to indicate the superclass and `with` to mix in the trait.
If you want to mix in multiple traits, you add more `with` clauses.

#### Scala's Class Hierarchy

* `scala.Any`

The base type of all types.

* `scala.AnyVal`

The base type of all primitive types.

* `scala.AnyRef`

Class **AnyRef** is the root class of all *reference types*.
All types except the value types descend from this class.
Alias of `java.lang.Object`.

* `scala.Nothing`

`Nothing` is at the bottom of Scala's type hierarchy.
**Nothing** is a subtype of every other type (including `scala.Null`); there exist *no instances* of this type.

* `scala.Null`

**Null** is a subtype of all reference types; its only instance is the **null** reference.

### Polymorphism

#### Value Parameters

#### Type Parameters

We can generalize the definition using a type parameter.

Type parameters are written in square brackets, e.g. `[T]`.

#### Generic Functions

Like classes, functions can have type parameters.

For instance, here is a function that creates a list consisting of a single element.
```scala
def singleton[T](elem: T) = new Cons[T](elem, new Nil[T])
```
We can then write:
```scala
singleton[Int](1)
singleton[Boolean](true)
```

#### Type Inference

In fact, the Scala compiler can usually deduce the correct type parameters from the value arguments of a function call.

So, in most cases, type parameters can be left out.
You could also write:
```scala
singleton(1)
singleton(true)
```

#### Types and Evaluation

Type parameters do not affect evaluation in Scala.

We can assume that all type parameters and type arguments are removed before evaluating the program.

This is also called *type erasure*.

### Polymorphism

Polymorphism means that a function type comes "in many forms".

In programming it means that
* the function can be applied to arguments of many types
* the type can have instances of many types

We have seen two principal forms of polymorphism:
* subtyping: instances of a subclass can be passed to a base class
* generics: instances of a function or class are created by type parameterization

## Week 4

### Objects Everywhere

#### Pure Object Orientation

A pure object-orientated language is one in which every value is an object.

If the language is based on classes, this means that the type of each value is a class.

#### Standard Classes

Conceptually, types such as `Int` or `Boolean` do not receive special treatment in Scala.
They are like the other classes, defined in the package `scala`.

#### Pure Booleans

The `Boolean` type maps to the JVM's primitive type `boolean`.

### Functions as Objects

In fact function values *are* treated as objects in Scala.

The function type `A => B` is just an abbreviation for the class `scala.Function1[A, B]`, which is roughly defined as follows:
```scala
package scala

trait Function1[A, B] {
  def apply(x: A): B
}
```
So functions are objects with `apply` method.

There are also traits `Function2`, `Function3`, ... for functions which take more arguments (currently up to 22).

#### Expansion of Function Values

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

#### Expansion of Function Calls

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

#### Functions and Methods

Note that a method such as
```scala
def f(x: Int): Boolean = ...
```
is not itself a function value.

But if `f` is used in a place where a `Function` type is expected, it is converted automatically to the function value:
```scala
(x: Int) => f(x)
```
or, expanded:
```scala
new Function1[Int, Boolean] {
  def apply(x: Int) = f(x)
}
```
This is called *eta expansion* in lambda calculus.

### Subtyping and Generics

#### Polymorphism

Two principal forms of polymorphism:
* subtyping
* generics

In this section we will look at their interactions.

Two main areas:
* bounds
* variance

#### Type Bounds

Generally, the notation
* `S <: T` means: `S` is a subtype of `T`, and
* `S >: T` means: `S` is a supertype of `T`

##### Upper Bounds

Consider the method `assertAllPos` which
* takes an `IntSet`
* returns the `IntSet` itself if all its elements are positive
* throws an exception otherwise

```scala
def assertAllPos[S <: IntSet](r: S): S = ...
```
Here, `<: IntSet` is an *upper bound* of the type parameter `S`.

It means that `S` can be instantiated only to types that conform to the bound i.e. `IntSet`.

##### Lower Bounds

You can also use a lower bound for a type parameter.
```scala
[S >: NonEmpty]
```
introduces a type parameter `S` that can range only over *supertypes* of `NonEmpty`.

So `S` could be one of `NonEmpty`, `IntSet`, `AnyRef`, or `Any`.

##### Mixed Bounds

Finally, it is also possible to mix a lower bound with an upper bound.

#### Covariance

There's another interaction between subtyping and type parameters we need to consider.
Given:
```scala
NonEmpty <: IntSet
```
is
```scala
List[NonEmpty] <: List[IntSet] ?
```
Intuitively, this makes sense.
A list of non-empty sets is a special case of a list of arbitrary sets.

We call types for which this relationship holds *covariant* because their subtyping relationship varies with the type parameter.

#### The Liskov Substitution Principle

### Variance

Roughly speaking, a type that accepts mutations of its elements should not be covariant.

But immutable types can be covariant, if some conditions on methods are met.

#### Definition of Variance

Say `C[T]` is a parameterized type and `A`, `B` are types such that `A <: B`.

I general, there are *three* possible relationships between `C[A]` and `C[B]`:
* `C[A] <: C[B]`
* `C[A] >: C[B]`
* neither `C[A]` nor `C[B]` is a subtype of the other

Scala lets you declare the variance of a type by annotating the type parameter:
```scala
class C[+T] { ... }  // C is covariant

class C[-T] { ... }  // C is contravariant

class C[T] { ... }  // C is nonvariant
```

#### Typing Rules for Functions

#### Function Trait Declaration

So functions are *contravariant* in their argument type(s) and *covariant* in their result type.

This lead to the following revised definition of the `Function1` trait:
```scala
trait Function1[-T, +U] {
  def apply(x: T): U
}
```

#### Variance Checks

The Scala compiler will check that there are no problematic combinations when compiling a class with variance annotations.

#### Variance and Lists

#### Making Classes Covariant

#### Lower Bounds

### Decomposition

#### Non-Solution: Type Tests and Type Casts

Scala lets you do type tests and type casts using methods defined in class `Any`:
```scala
def isInstanceOf[T]: Boolean
def asInstanceOf[T]: T
```
These correspond to Java's type tests and casts.
But their use in Scala is discouraged, because there are better alternatives.

### Pattern Matching

#### Functional Decomposition with Pattern Matching

Observation: the sole purpose of test and accessor functions is to *reverse* the construction process:
* Which subclass was used?
* What were the arguments of the constructor?

This situation is so common that many functional languages, Scala included, automate it.
And the technical term of this automated solution is *pattern matching*.

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

Variables always begin with a lowercase letter.

Names of constants begin with a capital letter, with the exception of the reserved words `null`, `true`, `false`.

#### Evaluating Match Expressions

The whole match expression is rewritten to the right-hand side of the first case where the pattern matches the selector.

References to pattern variables are replaced by the corresponding parts in the selector.

#### What Do Patterns Match?

* A constructor pattern `C(p1, ..., pn)` matches all the values of type `C`(or a subtype) that have been constructed with arguments matching the patterns `p1, ..., pn`.
* A variable pattern `x` matches any value, and *binds* the name of the variable to this value.
* A constant pattern `c` matches values that are equal to `c` (in the sense of `==`).

### Lists

The list is a functional data structure in functional programming.

There are two important differences between lists and arrays.
* Lists are immutable - the elements of a list cannot be changed.
* Lists are recursive, while arrays are flat.

#### The `List` Type

Like arrays, lists are **homogeneous**: the elements of a list must all have the same type.

The type of a list that has elements of type `T` is written `List[T]`.

#### Constructors of Lists

All lists are constructed from:
* the empty list `Nil`, and
* the construction operation `::` (pronounced *cons*):
`x :: xs` gives a new list with the first element `x`, followed by the elements of `xs`.

#### Right Associativity

Convention: Operators ending in ":" associate to the right.

Operators ending in ":" are also different as they are seen as method calls of the *right-hand* operand.

#### Operations on Lists

All operations on lists can be expressed in terms of the following three operations:
* `head`: the first element of the list
* `tail`: the list composed of all the elements except the first
* `isEmpty`: `true` if the list is empty, `false` otherwise

#### List Patterns

It is also possible to decompose lists with pattern matching.
* `Nil` - The `Nil` constant
* `p :: ps` - A pattern that matches a list with a `head` matching `p` and a `tail` matching `ps`
* `List(p1, ..., pn)` - Same as `p1 :: ... :: pn :: Nil`

## Week 5

#### List Methods

```scala
xs.length
xs.last
xs.init
xs take n
xs drop n
xs(n)
xs ++ ys
xs.reverse
xs updated (n, x)
xs indexOf x
xs contains x
```

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

A tuple type `(T1, ..., Tn)` is an abbreviation of the parameterized type `scala.TupleN[T1, ..., Tn]`.

#### The `Tuple` class

Here, all `TupleN` classes are modeled after the following pattern:
```scala

```

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

#### Collection Hierarchy

A common base class of `List` and `Vector` is `Seq`, the class of all *sequences*.

`Seq` itself is a subclass of `Iterable`.

#### Sets

Sets are another basic abstraction in the Scala collections.

#### Sets vs Sequences

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
Options also support quite a few operations of the other collections.

### Sorted and GroupBy

Two useful operations of SQL queries in addition to for expressions are `groupBy` and `orderBy`.

`orderBy` on a collection can be expressed by `sortWith` and `sorted`.

`groupBy` is available on Scala collections.
It partitions a collection into a map of collections according to a *discriminator function* `f`.
