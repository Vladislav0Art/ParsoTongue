package parsotongue.parser

sealed class ASTNode {
    data class Program(val statements: List<Statement>) : ASTNode()
    sealed class Statement : ASTNode()
}