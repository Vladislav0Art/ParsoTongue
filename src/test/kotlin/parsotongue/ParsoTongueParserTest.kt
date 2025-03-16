package parsotongue

import ij.demo.parsotongue.lexer.LanguageLexer
import ij.demo.parsotongue.parser.LanguageParser
import org.junit.jupiter.api.Assertions.*
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
            lexerProvider = LanguageLexer(),
            parserProvider = LanguageParser()
        )
    }

    @Test
    fun parse() {
        val source = "1 + 2 + 12"
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