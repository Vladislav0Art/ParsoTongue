package parsotongue.parser

import parsotongue.exceptions.MissingBinaryOperatorException
import parsotongue.exceptions.ParseException
import parsotongue.lexer.Token
import parsotongue.lexer.TokenType.Keyword
import parsotongue.lexer.TokenType.Operator
import parsotongue.lexer.TokenType.Symbol
import parsotongue.lexer.TokenType.Literal
import parsotongue.lexer.TokenType.Identifier
import parsotongue.lexer.TokenType
import parsotongue.lexer.TokenType.Other


class LanguageParser(private val tokens: List<Token>) : Parser {
    private var current = 0

    override fun parse(): Program {
        val statements = mutableListOf<Statement>()
        while (!isAtEnd()) {
            statements.add(statement())
        }

        return Program(statements)
    }

    private fun statement(): Statement {
        // e.g., `var a = 10`
        if (match(Keyword.VAR)) {
            return variableDeclaration()
        }
        // e.g., `function f(...) {}`
        if (match(Keyword.FUNCTION)) {
            return functionDeclaration()
        }
        // e.g., `if (...) {}`
        if (match(Keyword.IF)) {
            return ifStatement()
        }
        // e.g., `{ ... }`
        if (match(Symbol.LEFT_BRACE)) {
            return Block(block())
        }
        // e.g., `return ...;`
        if (match(Keyword.RETURN)) {
            return returnStatement()
        }
        // anything else
        return expressionStatement()
    }

    private fun variableDeclaration(): VariableDeclaration {
        val name = consume(Identifier.IDENTIFIER, "Expect variable name").lexeme

        consume(Symbol.ASSIGN, "Expect '=' after variable name")
        val initializer = expression()

        consume(Symbol.SEMICOLON, "Expect ';' after variable declaration")

        return VariableDeclaration(name, initializer)
    }

    private fun functionDeclaration(): FunctionDeclaration {
        val name = consume(Identifier.IDENTIFIER, "Expect function name").lexeme

        consume(Symbol.LEFT_PAREN, "Expect '(' after function name")

        val parameters = mutableListOf<String>()

        if (!matchesCurrentToken(Symbol.RIGHT_PAREN)) {
            // consume parameters (at least one expected here)
            do {
                parameters.add(consume(Identifier.IDENTIFIER, "Expect parameter name").lexeme)
            } while (match(Symbol.COMMA))
        }

        consume(Symbol.RIGHT_PAREN, "Expect ')' after parameters")

        consume(Symbol.LEFT_BRACE, "Expect '{' before function body")
        val body = Block(block())

        return FunctionDeclaration(name, parameters, body)
    }

    private fun returnStatement(): ReturnStatement {
        val value = if (!matchesCurrentToken(Symbol.SEMICOLON)) expression() else null
        consume(Symbol.SEMICOLON, "Expect ';' after return value")
        return ReturnStatement(value)
    }

    private fun ifStatement(): IfStatement {
        consume(Symbol.LEFT_PAREN, "Expect '(' after 'if'")
        val condition = expression()
        consume(Symbol.RIGHT_PAREN, "Expect ')' after if condition")

        consume(Symbol.LEFT_BRACE, "Expect '{' before if body")
        val thenBlock = Block(block())

        var elseBlock: Block? = null
        if (match(Keyword.ELSE)) {
            // TODO: support two options:
            //      1: else { ... } (+)
            //      2: else if { ... }
            consume(Symbol.LEFT_BRACE, "Expect '{' before else body")
            elseBlock = Block(block())
        }

        return IfStatement(condition, thenBlock, elseBlock)
    }

    private fun block(): List<Statement> {
        val statements = mutableListOf<Statement>()
        while (!matchesCurrentToken(Symbol.RIGHT_BRACE) && !isAtEnd()) {
            statements.add(statement())
        }

        consume(Symbol.RIGHT_BRACE, "Expect '}' after block")

        return statements
    }

    private fun expressionStatement(): ExpressionStatement {
        val expr = expression()
        consume(Symbol.SEMICOLON, "Expect ';' after expression")
        return ExpressionStatement(expr)
    }

    private fun expression(): Expression {
        return equality()
    }

    /**
     * Parse equality expressions: ==, !=
     *
     * It first tries to match comparison.
     * Other expression parsing functions also try to match other expressions with higher priority
     */
    private fun equality(): Expression {
        var expr = comparison()

        while (match(Operator.EQUAL_EQUAL, Operator.NOT_EQUAL)) {
            val operator = expectBinaryOperator(previous().type)
            val right = comparison()
            expr = BinaryExpression(expr, operator, right)
        }

        return expr
    }

    /**
     * parse comparison expressions: <, >, <=, >=
     */
    private fun comparison(): Expression {
        var expr = addition()

        while (match(
                Operator.LESS_THAN, Operator.GREATER_THAN,
                Operator.LESS_EQUAL, Operator.GREATER_EQUAL
            )) {
            val operator = expectBinaryOperator(previous().type)
            val right = addition()
            expr = BinaryExpression(expr, operator, right)
        }

        return expr
    }

