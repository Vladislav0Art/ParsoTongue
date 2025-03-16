package parsotongue.providers

import parsotongue.parser.LanguageParser
import parsotongue.lexer.Token
import parsotongue.parser.Parser

interface ParserProvider : Provider<List<Token>, Parser>

// implementors
object LanguageParserProvider : ParserProvider {
    override fun get(parameter: List<Token>): Parser = LanguageParser(parameter)
}