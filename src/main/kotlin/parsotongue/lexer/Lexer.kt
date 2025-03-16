package parsotongue.lexer

interface Lexer {
    fun tokenize(text: String): List<Token>
}