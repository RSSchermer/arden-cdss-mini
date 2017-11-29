# A mini patient record with Arden decision support

Executes the decision support defined in Arden syntax `.mlm` files in the `mlm` directory
on a sample of example patient data using [Arden2ByteCode](http://plri.github.io/arden2bytecode/).

## Building

Building this project requires [SBT](http://www.scala-sbt.org/). Once installed, run the
following command:

```
sbt dist
```

This will build a `zip` file in the `/target/universal` directory.

## Running a build

### Windows

Open a command prompt and type in the following commands:

```
cd C:\path\to\where\you\extracted\the\zip

bin\arden-cdss-mini.bat

```

Then open a web browser of your choice and navigate to http://localhost:9000

### Linux

Open a terminal and type in the following commands:

```
cd /path/to/where/you/extracted/the/zip

bin\arden-cdss-mini

```

Then open a web browser of your choice and navigate to http://localhost:9000
