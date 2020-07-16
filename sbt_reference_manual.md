# sbt Reference Manual

## Getting Started with sbt

### Installing sbt

### Directory structure

#### Base directory

In sbt's terminology, the "base directory" is the directory containing the project.

#### Source code

sbt uses the same directory structure as Maven for source files by default (all paths are relative to the base directory):
```
src/
    main/
        resources/
        scala/
        scala-2.12/
        java/
    test/
        resources
        scala
        scala-2.12/
        java
```

#### sbt build definition files

The build definition is described in `build.sbt` (actually any files named `*.sbt`) in the project's base directory.

#### Build support files

In addition to `build.sbt`, project directory can contain `.scala` files that defines helper objects and one-off plugins.
See organizing the build for more.

#### Build products

Generated files (compiled classes, packaged jars, managed files, caches, and documentation) will be written to the `target` directory by default.

### Running

#### sbt shell

Run sbt in your project directory with no arguments:
```bash
$ sbt
```
Running sbt with no command line arguments starts sbt shell.
sbt shell has a command prompt (with tab completion and history!).

For example, you could type compile at the sbt shell:
```sbt
compile
```
To compile again, press up arrow and then enter.

To run your program, type `run`.

To leave sbt shell, type `exit` or use Ctrl+D (Unix) or Ctrl+Z (Windows).

#### Batch mode

You can also run sbt in batch mode, specifying a space-separated list of sbt commands as arguments.
For sbt commands that take arguments, pass the command and arguments as one argument to sbt by enclosing them in quotes.
For example,
```bash
sbt clean compile "testOnly TestA TestB"
```
In this example, `testOnly` has arguments, `TestA` and `TestB`.
The commands will be run in sequence (`clean`, `compile`, then `testOnly`).

#### Common Commands

Here are some of the most common sbt commands.
* clean
* compile
* test
* console
* run
* package
* help
* reload

### Build definition

This page describes sbt build definitions, including some "thoery" and the syntax of `build.sbt`.

#### Specifying the sbt version

As part of your build definition you will specify the version of sbt that your build uses.
This allows people with different versions of the sbt launcher to build the same projects with consistent results.
To do this, create a file named `project/build.properties` that specifies the sbt version as follows:
```sbt
sbt.version = 1.3.13
```
If the required version is not available locally, the sbt launcher will download it for you.

#### What is a build definition?

A *build definition* is defined in `build.sbt`, and it consists of a set of projects (of type `Project`).
Because the term project can be ambiguous, we often call it a *subproject* in this guide.

Each subproject is configured by key-value pairs.

#### How `build.sbt` defines settings

`build.sbt` defines subprojects, which holds a sequence of key-value pairs called *setting expressions* using *build.sbt DSL*.
```sbt
ThisBuild / organization := "com.example"
ThisBuild / scalaVersion := "2.12.10"
ThisBuild / version      := "0.1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .settings(
    name := "hello"
  )
```
Each entry is called a *setting expression*. 
Some among them are also called task expressions.

A setting expression consists of three parts:
1. Left-hand side is a *key*.
1. *Operator*, which in this case is `:=`.
1. Right-hand side is called the *body*, or the *setting body*.

A key is an instance of `SettingKey[T]`, `TaskKey[T]`, or `InputKey[T]` where `T` is the expected value type.
The kinds of key are explained below.

build.sbt may also be interspersed with `val`s, `lazy val`s, and `def`s.
Top-level `object`s and `class`es are not allowed in `build.sbt`.
Those should go in the `project/` directory as Scala source files.

#### Keys

##### Types

There are three flavors of key:
* `SettingKey[T]`: a key for a value computed once (the value is computed when loading the subproject, and kept around).
* `TaskKey[T]`: a key for a value, called a *task*, that has to be recomputed each time, potentially with side effects.
* `InputKey[T]`: a key for a task that has command line arguments as input.
Check out Input Tasks for more details.

##### Built-in Keys

The built-in keys are just fields in an object called `Keys`.
A `build.sbt` implicitly has an import `sbt.Keys._`, so `sbt.Keys.name` can be referred to as `name`.

##### Custom Keys

