# Course 2 Functional Program Design in Scala

## Week 1

### Representation of JSON in Scala

```scala
abstract class JSON
case class JSeq(elems: List[JSON]) extends JSON
case class JObj(bindings: Map[String, JSON]) extends JSON
case class JNum(num: Double) extends JSON
case class JStr(str: String) extends JSON
case class JBool(b: Boolean) extends JSON
case object JNull extends JSON
```

### Functions Are Objects

In Scala, every concrete type is the type of some class or trait.

The function type is no exception.

### The `Function1` Trait

Here's an outline of trait `Function1`:
```scala
trait Function1[-A, +R] {
	def apply(x: A): R
}
```
The pattern matching block
```scala
{ case (key, value) => key + ": " + value } 
```
expands to the `Function1` instance
```scala
new Function1[JBinding, String] {
	def apply(x: JBinding) = x match {
		case (key, value) => key + ": " + value
	}
}
```

### Subclassing Functions

One nice aspect of functions being traits is that we can subclass the function type.

For instance, maps are functions from keys to values:
```scala
trait Map[Key, Value] extends (Key => Value)
```

Sequences are functions from `Int` indices to values:
```scala
trait Seq[Elem] extends (Int => Elem)
```

### Partial Functions

```scala
val f: PartialFunction[String, String] = { case "ping" => "pong" }
f.isDefinedAt("ping")  // true
f.isDefinedAt("pong")  // false
```

### for Comprehensions and Pattern Matching

The left-hand side of a generator may also be a pattern.

### Queries with `for`

### `for` Expressions and Higher Order Functions

`map`, `flatMap`, and `filter` can all be expressed as `for` expressions.

### Translation of `for`

In reality, the Scala compiler expresses `for` expressions in terms of `map`, `flatMap` and a lazy variant of `filter`.

### Generalization of `for`

It is based solely on the presence of the methods `map`, `flatMap` and `withFilter`.

This lets you use the `for` syntax for your own types as well, you must only define `map`, `flatMap` and `withFilter` for these types.

### Functional Random Generators

#### Generators

Let's define a trait `Generator[T]` that generates random values of type `T`:
```scala
trait Generator[+T] {
  def generate: T
}
```

#### Generators with `map` and `flatMap`

#### Generator Examples

#### A `List` Generator

#### A `tree` Generator

```scala
trait Tree
case class Inner(left: Tree, right: Tree) extends Tree
case class Leaf(x: Int) extends Tree
```

### Monads

Data structures with `map` and `flatMap` seem to be quite common.

In fact there's a name that describes this class of data structures together with some algebraic laws that they should have.

They are called *monads*.

#### What is a Monad?

A monad `M` is a parametric type `M[T]` with two operations, `flatMap` and `unit`, that have to satisfy some laws.
```scala
trait M[T] {
  def flatMap[U](f: T => M[U]): M[U]
}

def unit[T](x: T): M[T]
```
In the literature, `flatMap` is more commonly called `bind`.

#### Examples of Monads

* `List`
* `Set`
* `Option`
* `Generator` is a monad with `unit(x) = single(x)`

`flatMap` is an operation on each of these types, whereas `unit` in Scala is different for each monad.

#### Monads and `map`

`map` can be defined for every monad as a combination of `flatMap` and `unit`:
```scala
m map f == m flatMap (x => unit(f(x)))
        == m flatMap (f andThen unit)
```

#### Monad Laws

To qualify as a monad, a type has to satisfy three laws:
* Associativity
* Left unit
* Right unit

#### Checking Monad Laws

## Week 2

### Structural Induction on Trees

Structural induction is not limited to lists; it applies to any tree structure.

### Streams

#### Delayed Evaluation

Streams are similar to lists, but their tail is evaluated only *on demand*.

#### Defining Streams

Streams are defined from a constant `Stream.empty` and a constructor `Stream.cons`.

For instance,
```scala
val xs = Stream.cons(1, Stream.cons(2, Stream.empty))
```
They can also be defined like the other collections by using the object `Stream` as a factory.
```scala
Stream(1, 2, 3)
```
The `toStream` method on a collection will turn the collection into a stream:
```scala
(1 to 1000).toStream
```

#### Methods on Streams

`Stream` supports almost all methods of `List`.

#### Stream Cons Operator

There is however an alternative operator `#::` which produces a stream.

`#::` can be used in expressions as well as patterns.

#### Implementation of Streams

#### Other Stream Methods

### Lazy Evaluation

#### Lazy Evaluation in Scala

## Week 3

### Stateful Objects

One normally describes the world as a set of objects, some of which have state that *changes* over the course of time.

An object *has a state* if its behavior is influenced by its history.

### Implementation of State

Every form of mutable state is constructed from variables.

### State in Objects

In practice, objects with state are usually represented by objects that have some variable members.

