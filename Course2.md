# Course 2 Functional Program Design in Scala

## Week 1

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

### Partial Functions

```scala
val f: PartialFunction[String, String] = { case "ping" => "pong" }
f.isDefinedAt("ping")  // true
f.isDefinedAt("pong")  // false
```
