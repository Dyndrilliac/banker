*******************************************************************

* Title:     BankersAlgorithm
* Author:    [Matthew Boyette](mailto:N00868808@ospreys.unf.edu)
* Class:     Operating Systems (COP 4610)
* Professor: Dr. Sanjay Ahuja
* Term:      Fall 2017

*******************************************************************

This is a standalone Java project for Operating Systems (COP 4610) to provide deadlock detection and prevention using the Banker's algorithm. It uses the *JAMA* package's *Matrix* data structure, which is included with this repository in a Java archive (**.JAR*). See [*JAMA*'s website](http://math.nist.gov/javanumerics/jama/) for more information.

**VERY IMPORTANT**: The program's default input routines assume that all data corresponding to matrices or vectors are entered from left-to-right, and from top-to-bottom!

# File Listing

Samples:

* ***output_deny_unsafe.java***: Sample input/output for a case denying additional resources because it would result in an unsafe state.
* ***output_grant_safe.java***: Sample input/output for a case granting additional resources because it would result in an safe state.

Source Code:

* ***BankersAlgorithm.java***: This is where the magic happens.

Support:

* ***README.md***: this file, the program's documentation.
* ***LICENSE.md***: please use this software responsibly!
* ***makefile.unix***: a standard makefile for a Unix-like development environment. Rename to '***makefile***' to use.
* ***makefile.windows***: a standard makefile for a Windows development environment. Rename to '***makefile***' to use.
* ***JAMA-1.0.3.jar***: This file contains the *JAMA* package (and most notably, the *Matrix* structure) which is developed by the mathematics department of the **National Institute for Standards and Technology** (*NIST*).

# Compilation

* make

# Execution

* make execute

# Clean Up

* make clean
