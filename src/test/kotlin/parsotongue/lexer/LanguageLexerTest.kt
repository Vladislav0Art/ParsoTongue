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
                type = TokenType.PLUS,
                lexeme = "2",
                literal = 2,
                line = 0,
                column = 3,
                offset = 3,
            )
        )
        assertEquals(expectedTokens, tokens)
    }

}