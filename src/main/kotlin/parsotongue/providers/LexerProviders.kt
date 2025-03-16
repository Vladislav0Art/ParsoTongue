package parsotongue.providers

import parsotongue.lexer.LanguageLexer
import parsotongue.lexer.Lexer
import parsotongue.lexer.ParallelLexer

interface LexerProvider : Provider<String, Lexer>

object LanguageLexerProvider : LexerProvider {
    override fun get(parameter: String): Lexer = LanguageLexer(parameter)
}

object ParallelLexerProvider : LexerProvider {
    override fun get(parameter: String): Lexer = ParallelLexer(parameter)
}