Custom keys may be defined with their respective creation methods: `settingKey`, `taskKey`, and `inputKey`.
Each method expects the type of the value associated with the key as well as a description.
The name of the key is taken from the `val` the key is assigned to.

Note: Typically, `lazy val`s are used instead of `val`s to avoid initialization order problems.

#### Task vs. Setting keys

A `TaskKey[T]` is said to define a *task*.
Tasks are operations such as `compile` or `package`.
They may return `Unit`, or they may return a value related to the task, for example `package` is a `TaskKey[File]` and its value is the jar file it creates.

Each time you start a task execution, for example by typing `compile` at the interactive sbt prompt, sbt will re-run any tasks involved exactly once.

#### Define tasks and settings

Using `:=`, you can assign a value to a setting and a computation to a task.
For a setting, the value will be computed once at project load time.
For a task, the computation will be re-run each time the task is executed.

#### Keys in sbt shell

In sbt shell, you can type the name of any task to execute that task.
This is why typing `compile` runs the `compile` task.
`compile` is a task key.

If you type the name of a setting key rather than a task key, the value of the setting key will be displayed.
Typing a task key name executes the task but doesn't display the resulting value;
to see a task's result, use `show <task name>` rather than plain <task name>.
The convention for keys names is to use `camelCase` so that the command line name and the Scala identifiers are the same.

To learn more about any key, type `inspect <keyname>` at the sbt interactive prompt.
Some of the information inspect displays won't make sense yet, but at the top it shows you the setting's value type and a brief description of the setting.

#### Imports in build.sbt

You can place import statements at the top of build.sbt;
they need not be separated by blank lines.

There are some implied default imports, as follows:
```sbt
import sbt._
import Keys._
```

#### Bare .sbt build definition

The settings can be written directly into the `build.sbt` file instead of putting them inside a `.settings(...)` call.
We call this the "bare style".
```sbt
ThisBuild / version := "1.0"
ThisBuild / scalaVersion := "2.12.10"
```
This syntax is recommended for `ThisBuild` scoped settings and adding plugins.
See later section about the scoping and the plugins.

#### Adding library dependencies

To depend on third-party libraries, there are two options.
The first is to drop jars in `lib/` (unmanaged dependencies) and the other is to add managed dependencies, which will look like this in `build.sbt`:
```sbt
val derby = "org.apache.derby" % "derby" % "10.4.1.3"

ThisBuild / organization := "com.example"
ThisBuild / scalaVersion := "2.12.10"
ThisBuild / version      := "0.1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .settings(
    name := "Hello",
    libraryDependencies += derby
  )
```
This is how you add a managed dependency on the Apache Derby library, version 10.4.1.3.

The `libraryDependencies` key involves two complexities: `+=` rather than `:=`, and the `%` method.
`+=` appends to the key's old value rather than replacing it, this is explained in Task Graph.
The `%` method is used to construct an Ivy module ID from strings, explained in Library dependencies.

### Multi-project builds

This page introduces multiple subprojects in a single build.

#### Multiple subprojects

It can be useful to keep multiple related subprojects in a single build, especially if they depend on one another and you tend to modify them together.

Each subproject in a build has its own source directories, generates its own jar file when you run package, and in general works like any other project.

A project is defined by declaring a `lazy val` of type `Project`.
```sbt
lazy val util = (project in file("util"))

lazy val core = (project in file("core"))
```
The name of the `val` is used as the subproject's ID, which is used to refer to the subproject at the sbt shell.

Optionally the base directory may be omitted if it's the same as the name of the `val`.
```sbt
lazy val util = project

lazy val core = project
```

#### Build-wide settings

To factor out common settings across multiple projects, define the settings scoped to `ThisBuild`.
The limitation is that the right-hand side needs to be a pure value or settings scoped to `Global` or `ThisBuild`, and there are no default settings scoped to subprojects.
```sbt
ThisBuild / organization := "com.example"
ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.12.10"

lazy val core = (project in file("core"))
  .settings(
    // other settings
  )

lazy val util = (project in file("util"))
  .settings(
    // other settings
  )
```
Now we can bump up `version` in one place, and it will be reflected across subprojects when you reload the build.

#### Common settings

Another way to factor out common settings across multiple projects is to create a sequence named `commonSettings` and call `settings` method on each project.

