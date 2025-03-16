package parsotongue.parser

import parsotongue.lexer.Token


interface Parser {
    fun parse(tokens: List<Token>): Program
}