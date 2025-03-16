package parsotongue.lexer




sealed interface TokenType {
    // keywords
    sealed interface Keyword : TokenType {
        object VAR : Keyword
        object FUNCTION : Keyword
        object IF : Keyword
        object ELSE : Keyword
        object RETURN : Keyword
    }

    sealed interface Operator : TokenType {
        object PLUS : Operator
        object MINUS : Operator
        object MULTIPLY : Operator
        object DIVIDE : Operator
        object MODULO : Operator
        object LESS_THAN : Operator
        object GREATER_THAN : Operator
        object LESS_EQUAL : Operator
        object GREATER_EQUAL : Operator
        object EQUAL_EQUAL : Operator
        object NOT_EQUAL : Operator
    }

    // symbols
    sealed interface Symbol : TokenType {
        object ASSIGN : Symbol
        object SEMICOLON : Symbol
        object LEFT_PAREN : Symbol
        object RIGHT_PAREN : Symbol
        object LEFT_BRACE : Symbol
        object RIGHT_BRACE : Symbol
        object COMMA : Symbol
    }

    // literals
    sealed interface Literal : TokenType {
        object INTEGER : Literal
        object STRING : Literal
    }

    // identifiers
    sealed interface Identifier : TokenType {
        object IDENTIFIER : Identifier
    }

    sealed interface Other : TokenType {
        object EOF : Other
    }
}