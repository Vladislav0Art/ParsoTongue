# ParsoTongue Parser

A parser and lexer implementation for a custom programming language.

## Language Specification

### Data Types
- **Integer**: Decimal integer values
- **String**: Text values enclosed in double quotes
- **Function**: Named blocks of code with parameters

### Constants
- Keywords: `var`, `function`, `if`, `else`, `return`
- Operators:
  - Arithmetic: `+`, `-`, `*`, `/`, `%`
  - Comparison: `==`, `!=`, `<`, `>`, `<=`, `>=`
  - Assignment: `=`
- Symbols: `(`, `)`, `{`, `}`, `;`, `,`

### Supported Operations

#### Variable Operations
- Variable declaration and initialization: `var name = value;`
- Variable reference in expressions
- String concatenation using `+`

#### Control Flow
- If statements: `if (condition) { ... }`
- If-else statements: `if (condition) { ... } else { ... }`
- Block scoping via `{ ... }`

#### Functions
- Function declaration with parameters: `function name(param1, param2) { ... }`
- Return statements: `return value;`
- Function calls with arguments

#### Expressions
- Arithmetic operations
- Comparison operations
- Variable references
- Function calls
- Literal values (integers and strings)

## Implementation Details

### Lexical Analysis

See [LanguageLexer.kt](src/main/kotlin/parsotongue/lexer/LanguageLexer.kt).

- Character-by-character scanning
- Token classification (see [Token.kt](src/main/kotlin/parsotongue/lexer/Token.kt))
- Position tracking (line and column)

### Parsing

See [LanguageParser.kt](src/main/kotlin/parsotongue/parser/LanguageParser.kt) and [ParsoTongueParser.kt](src/main/kotlin/parsotongue/ParsoTongueParser.kt).

- AST generation
- Operator precedence handling

### AST Structure
- `Program`: List of statements
- `Statements`: Variable declarations, function declarations, if statements, return statements
- `Expressions`: Binary operations, literals, variable references, function calls
- `Blocks`: Compound statements

### Example Use

```koltin
val program = parser.read("src/test/resources/test_program.pt")
// `program` will be AST of a program in the given file
```

## Performance Analysis

### Bottlenecks
1. **Lexer Performance**
   - Doing it linearly; we can do it by chunks in parallel (but it may effect line and column tracking; have to be careful with chunk splitting).

2. **Parser Performance**
   - Right now, it does deep recursive calls for nested structures

### Stress Testing
- Measure memory consumption and spent time in [StressTest.kt](./src/test/kotlin/parsotongue/StressTest.kt).

Report:
```log
19:13:39.270  -- Testing lexer with input size: 1000
19:13:39.274  -- Input length: 30348 characters
19:13:39.297  -- [Lexing 1000 items] performance metrics:
19:13:39.299  -- 	- Execution time: 21.95 ms
19:13:39.299  -- 	- Memory used: 1.83 MB
19:13:39.299  -- Generated 8880 tokens
19:13:39.304  -- Testing lexer with input size: 10000
19:13:39.304  -- Input length: 329472 characters
19:13:39.317  -- [Lexing 10000 items] performance metrics:
19:13:39.317  -- 	- Execution time: 12.26 ms
19:13:39.317  -- 	- Memory used: 11.00 MB
19:13:39.317  -- Generated 87180 tokens
19:13:39.371  -- Testing lexer with input size: 100000
19:13:39.371  -- Input length: 3636486 characters
19:13:39.432  -- [Lexing 100000 items] performance metrics:
19:13:39.433  -- 	- Execution time: 61.24 ms
19:13:39.433  -- 	- Memory used: 56.36 MB
19:13:39.433  -- Generated 870180 tokens
19:13:39.565  -- Testing lexer with input size: 500000
19:13:39.565  -- Input length: 19680496 characters
19:13:40.180  -- [Lexing 500000 items] performance metrics:
19:13:40.181  -- 	- Execution time: 615.28 ms
19:13:40.181  -- 	- Memory used: 161.00 MB
19:13:40.181  -- Generated 4350180 tokens
```


## Improvement Suggestions

1. **Performance Optimizations**
   - Lexing can potentially be parallelized (effects line:column positioning).
   - Likely, there are more efficient parser implementations
   - Do streaming instead of working with string to support large inputs

2. **Implementation**:
   - Error recovery is not supported: we can collect unexpected tokens into some `ErrorNode` until the next statement (semicolon `;`); once a new statement started, we can continue parsing normally.

