# Coursera Scala Specialization Notes

##

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
