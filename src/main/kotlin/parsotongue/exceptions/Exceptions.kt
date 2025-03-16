package parsotongue.exceptions

import parsotongue.lexer.Token
import parsotongue.lexer.TokenType

class ParseException(message: String, token: Token) : RuntimeException("$message\nCurrent token: $token")

class MissingBinaryOperatorException(type: TokenType) : RuntimeException("Expected binary operator, got $type instead")