#### Dependencies

Projects in the build can be completely independent of one another, but usually they will be related to one another by some kind of dependency.
There are two types of dependencies: aggregate and classpath.

##### Aggregation

Aggregation means that running a task on the aggregate project will also run it on the aggregated projects.
For example,
```sbt
lazy val root = (project in file("."))
  .aggregate(util, core)

lazy val util = (project in file("util"))

lazy val core = (project in file("core"))
```
In the above example, the root project aggregates `util` and `core`.
Start up sbt with two subprojects as in the example, and try compile.
You should see that all three projects are compiled.

##### Classpath dependencies

A project may depend on code in another project.
This is done by adding a `dependsOn` method call.
For example, if `core` needed `util` on its classpath, you would define `core` as:
```sbt
lazy val core = project.dependsOn(util)
```
Now code in `core` can use classes from `util`.
This also creates an ordering between the projects when compiling them;
`util` must be updated and compiled before `core` can be compiled.

To depend on multiple projects, use multiple arguments to `dependsOn`, like `dependsOn(bar, baz)`.

#### Inter-project dependencies

On extremely large projects with many files and many subprojects, sbt can perform less optimally at continuously watching files that have changed and use a lot of disk and system I/O.

sbt has `trackInternalDependencies` and `exportToInternal` settings.
These can be used to control whether to trigger compilation of a dependent subprojects when you call compile.
Both keys will take one of three values: `TrackLevel.NoTracking`, `TrackLevel.TrackIfMissing`, and `TrackLevel.TrackAlways`.
By default they are both set to `TrackLevel.TrackAlways`.

#### Default root project

If a project is not defined for the root directory in the build, sbt creates a default one that aggregates all other projects in the build.

#### Navigating projects interactively

At the sbt interactive prompt, type `projects` to list your projects and `project <projectname>` to select a current project.
When you run a task like `compile`, it runs on the current project.
So you don't necessarily have to compile the root project, you could compile only a subproject.

You can run a task in another project by explicitly specifying the project ID, such as `subProjectID/compile`.

#### Common code

The definitions in `.sbt` files are not visible in other `.sbt` files.
In order to share code between `.sbt` files, define one or more Scala files in the `project/` directory of the build root.

#### Appendix: Subproject build definition files

Any `.sbt` files in `foo`, say `foo/build.sbt`, will be merged with the build definition for the entire build, but scoped to the `hello-foo` project.

Style choices:
* Each subproject's settings can go into `*.sbt` files in the base directory of that project, while the root `build.sbt` declares only minimum project declarations in the form of `lazy val foo = (project in file("foo"))` without the settings.
* We recommend putting all project declarations and settings in the root `build.sbt` file in order to keep all build definition under a single file.
However, it's up to you.

### Task graph

Rather than thinking of `settings` as key-value pairs, a better analogy would be to think of it as a *directed acyclic graph* (DAG) of tasks where the edges denote **happens-before**.
Let's call this the *task graph*.

#### Terminology

Let's review the key terms before we dive in.
* Setting/Task expression: entry inside `.settings(...)`.
* Key: Left hand side of a setting expression.
It could be a `SettingKey[A]`, a `TaskKey[A]`, or an `InputKey[A]`.
* Setting: Defined by a setting expression with `SettingKey[A]`.
The value is calculated once during load.
* Task: Defined by a task expression with `TaskKey[A]`.
The value is calculated each time it is invoked.

#### Declaring dependency to other tasks

