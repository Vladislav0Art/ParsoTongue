package parsotongue.parser

import parsotongue.lexer.TokenType

sealed class ASTNode

// implementations
data class Program(val statements: List<Statement>) : ASTNode()

sealed class Statement : ASTNode()

data class VariableDeclaration(
    val name: String,
    val initialValue: Expression
) : Statement()

data class FunctionDeclaration(
    val name: String,
    val parameters: List<String>,
    val body: Block
) : Statement()

data class ReturnStatement(val value: Expression?) : Statement()

data class Block(val statements: List<Statement>) : Statement()

data class IfStatement(
    val condition: Expression,
    val thenBlock: Block,
    val elseBlock: Block?
) : Statement()

data class ExpressionStatement(val expression: Expression) : Statement()

sealed class Expression : ASTNode()

data class BinaryExpression(
    val left: Expression,
    val operator: TokenType.Operator,
    val right: Expression
) : Expression()

data class IntegerLiteral(val value: Int) : Expression()
data class StringLiteral(val value: String) : Expression()
data class VariableReference(val name: String) : Expression()
data class FunctionCall(
    val name: String,
    val arguments: List<Expression>
) : Expression()