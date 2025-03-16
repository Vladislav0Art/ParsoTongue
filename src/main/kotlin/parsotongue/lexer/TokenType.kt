package parsotongue.lexer

enum class TokenType {
    // keywords
    VAR, FUNCTION, IF, ELSE, RETURN,

    // operators
    PLUS, MINUS, MULTIPLY, DIVIDE, MODULO,
    LESS_THAN, GREATER_THAN, LESS_EQUAL, GREATER_EQUAL, EQUAL_EQUAL, NOT_EQUAL,

    // symbols
    ASSIGN, SEMICOLON, LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE, COMMA,

    // literals
    INTEGER, STRING,

    // identifiers
    IDENTIFIER,

    // other
    EOF
}