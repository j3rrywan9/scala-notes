# sbt Reference Manual

## Getting Started with sbt

### Installing sbt

### Directory structure

#### Base directory

In sbt's terminology, the "base directory" is the directory containing the project.

#### Source code

#### sbt build definition files

The build definition is described in `build.sbt` (actually any files named `*.sbt`) in the project's base directory.

#### Build support files

In addition to `build.sbt`, project directory can contain `.scala` files that defines helper objects and one-off plugins.

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

#### Batch mode

You can also run sbt in batch mode, specifying a space-separated list of sbt commands as arguments.
For sbt commands that take arguments, pass the command and arguments as one argument to sbt by enclosing them in quotes.

### Build definition

#### Specifying the sbt version

As part of your build definition you will specify the version of sbt that your build uses.
This allows people with different versions of the sbt launcher to build the same projects with consistent results.
To do this, create a file named `project/build.properties` that specifies the sbt version as follows:
```sbt
sbt.version = 1.3.10
```
If the required version is not available locally, the sbt launcher will download it for you.

#### What is a build definition?

A *build definition* is defined in `build.sbt`, and it consists of a set of projects (of type `Project`).
Because the term project can be ambiguous, we often call it a *subproject* in this guide.

Each subproject is configured by key-value pairs.

#### How `build.sbt` defines settings

`build.sbt` defines subprojects, which holds a sequence of key-value pairs called *setting expressions* using *build.sbt DSL*.

Each entry is called a *setting expression*. 
Some among them are also called task expressions.

A setting expression consists of three parts:
1. Left-hand side is a *key*.
1. *Operator*, which in this case is `:=`.
1. Right-hand side is called the *body*, or the *setting body*.

A key is an instance of `SettingKey[T]`, `TaskKey[T]`, or `InputKey[T]` where `T` is the expected value type.
The kinds of key are explained below.

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

#### Bare .sbt build definition

#### Adding library dependencies

### Multi-project builds

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
Now code in core can use classes from `util`.
This also creates an ordering between the projects when compiling them;
`util` must be updated and compiled before `core` can be compiled.

To depend on multiple projects, use multiple arguments to `dependsOn`, like `dependsOn(bar, baz)`.

#### Inter-project dependencies

#### Default root project

If a project is not defined for the root directory in the build, sbt creates a default one that aggregates all other projects in the build.

#### Navigating projects interactively

At the sbt interactive prompt, type `projects` to list your projects and `project <projectname>` to select a current project.
When you run a task like `compile`, it runs on the current project.
So you don't necessarily have to compile the root project, you could compile only a subproject.

You can run a task in another project by explicitly specifying the project ID, such as `subProjectID/compile`.

#### Common code

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
