package parsotongue.parser

import parsotongue.lexer.Token
import parsotongue.parser.ASTNode.Program


interface Parser {
    fun parse(tokens: List<Token>): Program
}