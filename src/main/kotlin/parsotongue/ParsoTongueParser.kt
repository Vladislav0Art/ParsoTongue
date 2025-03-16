package parsotongue

import parsotongue.lexer.Lexer
import parsotongue.parser.ASTNode.Program
import parsotongue.parser.Parser

class ParsoTongueParser(
    private val lexer: Lexer,
    private val parser: Parser
    // TODO: add settings?
) {
    fun parse(source: String): Program {
        val tokens = lexer.tokenize(source)
        val program  = parser.parse(tokens)

        return program
    }
}