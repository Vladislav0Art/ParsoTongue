package parsotongue

import parsotongue.providers.LanguageLexerProvider
import parsotongue.providers.LanguageParserProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import parsotongue.lexer.TokenType
import parsotongue.parser.BinaryExpression
import parsotongue.parser.ExpressionStatement
import parsotongue.parser.IntegerLiteral
import parsotongue.parser.Program

class ParsoTongueParserTest {
    private lateinit var parser: ParsoTongueParser

    @BeforeEach
    fun setUp() {
        parser = ParsoTongueParser(
            lexerProvider = LanguageLexerProvider,
            parserProvider = LanguageParserProvider,
        )
    }

    @Test
    fun parse() {
        val source = "1 + 2 + 12;"
        val program = parser.parse(source)

        /**
         *      +
         *    /  \
         *   +    12
         *  / \
         * 1  2
         */
        val expectedProgram = Program(listOf(
            ExpressionStatement(
                expression = BinaryExpression(
                    left = BinaryExpression(
                        left = IntegerLiteral(1),
                        operator = TokenType.Operator.PLUS,
                        right = IntegerLiteral(2),
                    ),
                    operator = TokenType.Operator.PLUS,
                    right = IntegerLiteral(12),
                )
            )
        ))

        // TODO: how to compare the programs?
        assertEquals(expectedProgram, program)
    }

}