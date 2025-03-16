package parsotongue.lexer

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import parsotongue.lexer.TokenType.Identifier.IDENTIFIER
import parsotongue.lexer.TokenType.Keyword.*
import parsotongue.lexer.TokenType.Literal.INTEGER
import parsotongue.lexer.TokenType.Operator.*
import parsotongue.lexer.TokenType.Symbol.*
import parsotongue.providers.LanguageLexerProvider
import kotlin.test.assertEquals


class LanguageLexerTest {
    private lateinit var lexerProvider: LanguageLexerProvider

    @BeforeEach
    fun setUp() {
        lexerProvider = LanguageLexerProvider
    }

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
                line = 0,
                column = 0,
                startOffset = 0,
            ),
            // +
            Token(
                type = PLUS,
                lexeme = "+",
                line = 0,
                column = 2,
                startOffset = 2,
            ),
            // 2
            Token(
                type = INTEGER,
                lexeme = "2",
                literal = 2,
                line = 0,
                column = 4,
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
                line = 0,
                column = 0,
                startOffset = 0,
            ),
            Token(
                type = MINUS,
                lexeme = "-",
                line = 0,
                column = 2,
                startOffset = 2,
            ),
            Token(
                type = INTEGER,
                lexeme = "3",
                literal = 3,
                line = 0,
                column = 4,
                startOffset = 4,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize multiplication of two digits`() = runTest {
        val input = "4 * 6"
        val tokens = lexer(input).tokenize()

        val expectedTokens = listOf(
            Token(
                type = INTEGER,
                lexeme = "4",
                literal = 4,
                line = 0,
                column = 0,
                startOffset = 0,
            ),
            Token(
                type = MULTIPLY,
                lexeme = "*",
                line = 0,
                column = 2,
                startOffset = 2,
            ),
            Token(
                type = INTEGER,
                lexeme = "6",
                literal = 6,
                line = 0,
                column = 4,
                startOffset = 4,
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
                line = 0,
                column = 0,
                startOffset = 0,
            ),
            Token(
                type = DIVIDE,
                lexeme = "/",
                line = 0,
                column = 2,
                startOffset = 2,
            ),
            Token(
                type = INTEGER,
                lexeme = "2",
                literal = 2,
                line = 0,
                column = 4,
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
                line = 0,
                column = 0,
                startOffset = 0,
            ),
            Token(
                type = MODULO,
                lexeme = "%",
                line = 0,
                column = 3,
                startOffset = 3,
            ),
            Token(
                type = INTEGER,
                lexeme = "3",
                literal = 3,
                line = 0,
                column = 5,
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
                line = 0,
                column = 0,
                startOffset = 0,
            ),
            Token(
                type = IDENTIFIER,
                lexeme = "counter",
                line = 0,
                column = 4,
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
                line = 0,
                column = 0,
                startOffset = 0,
            ),
            Token(
                type = IDENTIFIER,
                lexeme = "total",
                line = 0,
                column = 4,
                startOffset = 4,
            ),
            Token(
                type = ASSIGN,
                lexeme = "=",
                line = 0,
                column = 10,
                startOffset = 10,
            ),
            Token(
                type = INTEGER,
                lexeme = "42",
                literal = 42,
                line = 0,
                column = 12,
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
                line = 0,
                column = 0,
                startOffset = 0,
            ),
            Token(
                type = LESS_THAN,
                lexeme = "<",
                line = 0,
                column = 2,
                startOffset = 2,
            ),
            Token(
                type = INTEGER,
                lexeme = "10",
                literal = 10,
                line = 0,
                column = 4,
                startOffset = 4,
            ),
            Token(
                type = GREATER_THAN,
                lexeme = ">",
                line = 0,
                column = 7,
                startOffset = 7,
            ),
            Token(
                type = INTEGER,
                lexeme = "3",
                literal = 3,
                line = 0,
                column = 9,
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
                line = 0,
                column = 0,
                startOffset = 0,
            ),
            Token(
                type = LESS_EQUAL,
                lexeme = "<=",
                line = 0,
                column = 2,
                startOffset = 2,
            ),
            Token(
                type = INTEGER,
                lexeme = "100",
                literal = 100,
                line = 0,
                column = 5,
                startOffset = 5,
            ),
            Token(
                type = GREATER_EQUAL,
                lexeme = ">=",
                line = 0,
                column = 9,
                startOffset = 9,
            ),
            Token(
                type = IDENTIFIER,
                lexeme = "y",
                line = 0,
                column = 12,
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
                line = 0,
                column = 0,
                startOffset = 0,
            ),
            Token(
                type = EQUAL_EQUAL,
                lexeme = "==",
                line = 0,
                column = 6,
                startOffset = 6,
            ),
            Token(
                type = INTEGER,
                lexeme = "0",
                literal = 0,
                line = 0,
                column = 9,
                startOffset = 9,
            ),
            Token(
                type = NOT_EQUAL,
                lexeme = "!=",
                line = 0,
                column = 11,
                startOffset = 11,
            ),
            Token(
                type = IDENTIFIER,
                lexeme = "max",
                line = 0,
                column = 14,
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
                line = 0,
                column = 0,
                startOffset = 0,
            ),
            Token(
                type = IDENTIFIER,
                lexeme = "init",
                line = 0,
                column = 9,
                startOffset = 9,
            ),
            Token(
                type = LEFT_PAREN,
                lexeme = "(",
                line = 0,
                column = 13,
                startOffset = 13,
            ),
            Token(
                type = RIGHT_PAREN,
                lexeme = ")",
                line = 0,
                column = 14,
                startOffset = 14,
            ),
            Token(
                type = LEFT_BRACE,
                lexeme = "{",
                line = 0,
                column = 16,
                startOffset = 16,
            ),
            Token(
                type = RIGHT_BRACE,
                lexeme = "}",
                line = 0,
                column = 18,
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
                line = 0,
                column = 0,
                startOffset = 0,
            ),
            Token(
                type = IDENTIFIER,
                lexeme = "getValue",
                line = 0,
                column = 9,
                startOffset = 9,
            ),
            Token(
                type = LEFT_PAREN,
                lexeme = "(",
                line = 0,
                column = 17,
                startOffset = 17,
            ),
            Token(
                type = RIGHT_PAREN,
                lexeme = ")",
                line = 0,
                column = 18,
                startOffset = 18,
            ),
            Token(
                type = LEFT_BRACE,
                lexeme = "{",
                line = 0,
                column = 20,
                startOffset = 20,
            ),
            Token(
                type = RETURN,
                lexeme = "return",
                line = 0,
                column = 22,
                startOffset = 22,
            ),
            Token(
                type = INTEGER,
                lexeme = "42",
                literal = 42,
                line = 0,
                column = 29,
                startOffset = 29,
            ),
            Token(
                type = RIGHT_BRACE,
                lexeme = "}",
                line = 0,
                column = 32,
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
                line = 0,
                column = 0,
                startOffset = 0,
            ),
            Token(
                type = LEFT_PAREN,
                lexeme = "(",
                line = 0,
                column = 3,
                startOffset = 3,
            ),
            Token(
                type = IDENTIFIER,
                lexeme = "x",
                line = 0,
                column = 4,
                startOffset = 4,
            ),
            Token(
                type = GREATER_THAN,
                lexeme = ">",
                line = 0,
                column = 6,
                startOffset = 6,
            ),
            Token(
                type = INTEGER,
                lexeme = "0",
                literal = 0,
                line = 0,
                column = 8,
                startOffset = 8,
            ),
            Token(
                type = RIGHT_PAREN,
                lexeme = ")",
                line = 0,
                column = 9,
                startOffset = 9,
            ),
            Token(
                type = LEFT_BRACE,
                lexeme = "{",
                line = 0,
                column = 11,
                startOffset = 11,
            ),
            Token(
                type = RIGHT_BRACE,
                lexeme = "}",
                line = 0,
                column = 13,
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
                line = 0,
                column = 0,
                startOffset = 0,
            ),
            Token(
                type = LEFT_PAREN,
                lexeme = "(",
                line = 0,
                column = 3,
                startOffset = 3,
            ),
            Token(
                type = IDENTIFIER,
                lexeme = "value",
                line = 0,
                column = 4,
                startOffset = 4,
            ),
            Token(
                type = EQUAL_EQUAL,
                lexeme = "==",
                line = 0,
                column = 10,
                startOffset = 10,
            ),
            Token(
                type = INTEGER,
                lexeme = "100",
                literal = 100,
                line = 0,
                column = 13,
                startOffset = 13,
            ),
            Token(
                type = RIGHT_PAREN,
                lexeme = ")",
                line = 0,
                column = 16,
                startOffset = 16,
            ),
            Token(
                type = LEFT_BRACE,
                lexeme = "{",
                line = 0,
                column = 18,
                startOffset = 18,
            ),
            Token(
                type = RIGHT_BRACE,
                lexeme = "}",
                line = 0,
                column = 20,
                startOffset = 20,
            ),
            Token(
                type = ELSE,
                lexeme = "else",
                line = 0,
                column = 22,
                startOffset = 22,
            ),
            Token(
                type = LEFT_BRACE,
                lexeme = "{",
                line = 0,
                column = 27,
                startOffset = 27,
            ),
            Token(
                type = RIGHT_BRACE,
                lexeme = "}",
                line = 0,
                column = 29,
                startOffset = 29,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

}
