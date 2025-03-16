package parsotongue

import parsotongue.providers.LexerProvider
import parsotongue.providers.ParserProvider
import parsotongue.parser.Program
import java.io.File


class ParsoTongueParser(
    private val lexerProvider: LexerProvider,
    private val parserProvider: ParserProvider,
) {
    suspend fun parse(source: String): Program {
        val lexer = lexerProvider.get(source)
        val tokens = lexer.tokenize()

        val parser = parserProvider.get(tokens)
        val program  = parser.parse()

        return program
    }

    suspend fun parse(file: File): Program = parse(file.readText())

    suspend fun read(filepath: String): Program = parse(File(filepath))
}