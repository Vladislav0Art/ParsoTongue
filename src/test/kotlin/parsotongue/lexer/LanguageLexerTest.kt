package parsotongue.lexer

import ij.demo.parsotongue.lexer.LanguageLexer
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LanguageLexerTest {
    private lateinit var lexer: Lexer

    @BeforeEach
    fun setUp() {
        lexer = LanguageLexer()
    }

    @Test
    fun `tokenize addition of two digits`() = runTest {
        val input = "1 + 2"
        val tokens = lexer.tokenize(input)

        val expectedTokens = listOf(
            // 1
            Token(
                type = TokenType.INTEGER,
                lexeme = "1",
                literal = 1,
                line = 0,
                column = 0,
                offset = 0,
            ),
            // +
            Token(
                type = TokenType.PLUS,
                lexeme = "+",
                line = 0,
                column = 2,
                offset = 2,
            ),
            // 2
            Token(
                type = TokenType.INTEGER,
                lexeme = "2",
                literal = 2,
                line = 0,
                column = 4,
                offset = 4,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize subtraction of two digits`() = runTest {
        val input = "5 - 3"
        val tokens = lexer.tokenize(input)

        val expectedTokens = listOf(
            Token(
                type = TokenType.INTEGER,
                lexeme = "5",
                literal = 5,
                line = 0,
                column = 0,
                offset = 0,
            ),
            Token(
                type = TokenType.MINUS,
                lexeme = "-",
                line = 0,
                column = 2,
                offset = 2,
            ),
            Token(
                type = TokenType.INTEGER,
                lexeme = "3",
                literal = 3,
                line = 0,
                column = 4,
                offset = 4,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize multiplication of two digits`() = runTest {
        val input = "4 * 6"
        val tokens = lexer.tokenize(input)

        val expectedTokens = listOf(
            Token(
                type = TokenType.INTEGER,
                lexeme = "4",
                literal = 4,
                line = 0,
                column = 0,
                offset = 0,
            ),
            Token(
                type = TokenType.MULTIPLY,
                lexeme = "*",
                line = 0,
                column = 2,
                offset = 2,
            ),
            Token(
                type = TokenType.INTEGER,
                lexeme = "6",
                literal = 6,
                line = 0,
                column = 4,
                offset = 4,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize division of two digits`() = runTest {
        val input = "8 / 2"
        val tokens = lexer.tokenize(input)

        val expectedTokens = listOf(
            Token(
                type = TokenType.INTEGER,
                lexeme = "8",
                literal = 8,
                line = 0,
                column = 0,
                offset = 0,
            ),
            Token(
                type = TokenType.DIVIDE,
                lexeme = "/",
                line = 0,
                column = 2,
                offset = 2,
            ),
            Token(
                type = TokenType.INTEGER,
                lexeme = "2",
                literal = 2,
                line = 0,
                column = 4,
                offset = 4,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize modulo of two digits`() = runTest {
        val input = "10 % 3"
        val tokens = lexer.tokenize(input)

        val expectedTokens = listOf(
            Token(
                type = TokenType.INTEGER,
                lexeme = "10",
                literal = 10,
                line = 0,
                column = 0,
                offset = 0,
            ),
            Token(
                type = TokenType.MODULO,
                lexeme = "%",
                line = 0,
                column = 3,
                offset = 3,
            ),
            Token(
                type = TokenType.INTEGER,
                lexeme = "3",
                literal = 3,
                line = 0,
                column = 5,
                offset = 5,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize simple variable declaration`() = runTest {
        val input = "var counter"
        val tokens = lexer.tokenize(input)

        val expectedTokens = listOf(
            Token(
                type = TokenType.VAR,
                lexeme = "var",
                line = 0,
                column = 0,
                offset = 0,
            ),
            Token(
                type = TokenType.IDENTIFIER,
                lexeme = "counter",
                line = 0,
                column = 4,
                offset = 4,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize variable declaration with assignment`() = runTest {
        val input = "var total = 42"
        val tokens = lexer.tokenize(input)

        val expectedTokens = listOf(
            Token(
                type = TokenType.VAR,
                lexeme = "var",
                line = 0,
                column = 0,
                offset = 0,
            ),
            Token(
                type = TokenType.IDENTIFIER,
                lexeme = "total",
                line = 0,
                column = 4,
                offset = 4,
            ),
            Token(
                type = TokenType.ASSIGN,
                lexeme = "=",
                line = 0,
                column = 10,
                offset = 10,
            ),
            Token(
                type = TokenType.INTEGER,
                lexeme = "42",
                literal = 42,
                line = 0,
                column = 12,
                offset = 12,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize less than and greater than operators`() = runTest {
        val input = "5 < 10 > 3"
        val tokens = lexer.tokenize(input)

        val expectedTokens = listOf(
            Token(
                type = TokenType.INTEGER,
                lexeme = "5",
                literal = 5,
                line = 0,
                column = 0,
                offset = 0,
            ),
            Token(
                type = TokenType.LESS_THAN,
                lexeme = "<",
                line = 0,
                column = 2,
                offset = 2,
            ),
            Token(
                type = TokenType.INTEGER,
                lexeme = "10",
                literal = 10,
                line = 0,
                column = 4,
                offset = 4,
            ),
            Token(
                type = TokenType.GREATER_THAN,
                lexeme = ">",
                line = 0,
                column = 7,
                offset = 7,
            ),
            Token(
                type = TokenType.INTEGER,
                lexeme = "3",
                literal = 3,
                line = 0,
                column = 9,
                offset = 9,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize less than or equal and greater than or equal operators`() = runTest {
        val input = "x <= 100 >= y"
        val tokens = lexer.tokenize(input)

        val expectedTokens = listOf(
            Token(
                type = TokenType.IDENTIFIER,
                lexeme = "x",
                line = 0,
                column = 0,
                offset = 0,
            ),
            Token(
                type = TokenType.LESS_EQUAL,
                lexeme = "<=",
                line = 0,
                column = 2,
                offset = 2,
            ),
            Token(
                type = TokenType.INTEGER,
                lexeme = "100",
                literal = 100,
                line = 0,
                column = 5,
                offset = 5,
            ),
            Token(
                type = TokenType.GREATER_EQUAL,
                lexeme = ">=",
                line = 0,
                column = 9,
                offset = 9,
            ),
            Token(
                type = TokenType.IDENTIFIER,
                lexeme = "y",
                line = 0,
                column = 12,
                offset = 12,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize equality and inequality operators`() = runTest {
        val input = "count == 0 != max"
        val tokens = lexer.tokenize(input)

        val expectedTokens = listOf(
            Token(
                type = TokenType.IDENTIFIER,
                lexeme = "count",
                line = 0,
                column = 0,
                offset = 0,
            ),
            Token(
                type = TokenType.EQUAL_EQUAL,
                lexeme = "==",
                line = 0,
                column = 6,
                offset = 6,
            ),
            Token(
                type = TokenType.INTEGER,
                lexeme = "0",
                literal = 0,
                line = 0,
                column = 9,
                offset = 9,
            ),
            Token(
                type = TokenType.NOT_EQUAL,
                lexeme = "!=",
                line = 0,
                column = 11,
                offset = 11,
            ),
            Token(
                type = TokenType.IDENTIFIER,
                lexeme = "max",
                line = 0,
                column = 14,
                offset = 14,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize void function declaration`() = runTest {
        val input = "function init() { }"
        val tokens = lexer.tokenize(input)

        val expectedTokens = listOf(
            Token(
                type = TokenType.FUNCTION,
                lexeme = "function",
                line = 0,
                column = 0,
                offset = 0,
            ),
            Token(
                type = TokenType.IDENTIFIER,
                lexeme = "init",
                line = 0,
                column = 9,
                offset = 9,
            ),
            Token(
                type = TokenType.LEFT_PAREN,
                lexeme = "(",
                line = 0,
                column = 13,
                offset = 13,
            ),
            Token(
                type = TokenType.RIGHT_PAREN,
                lexeme = ")",
                line = 0,
                column = 14,
                offset = 14,
            ),
            Token(
                type = TokenType.LEFT_BRACE,
                lexeme = "{",
                line = 0,
                column = 16,
                offset = 16,
            ),
            Token(
                type = TokenType.RIGHT_BRACE,
                lexeme = "}",
                line = 0,
                column = 18,
                offset = 18,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize function declaration with return value`() = runTest {
        val input = "function getValue() { return 42 }"
        val tokens = lexer.tokenize(input)

        val expectedTokens = listOf(
            Token(
                type = TokenType.FUNCTION,
                lexeme = "function",
                line = 0,
                column = 0,
                offset = 0,
            ),
            Token(
                type = TokenType.IDENTIFIER,
                lexeme = "getValue",
                line = 0,
                column = 9,
                offset = 9,
            ),
            Token(
                type = TokenType.LEFT_PAREN,
                lexeme = "(",
                line = 0,
                column = 17,
                offset = 17,
            ),
            Token(
                type = TokenType.RIGHT_PAREN,
                lexeme = ")",
                line = 0,
                column = 18,
                offset = 18,
            ),
            Token(
                type = TokenType.LEFT_BRACE,
                lexeme = "{",
                line = 0,
                column = 20,
                offset = 20,
            ),
            Token(
                type = TokenType.RETURN,
                lexeme = "return",
                line = 0,
                column = 22,
                offset = 22,
            ),
            Token(
                type = TokenType.INTEGER,
                lexeme = "42",
                literal = 42,
                line = 0,
                column = 29,
                offset = 29,
            ),
            Token(
                type = TokenType.RIGHT_BRACE,
                lexeme = "}",
                line = 0,
                column = 32,
                offset = 32,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize simple if statement`() = runTest {
        val input = "if (x > 0) { }"
        val tokens = lexer.tokenize(input)

        val expectedTokens = listOf(
            Token(
                type = TokenType.IF,
                lexeme = "if",
                line = 0,
                column = 0,
                offset = 0,
            ),
            Token(
                type = TokenType.LEFT_PAREN,
                lexeme = "(",
                line = 0,
                column = 3,
                offset = 3,
            ),
            Token(
                type = TokenType.IDENTIFIER,
                lexeme = "x",
                line = 0,
                column = 4,
                offset = 4,
            ),
            Token(
                type = TokenType.GREATER_THAN,
                lexeme = ">",
                line = 0,
                column = 6,
                offset = 6,
            ),
            Token(
                type = TokenType.INTEGER,
                lexeme = "0",
                literal = 0,
                line = 0,
                column = 8,
                offset = 8,
            ),
            Token(
                type = TokenType.RIGHT_PAREN,
                lexeme = ")",
                line = 0,
                column = 9,
                offset = 9,
            ),
            Token(
                type = TokenType.LEFT_BRACE,
                lexeme = "{",
                line = 0,
                column = 11,
                offset = 11,
            ),
            Token(
                type = TokenType.RIGHT_BRACE,
                lexeme = "}",
                line = 0,
                column = 13,
                offset = 13,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `tokenize if else statement`() = runTest {
        val input = "if (value == 100) { } else { }"
        val tokens = lexer.tokenize(input)

        val expectedTokens = listOf(
            Token(
                type = TokenType.IF,
                lexeme = "if",
                line = 0,
                column = 0,
                offset = 0,
            ),
            Token(
                type = TokenType.LEFT_PAREN,
                lexeme = "(",
                line = 0,
                column = 3,
                offset = 3,
            ),
            Token(
                type = TokenType.IDENTIFIER,
                lexeme = "value",
                line = 0,
                column = 4,
                offset = 4,
            ),
            Token(
                type = TokenType.EQUAL_EQUAL,
                lexeme = "==",
                line = 0,
                column = 10,
                offset = 10,
            ),
            Token(
                type = TokenType.INTEGER,
                lexeme = "100",
                literal = 100,
                line = 0,
                column = 13,
                offset = 13,
            ),
            Token(
                type = TokenType.RIGHT_PAREN,
                lexeme = ")",
                line = 0,
                column = 16,
                offset = 16,
            ),
            Token(
                type = TokenType.LEFT_BRACE,
                lexeme = "{",
                line = 0,
                column = 18,
                offset = 18,
            ),
            Token(
                type = TokenType.RIGHT_BRACE,
                lexeme = "}",
                line = 0,
                column = 20,
                offset = 20,
            ),
            Token(
                type = TokenType.ELSE,
                lexeme = "else",
                line = 0,
                column = 22,
                offset = 22,
            ),
            Token(
                type = TokenType.LEFT_BRACE,
                lexeme = "{",
                line = 0,
                column = 27,
                offset = 27,
            ),
            Token(
                type = TokenType.RIGHT_BRACE,
                lexeme = "}",
                line = 0,
                column = 29,
                offset = 29,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

}
