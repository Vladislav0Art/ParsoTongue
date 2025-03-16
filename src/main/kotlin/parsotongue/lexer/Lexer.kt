package parsotongue.lexer

interface Lexer {
    fun tokenize(): List<Token>
}