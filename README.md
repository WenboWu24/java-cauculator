# Calculator

A Java Swing-based calculator application with full expression input and scientific calculation support.

## Features

- **Expression Input**: Enter complete math expressions and press equals to calculate
- **Basic Operations**: Addition (+), Subtraction (-), Multiplication (×), Division (÷)
- **Scientific Functions**: sin, cos, tan, log, ln
- **Parentheses Support**: Use parentheses to change operation precedence
- **Precision Handling**: Automatically handles floating-point precision issues like `5÷3×6=10`
- **Input Validation**: Detects invalid expression formats

## Supported Expressions

```
# Basic Operations
5+3×2      → 11
(5+3)×2    → 16
6*(-2)     → -12

# Scientific Functions
sin(0)     → 0
cos(0)     → 1
tan(0)     → 0
log(100)   → 2
ln(e)      → 1

# Constants
π          → 3.141592653589793
e          → 2.718281828459045
```

## Input Rules

- Numbers must be preceded by an operator or left parenthesis (e.g., `6*(-2)`)
- Decimal points must be preceded by a digit (e.g., `0.3`, not `.3`)
- Function names must come before numbers (e.g., `sin(90)`, not `90sin`)
- Two operators cannot be consecutive (e.g., `6*-2` is invalid)

## Build & Run

```bash
# Compile
javac -d bin src/*.java

# Run
java -cp bin CalculatorApp
```

## Project Structure

```
calculator/
├── src/
│   ├── CalculatorApp.java         # Main entry point
│   ├── CalculatorController.java  # Controller
│   ├── CalculatorModel.java      # Model (expression parsing & calculation)
│   └── CalculatorView.java       # View (UI display)
└── README.md
```

## Usage

1. Click number and operator buttons to enter expressions
2. Keyboard input is also supported (numbers, operators, parentheses)
3. Press `=` or Enter to calculate
4. Press `C` to clear all
5. Press `←` to delete last character

## Implementation

- **Expression Parsing**: Infix to Postfix conversion (Reverse Polish Notation)
- **Precision**: BigDecimal for high-precision calculation
- **UI Framework**: Java Swing
- **Design Pattern**: MVC (Model-View-Controller)
