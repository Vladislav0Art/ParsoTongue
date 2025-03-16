package parsotongue.lexer


sealed class TokenType {
    override fun toString(): String = this::class.simpleName.orEmpty()

    // keywords
    sealed class Keyword : TokenType() {
        object VAR : Keyword()
        object FUNCTION : Keyword()
        object IF : Keyword()
        object ELSE : Keyword()
        object RETURN : Keyword()
    }

    sealed class Operator : TokenType() {
        object PLUS : Operator()
        object MINUS : Operator()
        object MULTIPLY : Operator()
        object DIVIDE : Operator()
        object MODULO : Operator()
        object LESS_THAN : Operator()
        object GREATER_THAN : Operator()
        object LESS_EQUAL : Operator()
        object GREATER_EQUAL : Operator()
        object EQUAL_EQUAL : Operator()
        object NOT_EQUAL : Operator()
    }

    // symbols
    sealed class Symbol : TokenType() {
        object ASSIGN : Symbol()
        object SEMICOLON : Symbol()
        object LEFT_PAREN : Symbol()
        object RIGHT_PAREN : Symbol()
        object LEFT_BRACE : Symbol()
        object RIGHT_BRACE : Symbol()
        object COMMA : Symbol()
    }

    // literals
    sealed class Literal : TokenType() {
        object INTEGER : Literal()
        object STRING : Literal()
    }

    // identifiers
    sealed class Identifier : TokenType() {
        object IDENTIFIER : Identifier()
    }

    sealed class Other : TokenType() {
        object EOF : Other()
    }
}