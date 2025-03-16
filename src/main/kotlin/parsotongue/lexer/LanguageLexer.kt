package parsotongue.lexer

import parsotongue.lexer.TokenType.*


class LanguageLexer(private val source: String) : Lexer {
    private val tokens = mutableListOf<Token>()
    private var current = 0
    private var start = 0

    // TODO: note that line and column start with 1, not 0!
    private var line = 1
    private var column = 1

    // Keywords map
    private val keywords = mapOf(
        "var" to Keyword.VAR,
        "function" to Keyword.FUNCTION,
        "if" to Keyword.IF,
        "else" to Keyword.ELSE,
        "return" to Keyword.RETURN
    )

    override fun tokenize(): List<Token> {
        while (!isAtEnd()) {
            start = current
            scanToken()
        }

        // EOF token
        tokens.add(Token(
            type = Other.EOF,
            lexeme = "",
            literal = null,
            line = line,
            column = column,
            // TODO: is it `start`?
            startOffset = start,
        ))

        return tokens
    }


    private fun scanToken() {
        val c = advance()
        // consumed token is expected to be in range [start, current) by the end of parsing
        when (c) {
            // single-character tokens
            '(' -> addToken(Symbol.LEFT_PAREN)
            ')' -> addToken(Symbol.RIGHT_PAREN)
            '{' -> addToken(Symbol.LEFT_BRACE)
            '}' -> addToken(Symbol.RIGHT_BRACE)
            ';' -> addToken(Symbol.SEMICOLON)
            ',' -> addToken(Symbol.COMMA)

            // operators
            '+' -> addToken(Operator.PLUS)
            '-' -> addToken(Operator.MINUS)
            '*' -> addToken(Operator.MULTIPLY)
            '/' -> addToken(Operator.DIVIDE)
            '%' -> addToken(Operator.MODULO)


            // operators that can be one or two chars
            '=' -> addToken(if (match('=')) Operator.EQUAL_EQUAL else Symbol.ASSIGN)
            '>' -> addToken(if (match('=')) Operator.GREATER_EQUAL else Operator.GREATER_THAN)
            '<' -> addToken(if (match('=')) Operator.LESS_EQUAL else Operator.LESS_THAN)
            '!' -> {
                if (match('=')) {
                    addToken(Operator.NOT_EQUAL)
                } else {
                    error("Unexpected character: '!'")
                }
            }

            // ignore whitespace
            ' ', '\r', '\t' -> {
                column++
            }
            // newline
            '\n' -> {
                line++
                column = 1
            }

            // literals, identifiers, and keywords
            // String literals
            '"' -> string()
            // we either have an integer of some identifier (variable or keyword)
            else -> {
                if (isDigit(c)) {
                    number()
                } else if (isAlpha(c)) {
                    identifier()
                } else {
                    error("Unexpected character: '$c'")
                }
            }
        }
    }

    private fun string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
                line++
                column = 1
            }
            advance()
        }

        if (isAtEnd()) {
            error("Unterminated string")
            return
        }

        // consume the closing "
        advance()

        // Get the string value (without quotes)
        val value: String = source.substring(start + 1, current - 1)
        addToken(type = Literal.STRING, literal = value)
    }

    private fun number() {
        // collect while a digit encountered
        while (isDigit(peek())) {
            advance()
        }

        val value = source.substring(start, current).toInt()
        addToken(type = Literal.INTEGER, literal = value)
    }

    private fun identifier() {
        while (isAlphaNumeric(peek())) {
            advance()
        }

        val text = source.substring(start, current)
        // it is either a keyword (if matches with existing ones) or an identifier
        val type = keywords[text] ?: Identifier.IDENTIFIER
        addToken(type)
    }

    private fun advance(): Char {
        column++
        return source[current++]
    }

    /**
     * Checks whether the current character in the source matches the expected character.
     * If the match is successful, it _**advances the current position**_ and updates the column.
     *
     * @param expected The character to match against the current character in the source.
     * @return `true` if the current character matches the expected character, otherwise `false`.
     */
    private fun match(expected: Char): Boolean {
        if (isAtEnd() || source[current] != expected) {
            return false
        }

        current++
        column++
        return true
    }

    private fun peek(): Char {
        // TODO: maybe throw if it is the end? otherwise, not clear if mistake made
        return if (isAtEnd()) '\u0000' else source[current]
    }

    private fun isAtEnd(): Boolean {
        return current >= source.length
    }

    /**
     * Checks whether the given char is a digit in the decimal number system (10-base).
     */
    private fun isDigit(c: Char): Boolean {
        return c in '0'..'9'
    }

    /**
     * Checks whether the given char is a supported character.
     *
     * Only 1) latin alphabet characters and 2) underscore are allowed.
     */
    private fun isAlpha(c: Char): Boolean {
        return c in 'a'..'z' || c in 'A'..'Z' || c == '_'
    }

    /**
     * Determines if the specified character is either a digit or a character.
     *
     * @param c The character to check.
     * @return `true` if the character is alphanumeric, otherwise `false`.
     *
     * @see [isAlpha]
     * @see [isDigit]
     */
    private fun isAlphaNumeric(c: Char): Boolean {
        return isDigit(c) || isAlpha(c)
    }

    private fun addToken(type: TokenType, literal: Any? = null) {
        val text = source.substring(start, current)
        tokens.add(Token(
            type = type,
            lexeme = text,
            literal = literal,
            line = line,
            column = column,
            startOffset = start,
        ))
    }

    // TODO: for now, we don't handle errors in the given source
    private fun error(message: String) {
        throw RuntimeException("Error at line $line, column $column: $message")
    }
}