package parsotongue

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import parsotongue.lexer.LanguageLexer
import parsotongue.parser.*
import parsotongue.providers.LanguageLexerProvider
import parsotongue.providers.LanguageParserProvider
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.Timeout
import java.util.concurrent.TimeUnit

class StressTest {
    private val logger = KotlinLogging.logger {}

    /**
     * Measures execution time and consumed memory
     */
    private fun <T> measureExecutionTime(name: String, block: () -> T): T {
        val startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
        val startTime = System.nanoTime()
        val result = block()
        val endTime = System.nanoTime()
        val endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()

        // convert to milliseconds
        val timeMs = (endTime - startTime) / 1_000_000.0
        // convert to MB
        val memoryMB = (endMemory - startMemory) / (1024.0 * 1024.0)

        logger.debug { "[$name] performance metrics:" }
        logger.debug { "\t- Execution time: %.2f ms".format(timeMs) }
        logger.debug { "\t- Memory used: %.2f MB".format(memoryMB) }

        return result
    }

    @Test
    @Timeout(value = 2, unit = TimeUnit.MINUTES)
    fun `stress test lexer with large input`() = runTest {
        val sizes = listOf(1_000, 10_000, 100_000, 500_000)

        sizes.forEach { size ->
            val input = generateLargeInput(size)
            logger.debug { "Testing lexer with input size: $size" }
            logger.debug { "Input length: ${input.length} characters" }

            val tokens = measureExecutionTime("Lexing $size items") {
                val lexer = LanguageLexer(input)
                lexer.tokenize()
            }

            logger.debug { "Generated ${tokens.size} tokens" }
        }
    }

    @Test
    fun `stress test parser with large input`() = runTest {
        val sizes = listOf(1_000, 5_000, 100_000, 200_000)

        sizes.forEach { size ->
            val input = generateLargeInput(size)
            logger.debug { "Testing parser with input size: $size" }
            logger.debug { "Input length: ${input.length} characters" }

            val tokens = measureExecutionTime("Lexing phase for parser test") {
                val lexer = LanguageLexer(input)
                lexer.tokenize()
            }
            logger.debug { "Generated ${tokens.size} tokens" }

            val parser = ParsoTongueParser(
                lexerProvider = LanguageLexerProvider,
                parserProvider = LanguageParserProvider
            )

            val ast = measureExecutionTime("Parsing $size items") {
                parser.parse(input)
            }

            logger.debug { "Generated AST with ${countAstNodes(ast)} nodes" }
        }
    }

    private fun countAstNodes(program: Program): Int {
        // start with `Program` node
        var count = 1

        fun visit(node: Any?) {
            if (node == null) return
            ++count
            when (node) {
                is List<*> -> node.forEach { visit(it) }
                is BinaryExpression -> {
                    visit(node.left)
                    visit(node.right)
                }
                is Block -> node.statements.forEach { visit(it) }
                is ExpressionStatement -> visit(node.expression)
                is FunctionDeclaration -> {
                    visit(node.body)
                    node.parameters.forEach { visit(it) }
                }
                is IfStatement -> {
                    visit(node.condition)
                    visit(node.thenBlock)
                    visit(node.elseBlock)
                }
                is VariableDeclaration -> visit(node.initialValue)
                is ReturnStatement -> visit(node.value)
                is FunctionCall -> {
                    node.arguments.forEach { visit(it) }
                }
                is IntegerLiteral, is StringLiteral, is VariableReference -> {
                    // leaf nodes, no need to visit further
                }
            }
        }
        visit(program)

        return count
    }

    private fun generateLargeInput(size: Int): String {
        val sb = StringBuilder()
        val spaces = " ".repeat(3)

        // helper function for deep nesting
        fun generateNestedIf(depth: Int, indent: String, varIndex: Int) {
            if (depth <= 0) {
                return
            }
            sb.append("${indent}if (x$varIndex < ${varIndex + depth}) {\n")
            sb.append("${indent}${spaces}var temp$depth = x$varIndex + $depth * ${varIndex + 1};\n")
            generateNestedIf(depth - 1, "$indent$spaces", varIndex + 1)
            sb.append("${indent}}\n")
        }

        // generate a complex program with variables, functions, and nested structures
        sb.append("function main() {\n")

        // Add many variable declarations
        repeat(size) { i ->
            val expr = when (i % 4) {
                0 -> "$i"
                1 -> "$i * ${i + 1}"
                2 -> "$i + ${i + 1} * ${i + 2}"
                else -> "${i + 1} * (${i + 2} + ${i + 3})"
            }
            sb.append("${spaces}var x$i = $expr;\n")
        }

        // add nested if statements with some conditions
        val nestedDepth = minOf(10, maxOf(3, size / 100))
        sb.append("\n")
        generateNestedIf(nestedDepth, spaces, 0)
        sb.append("\n")

        // add expressions and calculations
        repeat(size / 50) { i ->
            sb.append("${spaces}var temp${i}_1 = x$i * ${i + 1} + x${i + 1};\n")
            sb.append("${spaces}var temp${i}_2 = temp${i}_1 * (x${i + 2} + ${i + 3});\n")
            sb.append("${spaces}if (temp${i}_2 > x$i) {\n")
            sb.append("${spaces}${spaces}var temp${i}_3 = temp${i}_2 * x${i + 3};\n")
            sb.append("${spaces}}\n")
        }

        // add debug output for tokens if size is small
        if (size <= 10) {
            logger.debug { "Generated code tokens:" }
            val lexer = LanguageLexer(sb.toString())

            lexer.tokenize().forEach { token ->
                logger.debug { "${token.type} '${token.lexeme}' at line ${token.line}, column ${token.column}" }
            }
        }

        if (size <= 10) {
            logger.debug { "Nesting depth: $nestedDepth" }
        }

        sb.append("\n${spaces}return 0;\n")
        sb.append("}\n")
        return sb.toString()
    }
}
