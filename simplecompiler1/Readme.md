# My Custom Programming Language

## Overview

This document outlines the rules and structure of a custom programming language designed for educational purposes. The language includes basic constructs such as variable declarations, function definitions, control structures, and expressions.

## File Extension

- **File Extension**: `.irons`

## Keywords and Data Types

- **Keywords**: Start with an underscore (`_`), e.g., `_int`, `_float`, `_string`, `_bool`, `_if`, `_else`, `_while`, `_return`, `_input`, `_output`.
- **Data Types**:
  - `_int`: Integer type.
  - `_float`: Floating-point type.
  - `_string`: String type.
  - `_bool`: Boolean type (values: `true`, `false`).

## Identifiers

- **Variable Names**: Must start with a lowercase letter (a-z) and can be followed by letters, digits, or underscores.
- **Function Names**: Follow the same rules as variable names.

## Syntax Rules

### Comments

- **Single Line**: Start with `//` and continue to the end of the line.
- **Multi-line**: Start with `/*` and end with `*/`.

### Declarations

```irons
_int x = 10;
_float y = 3.14;
_string z = "Hello, World!";
_bool isTrue = true;