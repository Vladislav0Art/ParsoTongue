package ij.demo.parsotongue.parser

import parsotongue.lexer.Token
import parsotongue.parser.Parser
import parsotongue.parser.Program


class LanguageParser(private val tokens: List<Token>) : Parser {
    override fun parse(): Program {
        TODO("Not yet implemented")
    }
}