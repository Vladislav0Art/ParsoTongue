package parsotongue

import parsotongue.lexer.Lexer
import parsotongue.lexer.Token
import parsotongue.parser.Program
import parsotongue.parser.Parser

class ParsoTongueParser(
    // TODO: make interfaces
    private val lexerProvider: (String) -> Lexer,
    private val parserProvider: (List<Token>) -> Parser,
) {
    fun parse(source: String): Program {
        val lexer = lexerProvider(source)
        val tokens = lexer.tokenize(source)

        val parser = parserProvider(tokens)
        val program  = parser.parse()

        return program
    }
}