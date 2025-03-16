package parsotongue.lexer

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import parsotongue.lexer.TokenType.Identifier.IDENTIFIER
import parsotongue.lexer.TokenType.Keyword.*
import parsotongue.lexer.TokenType.Literal.INTEGER
import parsotongue.lexer.TokenType.Operator.*
import parsotongue.lexer.TokenType.Other.EOF
import parsotongue.lexer.TokenType.Symbol.*
import org.junit.jupiter.params.provider.MethodSource
import parsotongue.providers.LanguageLexerProvider
import java.util.stream.Stream
import kotlin.test.assertEquals


class LanguageLexerTest {
    private lateinit var lexerProvider: LanguageLexerProvider

    @BeforeEach
    fun setUp() {
        lexerProvider = LanguageLexerProvider
    }

    /**
     * Produces an implementation of [Lexer].
     */
    private fun lexer(source: String) = lexerProvider.get(source)

    @Test
    fun `tokenize addition of two digits`() = runTest {
        val input = "1 + 2"
        val tokens = lexer(input).tokenize()

        val expectedTokens = listOf(
            // 1
            Token(
                type = INTEGER,
                lexeme = "1",
                literal = 1,
                line = 1,
                column = 1,
                startOffset = 0,
            ),
            // +
            Token(
                type = PLUS,
                lexeme = "+",
                line = 1,
                column = 3,
                startOffset = 2,
            ),
            // 2
            Token(
                type = INTEGER,
                lexeme = "2",
                literal = 2,
                line = 1,
                column = 5,
                startOffset = 4,
            ),
            // EOF
            Token(
                type = EOF,
                lexeme = "",
                line = 1,
                column = 6,
                startOffset = 4,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize subtraction of two digits`() = runTest {
        val input = "5 - 3"
        val tokens = lexer(input).tokenize()

        val expectedTokens = listOf(
            Token(
                type = INTEGER,
                lexeme = "5",
                literal = 5,
                line = 1,
                column = 1,
                startOffset = 0,
            ),
            Token(
                type = MINUS,
                lexeme = "-",
                line = 1,
                column = 3,
                startOffset = 2,
            ),
            Token(
                type = INTEGER,
                lexeme = "3",
                literal = 3,
                line = 1,
                column = 5,
                startOffset = 4,
            ),
            Token(
                type = EOF,
                lexeme = "",
                line = 1,
                column = 6,
                startOffset = 4,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize multiplication of two integers`() = runTest {
        val input = "421 * 623"
        val tokens = lexer(input).tokenize()

        val expectedTokens = listOf(
            Token(
                type = INTEGER,
                lexeme = "421",
                literal = 421,
                line = 1,
                column = 1,
                startOffset = 0,
            ),
            Token(
                type = MULTIPLY,
                lexeme = "*",
                line = 1,
                column = 5,
                startOffset = 4,
            ),
            Token(
                type = INTEGER,
                lexeme = "623",
                literal = 623,
                line = 1,
                column = 7,
                startOffset = 6,
            ),
            Token(
                type = EOF,
                lexeme = "",
                line = 1,
                column = 10,
                startOffset = 6,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize division of two digits`() = runTest {
        val input = "8 / 2"
        val tokens = lexer(input).tokenize()

        val expectedTokens = listOf(
            Token(
                type = INTEGER,
                lexeme = "8",
                literal = 8,
                line = 1,
                column = 1,
                startOffset = 0,
            ),
            Token(
                type = DIVIDE,
                lexeme = "/",
                line = 1,
                column = 3,
                startOffset = 2,
            ),
            Token(
                type = INTEGER,
                lexeme = "2",
                literal = 2,
                line = 1,
                column = 5,
                startOffset = 4,
            ),
            Token(
                type = EOF,
                lexeme = "",
                line = 1,
                column = 6,
                startOffset = 4,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize modulo of two digits`() = runTest {
        val input = "10 % 3"
        val tokens = lexer(input).tokenize()

        val expectedTokens = listOf(
            Token(
                type = INTEGER,
                lexeme = "10",
                literal = 10,
                line = 1,
                column = 1,
                startOffset = 0,
            ),
            Token(
                type = MODULO,
                lexeme = "%",
                line = 1,
                column = 4,
                startOffset = 3,
            ),
            Token(
                type = INTEGER,
                lexeme = "3",
                literal = 3,
                line = 1,
                column = 6,
                startOffset = 5,
            ),
            Token(
                type = EOF,
                lexeme = "",
                line = 1,
                column = 7,
                startOffset = 5,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize simple variable declaration`() = runTest {
        val input = "var counter"
        val tokens = lexer(input).tokenize()

        val expectedTokens = listOf(
            Token(
                type = VAR,
                lexeme = "var",
                line = 1,
                column = 1,
                startOffset = 0,
            ),
            Token(
                type = IDENTIFIER,
                lexeme = "counter",
                line = 1,
                column = 5,
                startOffset = 4,
            ),
            Token(
                type = EOF,
                lexeme = "",
                line = 1,
                column = 12,
                startOffset = 4,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize variable declaration with assignment`() = runTest {
        val input = "var total = 42"
        val tokens = lexer(input).tokenize()

        val expectedTokens = listOf(
            Token(
                type = VAR,
                lexeme = "var",
                line = 1,
                column = 1,
                startOffset = 0,
            ),
            Token(
                type = IDENTIFIER,
                lexeme = "total",
                line = 1,
                column = 5,
                startOffset = 4,
            ),
            Token(
                type = ASSIGN,
                lexeme = "=",
                line = 1,
                column = 11,
                startOffset = 10,
            ),
            Token(
                type = INTEGER,
                lexeme = "42",
                literal = 42,
                line = 1,
                column = 13,
                startOffset = 12,
            ),
            Token(
                type = EOF,
                lexeme = "",
                line = 1,
                column = 15,
                startOffset = 12,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize less than and greater than operators`() = runTest {
        val input = "5 < 10 > 3"
        val tokens = lexer(input).tokenize()

        val expectedTokens = listOf(
            Token(
                type = INTEGER,
                lexeme = "5",
                literal = 5,
                line = 1,
                column = 1,
                startOffset = 0,
            ),
            Token(
                type = LESS_THAN,
                lexeme = "<",
                line = 1,
                column = 3,
                startOffset = 2,
            ),
            Token(
                type = INTEGER,
                lexeme = "10",
                literal = 10,
                line = 1,
                column = 5,
                startOffset = 4,
            ),
            Token(
                type = GREATER_THAN,
                lexeme = ">",
                line = 1,
                column = 8,
                startOffset = 7,
            ),
            Token(
                type = INTEGER,
                lexeme = "3",
                literal = 3,
                line = 1,
                column = 10,
                startOffset = 9,
            ),
            Token(
                type = EOF,
                lexeme = "",
                line = 1,
                column = 11,
                startOffset = 9,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize less than or equal and greater than or equal operators`() = runTest {
        val input = "x <= 100 >= y"
        val tokens = lexer(input).tokenize()

        val expectedTokens = listOf(
            Token(
                type = IDENTIFIER,
                lexeme = "x",
                line = 1,
                column = 1,
                startOffset = 0,
            ),
            Token(
                type = LESS_EQUAL,
                lexeme = "<=",
                line = 1,
                column = 3,
                startOffset = 2,
            ),
            Token(
                type = INTEGER,
                lexeme = "100",
                literal = 100,
                line = 1,
                column = 6,
                startOffset = 5,
            ),
            Token(
                type = GREATER_EQUAL,
                lexeme = ">=",
                line = 1,
                column = 10,
                startOffset = 9,
            ),
            Token(
                type = IDENTIFIER,
                lexeme = "y",
                line = 1,
                column = 13,
                startOffset = 12,
            ),
            Token(
                type = EOF,
                lexeme = "",
                line = 1,
                column = 14,
                startOffset = 12,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize equality and inequality operators`() = runTest {
        val input = "count == 0 != max"
        val tokens = lexer(input).tokenize()

        val expectedTokens = listOf(
            Token(
                type = IDENTIFIER,
                lexeme = "count",
                line = 1,
                column = 1,
                startOffset = 0,
            ),
            Token(
                type = EQUAL_EQUAL,
                lexeme = "==",
                line = 1,
                column = 7,
                startOffset = 6,
            ),
            Token(
                type = INTEGER,
                lexeme = "0",
                literal = 0,
                line = 1,
                column = 10,
                startOffset = 9,
            ),
            Token(
                type = NOT_EQUAL,
                lexeme = "!=",
                line = 1,
                column = 12,
                startOffset = 11,
            ),
            Token(
                type = IDENTIFIER,
                lexeme = "max",
                line = 1,
                column = 15,
                startOffset = 14,
            ),
            Token(
                type = EOF,
                lexeme = "",
                line = 1,
                column = 18,
                startOffset = 14,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize void function declaration`() = runTest {
        val input = "function init() { }"
        val tokens = lexer(input).tokenize()

        val expectedTokens = listOf(
            Token(
                type = FUNCTION,
                lexeme = "function",
                line = 1,
                column = 1,
                startOffset = 0,
            ),
            Token(
                type = IDENTIFIER,
                lexeme = "init",
                line = 1,
                column = 10,
                startOffset = 9,
            ),
            Token(
                type = LEFT_PAREN,
                lexeme = "(",
                line = 1,
                column = 14,
                startOffset = 13,
            ),
            Token(
                type = RIGHT_PAREN,
                lexeme = ")",
                line = 1,
                column = 15,
                startOffset = 14,
            ),
            Token(
                type = LEFT_BRACE,
                lexeme = "{",
                line = 1,
                column = 17,
                startOffset = 16,
            ),
            Token(
                type = RIGHT_BRACE,
                lexeme = "}",
                line = 1,
                column = 19,
                startOffset = 18,
            ),
            Token(
                type = EOF,
                lexeme = "",
                line = 1,
                column = 20,
                startOffset = 18,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize function declaration with return value`() = runTest {
        val input = "function getValue() { return 42 }"
        val tokens = lexer(input).tokenize()

        val expectedTokens = listOf(
            Token(
                type = FUNCTION,
                lexeme = "function",
                line = 1,
                column = 1,
                startOffset = 0,
            ),
            Token(
                type = IDENTIFIER,
                lexeme = "getValue",
                line = 1,
                column = 10,
                startOffset = 9,
            ),
            Token(
                type = LEFT_PAREN,
                lexeme = "(",
                line = 1,
                column = 18,
                startOffset = 17,
            ),
            Token(
                type = RIGHT_PAREN,
                lexeme = ")",
                line = 1,
                column = 19,
                startOffset = 18,
            ),
            Token(
                type = LEFT_BRACE,
                lexeme = "{",
                line = 1,
                column = 21,
                startOffset = 20,
            ),
            Token(
                type = RETURN,
                lexeme = "return",
                line = 1,
                column = 23,
                startOffset = 22,
            ),
            Token(
                type = INTEGER,
                lexeme = "42",
                literal = 42,
                line = 1,
                column = 30,
                startOffset = 29,
            ),
            Token(
                type = RIGHT_BRACE,
                lexeme = "}",
                line = 1,
                column = 33,
                startOffset = 32,
            ),
            Token(
                type = EOF,
                lexeme = "",
                line = 1,
                column = 34,
                startOffset = 32,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize simple if statement`() = runTest {
        val input = "if (x > 0) { }"
        val tokens = lexer(input).tokenize()

        val expectedTokens = listOf(
            Token(
                type = IF,
                lexeme = "if",
                line = 1,
                column = 1,
                startOffset = 0,
            ),
            Token(
                type = LEFT_PAREN,
                lexeme = "(",
                line = 1,
                column = 4,
                startOffset = 3,
            ),
            Token(
                type = IDENTIFIER,
                lexeme = "x",
                line = 1,
                column = 5,
                startOffset = 4,
            ),
            Token(
                type = GREATER_THAN,
                lexeme = ">",
                line = 1,
                column = 7,
                startOffset = 6,
            ),
            Token(
                type = INTEGER,
                lexeme = "0",
                literal = 0,
                line = 1,
                column = 9,
                startOffset = 8,
            ),
            Token(
                type = RIGHT_PAREN,
                lexeme = ")",
                line = 1,
                column = 10,
                startOffset = 9,
            ),
            Token(
                type = LEFT_BRACE,
                lexeme = "{",
                line = 1,
                column = 12,
                startOffset = 11,
            ),
            Token(
                type = RIGHT_BRACE,
                lexeme = "}",
                line = 1,
                column = 14,
                startOffset = 13,
            ),
            Token(
                type = EOF,
                lexeme = "",
                line = 1,
                column = 15,
                startOffset = 13,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize if else statement`() = runTest {
        val input = "if (value == 100) { } else { }"
        val tokens = lexer(input).tokenize()

        val expectedTokens = listOf(
            Token(
                type = IF,
                lexeme = "if",
                line = 1,
                column = 1,
                startOffset = 0,
            ),
            Token(
                type = LEFT_PAREN,
                lexeme = "(",
                line = 1,
                column = 4,
                startOffset = 3,
            ),
            Token(
                type = IDENTIFIER,
                lexeme = "value",
                line = 1,
                column = 5,
                startOffset = 4,
            ),
            Token(
                type = EQUAL_EQUAL,
                lexeme = "==",
                line = 1,
                column = 11,
                startOffset = 10,
            ),
            Token(
                type = INTEGER,
                lexeme = "100",
                literal = 100,
                line = 1,
                column = 14,
                startOffset = 13,
            ),
            Token(
                type = RIGHT_PAREN,
                lexeme = ")",
                line = 1,
                column = 17,
                startOffset = 16,
            ),
            Token(
                type = LEFT_BRACE,
                lexeme = "{",
                line = 1,
                column = 19,
                startOffset = 18,
            ),
            Token(
                type = RIGHT_BRACE,
                lexeme = "}",
                line = 1,
                column = 21,
                startOffset = 20,
            ),
            Token(
                type = ELSE,
                lexeme = "else",
                line = 1,
                column = 23,
                startOffset = 22,
            ),
            Token(
                type = LEFT_BRACE,
                lexeme = "{",
                line = 1,
                column = 28,
                startOffset = 27,
            ),
            Token(
                type = RIGHT_BRACE,
                lexeme = "}",
                line = 1,
                column = 30,
                startOffset = 29,
            ),
            Token(
                type = EOF,
                lexeme = "",
                line = 1,
                column = 31,
                startOffset = 29,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    /**
     * In more complex inputs we assert only token types and order, not positioning
     */

    @Test
    fun `tokenize if else-if else statement`() = runTest {
        // arrange
        val input = """
            if (x < 0) {
                return -1
            } else if (x > 0) {
                return 1
            } else {
                return 0
            }
        """.trimIndent()

        // act
        val tokens = lexer(input).tokenize()

        // assert
        // For complex inputs we verify only token types
        val expectedTypes = listOf(
            IF, LEFT_PAREN, IDENTIFIER, LESS_THAN, INTEGER, RIGHT_PAREN,
            LEFT_BRACE, RETURN, MINUS, INTEGER, RIGHT_BRACE,
            ELSE, IF, LEFT_PAREN, IDENTIFIER, GREATER_THAN, INTEGER, RIGHT_PAREN,
            LEFT_BRACE, RETURN, INTEGER, RIGHT_BRACE,
            ELSE, LEFT_BRACE, RETURN, INTEGER, RIGHT_BRACE,
            EOF
        )

        // verify the number of tokens
        assertEquals(expectedTypes.size, tokens.size)

        // verify token types
        expectedTypes.forEachIndexed { index, expectedType ->
            assertEquals(expectedType, tokens[index].type)
        }
    }

    @Test
    fun `tokenize several consequent function calls`() = runTest {
        // arrange
        val input = """
            initialize()
            process(getValue(), calculateSum(10, 20))
            cleanup(getStatus())
        """.trimIndent()

        // act
        val tokens = lexer(input).tokenize()

        // assert
        val expectedTypes = listOf(
            // initialize()
            IDENTIFIER, LEFT_PAREN, RIGHT_PAREN,
            // process(getValue(), calculateSum(10, 20))
            IDENTIFIER, LEFT_PAREN,                           // process(
            IDENTIFIER, LEFT_PAREN, RIGHT_PAREN,             // getValue()
            COMMA,                                           // ,
            IDENTIFIER, LEFT_PAREN, INTEGER, COMMA, INTEGER, RIGHT_PAREN,  // calculateSum(10, 20)
            RIGHT_PAREN,                                     // )
            // cleanup(getStatus())
            IDENTIFIER, LEFT_PAREN,                          // cleanup(
            IDENTIFIER, LEFT_PAREN, RIGHT_PAREN,            // getStatus()
            RIGHT_PAREN,                                     // )
            EOF
        )

        // verify the number of tokens
        assertEquals(expectedTypes.size, tokens.size)

        // verify token types
        expectedTypes.forEachIndexed { index, expectedType ->
            assertEquals(expectedType, tokens[index].type)
        }
    }

    @Test
    fun `tokenize multiline function declaration`() = runTest {
        // arrange
        val input = """
            function calculateAverage(
                firstNumber,
                secondNumber,
                thirdNumber
            ) {
                var sum = firstNumber +
                    secondNumber +
                    thirdNumber
                return sum
            }
        """.trimIndent()

        // act
        val tokens = lexer(input).tokenize()

        // assert
        val expectedTypes = listOf(
            // function declaration
            FUNCTION, IDENTIFIER, LEFT_PAREN,
            // parameters
            IDENTIFIER, COMMA,
            IDENTIFIER, COMMA,
            IDENTIFIER,
            RIGHT_PAREN, LEFT_BRACE,
            // variable declaration
            VAR, IDENTIFIER, ASSIGN,
            // sum expression
            IDENTIFIER, PLUS,
            IDENTIFIER, PLUS,
            IDENTIFIER,
            // return statement
            RETURN, IDENTIFIER,
            RIGHT_BRACE,
            EOF
        )

        // verify the number of tokens
        assertEquals(expectedTypes.size, tokens.size)

        // verify token types
        expectedTypes.forEachIndexed { index, expectedType ->
            assertEquals(expectedType, tokens[index].type)
        }
    }

    @Test
    fun `tokenize several multiline variable declarations`() = runTest {
        // arrange
        val input = """
            var firstNumber = 
                10 + 
                20 * 
                30
            var secondNumber = 
                getValue() + 
                calculateSum(
                    40,
                    50
                )
            var result = 
                firstNumber + 
                secondNumber
        """.trimIndent()

        // act
        val tokens = lexer(input).tokenize()

        // assert
        val expectedTypes = listOf(
            // first variable declaration
            VAR, IDENTIFIER, ASSIGN,
            INTEGER, PLUS,
            INTEGER, MULTIPLY,
            INTEGER,

            // second variable declaration
            VAR, IDENTIFIER, ASSIGN,
            IDENTIFIER, LEFT_PAREN, RIGHT_PAREN, PLUS,
            IDENTIFIER, LEFT_PAREN,
            INTEGER, COMMA,
            INTEGER,
            RIGHT_PAREN,

            // third variable declaration
            VAR, IDENTIFIER, ASSIGN,
            IDENTIFIER, PLUS,
            IDENTIFIER,

            EOF
        )

        // verify the number of tokens
        assertEquals(expectedTypes.size, tokens.size)

        // verify token types
        expectedTypes.forEachIndexed { index, expectedType ->
            assertEquals(expectedType, tokens[index].type)
        }
    }

    @ParameterizedTest(name = "Tokenize {0} {1} {2}")
    @MethodSource("arithmeticOperationsProvider")
    @DisplayName("Tokenize multiline arithmetic operations")
    fun `tokenize multiline arithmetic operations`(
        firstOperand: Int,
        operator: String,
        secondOperand: Int,
        expectedTokenTypes: List<TokenType>
    ) = runTest {
        // arrange
        val input = """
            $firstOperand
               $operator
            $secondOperand
        """.trimIndent()

        // act
        val tokens = lexer(input).tokenize()

        // assert
        // verify the number of tokens (excluding EOF)
        assertEquals(expectedTokenTypes.size, tokens.size - 1)

        // verify each token type
        expectedTokenTypes.forEachIndexed { index, expectedType ->
            assertEquals(expectedType, tokens[index].type)
        }

        // verify the positions of tokens
        assertEquals(1, tokens[0].line)
        assertEquals(1, tokens[0].column)

        assertEquals(2, tokens[1].line)
        assertEquals(4, tokens[1].column)

        assertEquals(3, tokens[2].line)
        assertEquals(1, tokens[2].column)

        // verify the token values
        assertEquals(firstOperand.toString(), tokens[0].lexeme)
        assertEquals(operator, tokens[1].lexeme)
        assertEquals(secondOperand.toString(), tokens[2].lexeme)
    }

    @Test
    fun `tokenize multiline nested expressions`() = runTest {
        // arrange
        val input = """
            var result = (
                (10 + 20) *
                    (30 + 
                        40
                    )
            ) / 2
        """.trimIndent()

        // act
        val tokens = lexer(input).tokenize()

        // assert
        val expectedTypes = listOf(
            VAR, IDENTIFIER, ASSIGN,
            LEFT_PAREN,
            LEFT_PAREN, INTEGER, PLUS, INTEGER, RIGHT_PAREN,
            MULTIPLY,
            LEFT_PAREN, INTEGER, PLUS,
            INTEGER,
            RIGHT_PAREN,
            RIGHT_PAREN,
            DIVIDE, INTEGER,
            EOF
        )

        // verify the number of tokens
        assertEquals(expectedTypes.size, tokens.size)

        // verify token types
        expectedTypes.forEachIndexed { index, expectedType ->
            assertEquals(expectedType, tokens[index].type)
        }
    }

    @Test
    fun `tokenize multiline complex expression with function calls`() = runTest {
        // arrange
        val input = """
            var total = 
                calculateSum(
                    getValue(
                        10,
                        20
                    ),
                    processNumber(
                        30
                    )
                ) +
                getDefaultValue()
        """.trimIndent()

        // act
        val tokens = lexer(input).tokenize()

        // assert
        val expectedTypes = listOf(
            VAR, IDENTIFIER, ASSIGN,
            IDENTIFIER, LEFT_PAREN,
            IDENTIFIER, LEFT_PAREN,
            INTEGER, COMMA,
            INTEGER,
            RIGHT_PAREN, COMMA,
            IDENTIFIER, LEFT_PAREN,
            INTEGER,
            RIGHT_PAREN,
            RIGHT_PAREN, PLUS,
            IDENTIFIER, LEFT_PAREN, RIGHT_PAREN,
            EOF
        )

        // verify the number of tokens
        assertEquals(expectedTypes.size, tokens.size)

        // verify token types
        expectedTypes.forEachIndexed { index, expectedType ->
            assertEquals(expectedType, tokens[index].type)
        }
    }


    companion object {
        @JvmStatic
        fun arithmeticOperationsProvider(): Stream<Array<Any>> {
            return Stream.of(
                /**
                 * Format: `firstOperand`, `operator`, `secondOperand`, `expectedTokenTypes`
                 */
                arrayOf(123, "+", 234, listOf(INTEGER, PLUS, INTEGER)),
                arrayOf(456, "-", 789, listOf(INTEGER, MINUS, INTEGER)),
                arrayOf(55, "*", 11, listOf(INTEGER, MULTIPLY, INTEGER)),
                arrayOf(100, "/", 25, listOf(INTEGER, DIVIDE, INTEGER)),
                arrayOf(101, "%", 10, listOf(INTEGER, MODULO, INTEGER)),
                arrayOf(0, "==", 0, listOf(INTEGER, EQUAL_EQUAL, INTEGER)),
                arrayOf(42, "!=", 43, listOf(INTEGER, NOT_EQUAL, INTEGER)),
                arrayOf(10, "<", 20, listOf(INTEGER, LESS_THAN, INTEGER)),
                arrayOf(30, ">", 20, listOf(INTEGER, GREATER_THAN, INTEGER)),
                arrayOf(5, "<=", 10, listOf(INTEGER, LESS_EQUAL, INTEGER)),
                arrayOf(15, ">=", 10, listOf(INTEGER, GREATER_EQUAL, INTEGER))
            )
        }
    }
}