    // Parse addition/subtraction: +, -
    private fun addition(): Expression {
        var expr = multiplication()

        while (match(Operator.PLUS, Operator.MINUS)) {
            val operator = expectBinaryOperator(previous().type)
            val right = multiplication()
            expr = BinaryExpression(expr, operator, right)
        }

        return expr
    }

    /**
     * parse multiplication/division: *, /, %
     */
    private fun multiplication(): Expression {
        var expr = primary()

        while (match(Operator.MULTIPLY, Operator.DIVIDE, Operator.MODULO)) {
            val operator = expectBinaryOperator(previous().type)
            val right = primary()
            expr = BinaryExpression(expr, operator, right)
        }

        return expr
    }

    /**
     * Parse primary expressions: literals, identifiers, function calls, or expression in parenthesis.
     */
    private fun primary(): Expression {
        if (match(Literal.INTEGER)) {
            return IntegerLiteral(previous().literal as Int)
        }
        if (match(Literal.STRING)) {
            return StringLiteral(previous().literal as String)
        }
        if (match(Identifier.IDENTIFIER)) {
            val name = previous().lexeme

            // if parenthesis isn't closed with left paren, then this is a function call
            if (match(Symbol.LEFT_PAREN)) {
                val arguments = mutableListOf<Expression>()

                if (!matchesCurrentToken(Symbol.RIGHT_PAREN)) {
                    do {
                        arguments.add(expression())
                    } while (match(Symbol.COMMA))
                }

                consume(Symbol.RIGHT_PAREN, "Expect ')' after function arguments")

                return FunctionCall(name, arguments)
            }

            return VariableReference(name)
        }
        if (match(Symbol.LEFT_PAREN)) {
            val expr = expression()
            consume(Symbol.RIGHT_PAREN, "Expect ')' after expression")
            return expr
        }

        throw error(peek(), "Expect expression")
    }

    /**
     * Validates that the given token type is a valid binary operator and returns it.
     *
     * If the token type is not a binary operator, throws [MissingBinaryOperatorException].
     *
     * @param type The token type to validate.
     * @return The validated binary operator.
     * @throws MissingBinaryOperatorException if the given token type is not a valid binary operator.
     */
    private fun expectBinaryOperator(type: TokenType): Operator {
        val nonBinaryOperators = listOf(
            Operator.EQUAL_EQUAL, Operator.NOT_EQUAL,
            Operator.GREATER_THAN, Operator.GREATER_EQUAL,
            Operator.LESS_THAN, Operator.LESS_EQUAL,
        )
        if (type is Operator && type !in nonBinaryOperators) {
            return type
        }
        throw MissingBinaryOperatorException(type)
    }

    /**
     * Attempts to match the current token against one or more specified token types.
     *
     * If the current token matches any of the given types, it _**advances the token cursor**_
     * and returns `true`. Otherwise, it leaves the cursor unchanged and returns `false`.
     *
     * @param types The token types to match against the current token.
     * @return `true` if the current token matches any of the provided types; `false` otherwise.
     */
    private fun match(vararg types: TokenType): Boolean {
        for (type in types) {
            if (matchesCurrentToken(type)) {
                advance()
                return true
            }
        }
        return false
    }

    /**
     * Returns `true` if the provided token matches the current one (i.e., the one that [current] points to),
     * otherwise `false`.
     */
    private fun matchesCurrentToken(type: TokenType): Boolean {
        return if (isAtEnd()) false else peek().type == type
    }

    /**
     * Advances the current position (i.e., [current]) by one if possible.
     *
     * No-op when the current token is [TokenType.Other.EOF].
     *
     * @return the token that was pointed at before the advancement.
     * @see [previous]
     */
    private fun advance(): Token {
        if (!isAtEnd()) current++
        return previous()
    }

    /**
     * Ensures that the current token matches the expected type.
     *
     * If the token matches, it advances the token cursor and returns the consumed token.
     * Otherwise, it throws a parsing error with a provided error message.
     *
     * @param type The expected type of the token to be consumed.
     * @param message The error message to display if the current token does not match the expected type.
     * @return The consumed token if the current token matches the expected type.
     * @throws ParseException if the current token does not match the expected type.
     */
    private fun consume(type: TokenType, message: String): Token {
        if (matchesCurrentToken(type)) {
            return advance()
        }
        throw error(peek(), message)
    }

    private fun error(currentToken: Token, message: String): ParseException {
        val errorPos = if (currentToken.type == Other.EOF) "end" else "line ${currentToken.line} at position ${currentToken.column}"
        return ParseException(message = "Error at $errorPos: $message", currentToken)
    }

    private fun isAtEnd(): Boolean {
        return peek().type == Other.EOF
    }

    /**
     * Returns the current token pointed by [current].
     */
    private fun peek(): Token {
        return tokens[current]
    }

    /**
     * Returns previous token pointed by [current] - 1.
     * Expects the index to exist in the token list.
     */
    private fun previous(): Token {
        return tokens[current - 1]
    }
}

