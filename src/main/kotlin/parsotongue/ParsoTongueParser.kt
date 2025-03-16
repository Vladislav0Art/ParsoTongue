package parsotongue

import parsotongue.providers.LexerProvider
import parsotongue.providers.ParserProvider
import parsotongue.parser.Program


class ParsoTongueParser(
    private val lexerProvider: LexerProvider,
    private val parserProvider: ParserProvider,
) {
    fun parse(source: String): Program {
        val lexer = lexerProvider.get(source)
        val tokens = lexer.tokenize()

        val parser = parserProvider.get(tokens)
        val program  = parser.parse()

        return program
    }
}