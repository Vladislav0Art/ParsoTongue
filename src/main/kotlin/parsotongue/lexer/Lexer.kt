package parsotongue.lexer

interface Lexer {
    suspend fun tokenize(): List<Token>
}