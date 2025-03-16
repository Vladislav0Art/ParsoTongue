package parsotongue.providers

import parsotongue.lexer.LanguageLexer
import parsotongue.lexer.Lexer

interface LexerProvider : Provider<String, Lexer>

object LanguageLexerProvider : LexerProvider {
    override fun get(parameter: String): Lexer = LanguageLexer(parameter)
}

