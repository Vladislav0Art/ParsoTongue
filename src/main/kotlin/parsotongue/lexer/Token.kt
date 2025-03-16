package parsotongue.lexer

data class Token(
    val type: TokenType,
    val lexeme: String,
    /**
     * Stored value of the token.
     *
     * Example:
     * 1. String literals:
     *  - Given a literal `"my str"`.
     *  - Token of this type with contain `lexeme` as `"my str"` and `literal` as `my str`.
     * 2. Integer literals:
     *  - Give a literal `123`.
     *  - Token of this type with contain `lexeme` as `123` and `literal` as `123`.
     */
    val literal: Any? = null,
    val line: Int,
    val column: Int,
    /**
     * Start offset is a global **index** in the source string, 0-based counting.
     */
    val startOffset: Int,
) {
    /**
     * Length of the token in the source in characters.
     */
    val length: Int
        get() = lexeme.length

    /**
     * The global 0-based end position of the token in the source string.
     *
     * Calculated as the sum of `startOffset` and `length`.
     *
     * The token is inside a range: [[startOffset], [endOffset]).
     */
    val endOffset: Int
        get() = startOffset + length
}