In `build.sbt` DSL, we use `.value` method to express the dependency to another task or setting.
The `value` method is special and may only be called in the argument to `:=` (or, `+=` or `++=`, which we'll see later).

#### Inspecting the task

This is how sbt knows which tasks depend on which other tasks.

When you type `compile` sbt automatically performs an `update`, for example.
It Just Works because the values required as inputs to the `compile` computation require sbt to do the `update` computation first.

In this way, all build dependencies in sbt are *automatic* rather than explicitly declared.
If you use a key's value in another computation, then the computation depends on that key.

#### Defining a task that depends on other settings

#### Defining a setting that depends on other settings

In terms of the execution timing, we can think of the settings as a special tasks that evaluate during loading time.

### Scopes

#### The whole story about keys

In truth, each key can have an associated value in more than one context, called a *scope*.

*There is no single value for a given key*, because the value may differ according to scope.

However, there is a single value for a given *scoped* key.

If you think about sbt processing a list of settings to generate a key-value map describing the project, as discussed earlier, the keys in that key-value map are *scoped* keys.
Each setting defined in the build definition (for example in `build.sbt`) applies to a scoped key as well.

Often the scope is implied or has a default, but if the defaults are wrong, you'll need to mention the desired scope in `build.sbt`.

#### Scope axes

A *scope axis* is a type constructor similar to `Option[A]`, that is used to form a component in a scope.

There are three scope axes:
* The subproject axis
* The dependency configuration axis
* The task axis

Similarly, a full scope in sbt is formed by a **tuple** of a subproject, a configuration, and a task value:
```sbt
projA / Compile / console / scalacOptions
```

##### Scoping by the subproject axis

If you put multiple projects in a single build, each project needs its own settings.
That is, keys can be scoped according to the project.

The project axis can also be set to `ThisBuild`, which means the "entire build", so a setting applies to the entire build rather than a single project.
Build-level settings are often used as a fallback when a project doesn't define a project-specific setting.
We will discuss more on build-level settings later in this page.

##### Scoping by the configuration axis

A *dependency configuration* (or "configuration" for short) defines a graph of library dependencies, potentially with its own classpath, sources, generated packages, etc.
The dependency configuration concept comes from Ivy, which sbt used to use for managed dependencies Library Dependencies, and from MavenScopes.

Some configurations you'll see in sbt:
* `Compile` which defines the main build (`src/main/scala`).
* `Test` which defines how to build tests (`src/test/scala`).
* `Runtime` which defines the classpath for the `run` task.

By default, all the keys associated with compiling, packaging, and running are scoped to a configuration and therefore may work differently in each configuration.
The most obvious examples are the task keys `compile`, `package`, and `run`;
but all the keys which affect those keys (such as `sourceDirectories` or `scalacOptions` or `fullClasspath`) are also scoped to the configuration.

Another thing to note about a configuration is that it can extend other configurations.
The following figure shows the extension relationship among the most common configurations.

##### Scoping by Task axis 

Settings can affect how a task works.
For example, the `packageSrc` task is affected by the `packageOptions` setting.

To support this, a task key (such as `packageSrc`) can be a scope for another key (such as `packageOptions`).

The various tasks that build a package (`packageSrc`, `packageBin`, `packageDoc`) can share keys related to packaging, such as `artifactName` and `packageOptions`.
Those keys can have distinct values for each packaging task.

##### Zero scope component

Each scope axis can be filled in with an instance of the axis type (analogous to `Some(_)`), or the axis can be filled in with the special value `Zero`.
So we can think of `Zero` as `None`.

`Zero` is a universal fallback for all scope axes, but its direct use should be reserved to sbt and plugin authors in most cases.

`Global` is a scope that sets `Zero` to all axes: `Zero` / `Zero` / `Zero`.
In other words, `Global / someKey` is a shorthand for `Zero / Zero / Zero / someKey`.

#### Referring to scopes in a build definition

If you create a setting in `build.sbt` with a bare key, it will be scoped to (current subproject / configuration `Zero` / task `Zero`):
```sbt
lazy val root = (project in file("."))
  .settings(
    name := "hello"
  )
```

#### Referring to scoped keys from the sbt shell 

On the command line and in the sbt shell, sbt displays (and parses) scoped keys like this:
```sbt
ref / Config / intask / key
```
* `ref` identifies the subproject axis.
It could be <project-id>, ProjectRef(uri("file:..."), "id"), or `ThisBuild` that denotes the "entire build" scope.
* `Config` identifies the configuration axis using the capitalized Scala identifier.
* `intask` identifies the task axis.
* `key` identifies the key being scoped.

`Zero` can appear for each axis.

If you omit part of the scoped key, it will be inferred as follows:
* the current project will be used if you omit the project.
* a key-dependent configuration will be auto-detected if you omit the configuration or task.

#### Inspecting scopes

In sbt shell, you can use the `inspect` command to understand keys and their scopes.
Try `inspect Test/fullClasspath`:

"Provided by" points you to the scoped key that defines the value, in this case `ProjectRef(uri("file:/tmp/hello/"), "root") / Test / fullClasspath` (which is the `fullClasspath` key scoped to the `Test` configuration and the `ProjectRef(uri("file:/tmp/hello/"), "root")` project).

"Dependencies" was discussed in detail in the previous page.

We'll discuss "Delegates" later.

Try `inspect fullClasspath` (as opposed to the above example, `inspect Test / fullClasspath`) to get a sense of the difference.
Because the configuration is omitted, it is autodetected as `Compile`.
`inspect Compile / fullClasspath` should therefore look the same as `inspect fullClasspath`.

#### When to specify a scope 

You need to specify the scope if the key in question is normally scoped.
For example, the `compile` task, by default, is scoped to `Compile` and `Test` configurations, and does not exist outside of those scopes.

To change the value associated with the `compile` key, you need to write `Compile / compile` or `Test / compile`.
Using plain compile would define a new compile task scoped to the current project, rather than overriding the standard compile tasks which are scoped to a configuration.

If you get an error like "Reference to undefined setting", often you've failed to specify a scope, or you've specified the wrong scope.
The key you're using may be defined in some other scope.
sbt will try to suggest what you meant as part of the error message;
look for "Did you mean Compile / compile?"

One way to think of it is that a name is only part of a key.
In reality, all keys consist of both a name, and a scope (where the scope has three axes).
The entire expression `Compile / packageBin / packageOptions` is a key name, in other words.
Simply `packageOptions` is also a key name, but a different one (for keys with no slashes, a scope is implicitly assumed: current project, `Zero` config, `Zero` task).

#### Build-level settings

An advanced technique for factoring out common settings across subprojects is to define the settings scoped to `ThisBuild`.

If a key that is scoped to a particular subproject is not found, sbt will look for it in `ThisBuild` as a fallback.
Using the mechanism, we can define a build-level default setting for frequently used keys such as `version`, `scalaVersion`, and `organization`.

Due to the nature of scope delegation that we will cover later, build-level settings should be set only to a pure value or settings from either `Global` or `ThisBuild` scoping.

#### Scope delegation

A scoped key may be undefined, if it has no value associated with it in its scope.

For each scope axis, sbt has a fallback search path made up of other scope values.
Typically, if a key has no associated value in a more-specific scope, sbt will try to get a value from a more general scope, such as the `ThisBuild` scope.

This feature allows you to set a value once in a more general scope, allowing multiple more-specific scopes to inherit the value.
We will discuss scope delegation in detail later.

### Appending values

#### Appending to previous values: `+=` and `++=`

Assignment with `:=` is the simplest transformation, but keys have other methods as well.
If the `T` in `SettingKey[T]` is a sequence, i.e. the key's value type is a sequence, you can append to the sequence rather than replacing it.
* `+=` will append a single element to the sequence.
* `++=` will concatenate another sequence.

For example, the key `Compile / sourceDirectories` has a `Seq[File]` as its value.
By default this key's value would include src/main/scala.
If you wanted to also compile source code in a directory called source (since you just have to be nonstandard), you could add that directory:
```sbt
Compile / sourceDirectories += new File("source")
```
Or, using the `file()` function from the sbt package for convenience:
```sbt
Compile / sourceDirectories += file("source")
```
(`file()` just creates a new `File`.)

You could use `++=` to add more than one directory at a time:
```sbt
Compile / sourceDirectories ++= Seq(file("sources1"), file("sources2"))
```
Where `Seq(a, b, c, ...)` is standard Scala syntax to construct a sequence.

To replace the default source directories entirely, you use `:=` of course:
```sbt
Compile / sourceDirectories := Seq(file("sources1"), file("sources2"))
```

#### When settings are undefined

#### Tasks based on other keys' values

#### Appending with dependencies: `+=` and `++=`

### Scope delegation

To summarize what we've learned so far:
* A scope is a tuple of components in three axes: the subproject axis, the configuration axis, and the task axis.
* There's a special scope component `Zero` for any of the scope axes.
* There's a special scope component `ThisBuild` for the subprojects axis only.
* `Test` extends `Runtime`, and `Runtime` extends `Compile` configuration.
* A key placed in `build.sbt` is scoped to `${current subproject} / Zero / Zero` by default.
* A key can be scoped using `/` operator.

#### Scope delegation rules

Here are the rules for scope delegation:
* Rule 1: Scope axes have the following precedence: the subproject axis, the configuration axis, and then the task axis.
* Rule 2: Given a scope, delegate scopes are searched by substituting the task axis in the following order: the given task scoping, and then `Zero`, which is non-task scoped version of the scope.
* Rule 3: Given a scope, delegate scopes are searched by substituting the configuration axis in the following order: the given configuration, its parents, their parents and so on, and then `Zero` (same as unscoped configuration axis).
* Rule 4: Given a scope, delegate scopes are searched by substituting the subproject axis in the following order: the given subproject, `ThisBuild`, and then `Zero`.
* Rule 5: A delegated scoped key and its dependent settings/tasks are evaluated without carrying the original context.

We will look at each rule in the rest of this page.

#### `inspect` command lists the delegates

You might want to look up quickly what is going on.
This is where `inspect` can be used.

### Library dependencies

Library dependencies can be added in two ways:
* *unmanaged dependencies* are jars dropped into the `lib` directory
* *managed dependencies* are configured in the build definition and downloaded automatically from repositories

#### Unmanaged dependencies

#### Managed dependencies

sbt uses Coursier to implement managed dependencies, so if you're familiar with Coursier, Apache Ivy or Maven, you won't have much trouble.

##### The `libraryDependencies` key

Most of the time, you can simply list your dependencies in the setting `libraryDependencies`.
It's also possible to write a Maven POM file or Ivy configuration file to externally configure your dependencies, and have sbt use those external configuration files.

Declaring a dependency looks like this, where `groupId`, `artifactId`, and `revision` are strings:
```sbt
libraryDependencies += groupID % artifactID % revision
```
or like this, where `configuration` can be a string or a `Configuration` value (such as `Test`):
```sbt
libraryDependencies += groupID % artifactID % revision % configuration
```

##### Getting the right Scala version with `%%`

If you use `organization %% moduleName % version` rather than `organization % moduleName % version` (the difference is the double `%%` after the `organization`), sbt will add your project's binary Scala version to the artifact name.
This is just a shortcut.
You could write this without the `%%`:
```sbt
libraryDependencies += "org.scala-tools" % "scala-stm_2.11" % "0.3"
```
Assuming the scalaVersion for your build is 2.11.1, the following is identical (note the double `%%` after "`org.scala-tools`"):
```sbt
libraryDependencies += "org.scala-tools" %% "scala-stm" % "0.3"
```
The idea is that many dependencies are compiled for multiple Scala versions, and you'd like to get the one that matches your project to ensure binary compatibility.

##### Resolvers

Not all packages live on the same server;
sbt uses the standard Maven2 repository by default.
If your dependency isn't on one of the default repositories, you'll have to add a *resolver* to help Ivy find it.

To add an additional repository, use
```sbt
resolvers += name at location
```
with the special `at` between two strings.

### Using plugins

A plugin extends the build definition, most commonly by adding new settings.
The new settings could be new tasks.
For example, a plugin could add a `codeCoverage` task which would generate a test coverage report.

### Custom settings and tasks

#### Defining a key

### Organizing the build

#### sbt is recursive

`build.sbt` conceals how sbt really works.
sbt builds are defined with Scala code.
That code, itself, has to be built.
What better way than with sbt?

The `project` directory *is another build inside your build*, which knows how to build your build.
To distinguish the builds, we sometimes use the term **proper build** to refer to your build, and **meta-build** to refer to the build in `project`.
The projects inside the metabuild can do anything any other project can do.
*Your build definition is an sbt project*.

And the turtles go all the way down.
If you like, you can tweak the build definition of the build definition project, by creating a `project/project/` directory.

#### Tracking dependencies in one place

One way of using the fact that `.scala` files under `project` becomes part of the build definition is to create `project/Dependencies.scala` to track dependencies in one place.

The `Dependencies` object will be available in `build.sbt`.
To make it easier to use the `val`s defined in it, import `Dependencies._` in your `build.sbt` file.
