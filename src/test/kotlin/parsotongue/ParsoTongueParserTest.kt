package parsotongue

import parsotongue.providers.LanguageLexerProvider
import parsotongue.providers.LanguageParserProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import parsotongue.lexer.TokenType
import parsotongue.parser.BinaryExpression
import parsotongue.parser.Block
import parsotongue.parser.ExpressionStatement
import parsotongue.parser.FunctionDeclaration
import parsotongue.parser.IfStatement
import parsotongue.parser.IntegerLiteral
import parsotongue.parser.Program
import parsotongue.parser.ReturnStatement
import parsotongue.parser.Statement
import parsotongue.parser.StringLiteral
import parsotongue.parser.VariableDeclaration
import parsotongue.parser.VariableReference

class ParsoTongueParserTest {
    private lateinit var parser: ParsoTongueParser

    @BeforeEach
    fun setUp() {
        parser = ParsoTongueParser(
            lexerProvider = LanguageLexerProvider,
            parserProvider = LanguageParserProvider,
        )
    }

    @Test
    fun `test arithmetic expression`() {
        val source = "1 + 2 + 12;"
        val program = parser.parse(source)

        /**
         *      +
         *    /  \
         *   +    12
         *  / \
         * 1  2
         */
        val expectedProgram = Program(listOf(
            ExpressionStatement(
                expression = BinaryExpression(
                    left = BinaryExpression(
                        left = IntegerLiteral(1),
                        operator = TokenType.Operator.PLUS,
                        right = IntegerLiteral(2),
                    ),
                    operator = TokenType.Operator.PLUS,
                    right = IntegerLiteral(12),
                )
            )
        ))

        assertEquals(expectedProgram, program)
    }

    @Test
    fun `test variable declaration and assignment`() {
        val source = "var x = 42;"
        val program = parser.parse(source)

        val expectedProgram = Program(listOf(
            VariableDeclaration(
                name = "x",
                initialValue = IntegerLiteral(42)
            )
        ))

        assertEquals(expectedProgram, program)
    }

    @Test
    fun `test string variable declaration`() {
        val source = """var message = "Hello, World!";"""
        val program = parser.parse(source)

        val expectedProgram = Program(listOf(
            VariableDeclaration(
                name = "message",
                initialValue = StringLiteral("Hello, World!")
            )
        ))

        assertEquals(expectedProgram, program)
    }

    @Test
    fun `test arithmetic operations`() {
        val source = """
            var a = 10 + 5;
            var b = 20 - 3;
            var c = 4 * 3;
            var d = 15 / 3;
            var e = 17 % 5;
        """.trimIndent()
        val program = parser.parse(source)

        val expectedProgram = Program(listOf(
            VariableDeclaration(
                name = "a",
                initialValue = BinaryExpression(
                    left = IntegerLiteral(10),
                    operator = TokenType.Operator.PLUS,
                    right = IntegerLiteral(5)
                )
            ),
            VariableDeclaration(
                name = "b",
                initialValue = BinaryExpression(
                    left = IntegerLiteral(20),
                    operator = TokenType.Operator.MINUS,
                    right = IntegerLiteral(3)
                )
            ),
            VariableDeclaration(
                name = "c",
                initialValue = BinaryExpression(
                    left = IntegerLiteral(4),
                    operator = TokenType.Operator.MULTIPLY,
                    right = IntegerLiteral(3)
                )
            ),
            VariableDeclaration(
                name = "d",
                initialValue = BinaryExpression(
                    left = IntegerLiteral(15),
                    operator = TokenType.Operator.DIVIDE,
                    right = IntegerLiteral(3)
                )
            ),
            VariableDeclaration(
                name = "e",
                initialValue = BinaryExpression(
                    left = IntegerLiteral(17),
                    operator = TokenType.Operator.MODULO,
                    right = IntegerLiteral(5)
                )
            )
        ))

        assertEquals(expectedProgram, program)
    }

    @Test
    fun `test comparison operations`() {
        val source = """
            var a = 10 == 10;
            var b = 20 != 30;
            var c = 5 < 10;
            var d = 15 > 3;
            var e = 7 <= 7;
            var f = 12 >= 8;
        """.trimIndent()
        val program = parser.parse(source)

        val expectedProgram = Program(listOf(
            VariableDeclaration(
                name = "a",
                initialValue = BinaryExpression(
                    left = IntegerLiteral(10),
                    operator = TokenType.Operator.EQUAL_EQUAL,
                    right = IntegerLiteral(10)
                )
            ),
            VariableDeclaration(
                name = "b",
                initialValue = BinaryExpression(
                    left = IntegerLiteral(20),
                    operator = TokenType.Operator.NOT_EQUAL,
                    right = IntegerLiteral(30)
                )
            ),
            VariableDeclaration(
                name = "c",
                initialValue = BinaryExpression(
                    left = IntegerLiteral(5),
                    operator = TokenType.Operator.LESS_THAN,
                    right = IntegerLiteral(10)
                )
            ),
            VariableDeclaration(
                name = "d",
                initialValue = BinaryExpression(
                    left = IntegerLiteral(15),
                    operator = TokenType.Operator.GREATER_THAN,
                    right = IntegerLiteral(3)
                )
            ),
            VariableDeclaration(
                name = "e",
                initialValue = BinaryExpression(
                    left = IntegerLiteral(7),
                    operator = TokenType.Operator.LESS_EQUAL,
                    right = IntegerLiteral(7)
                )
            ),
            VariableDeclaration(
                name = "f",
                initialValue = BinaryExpression(
                    left = IntegerLiteral(12),
                    operator = TokenType.Operator.GREATER_EQUAL,
                    right = IntegerLiteral(8)
                )
            )
        ))

        assertEquals(expectedProgram, program)
    }

    @Test
    fun `test function declarations`() {
        val source = """
            function empty() {
                var x = 1;
            }

            function add(a, b) {
                var result = a + b;
                return result;
            }

            function greet(name) {
                var message = "Hello, ";
                return message + name;
            }
        """.trimIndent()
        val program = parser.parse(source)

        val expectedProgram = Program(listOf(
            FunctionDeclaration(
                name = "empty",
                parameters = listOf(),
                body = Block(listOf(
                    VariableDeclaration(
                        name = "x",
                        initialValue = IntegerLiteral(1)
                    )
                ))
            ),
            FunctionDeclaration(
                name = "add",
                parameters = listOf("a", "b"),
                body = Block(listOf(
                    VariableDeclaration(
                        name = "result",
                        initialValue = BinaryExpression(
                            left = VariableReference("a"),
                            operator = TokenType.Operator.PLUS,
                            right = VariableReference("b")
                        )
                    ),
                    ReturnStatement(VariableReference("result"))
                ))
            ),
            FunctionDeclaration(
                name = "greet",
                parameters = listOf("name"),
                body = Block(listOf(
                    VariableDeclaration(
                        name = "message",
                        initialValue = StringLiteral("Hello, ")
                    ),
                    ReturnStatement(
                        BinaryExpression(
                            left = VariableReference("message"),
                            operator = TokenType.Operator.PLUS,
                            right = VariableReference("name")
                        )
                    )
                ))
            )
        ))

        assertEquals(expectedProgram, program)
    }

    @Test
    fun `test conditional statements`() {
        val source = """
            if (x > 10) {
                var result = "greater";
            }

            if (a == b) {
                var equal = 1;
            } else {
                var equal = 0;
            }

            if (value >= 0) {
                if (value == 0) {
                    var message = "zero";
                } else {
                    var message = "positive";
                }
            }
        """.trimIndent()
        val program = parser.parse(source)

        val expectedProgram = Program(listOf(
            IfStatement(
                condition = BinaryExpression(
                    left = VariableReference("x"),
                    operator = TokenType.Operator.GREATER_THAN,
                    right = IntegerLiteral(10)
                ),
                thenBlock = Block(listOf(
                    VariableDeclaration(
                        name = "result",
                        initialValue = StringLiteral("greater")
                    )
                )),
                elseBlock = null
            ),
            IfStatement(
                condition = BinaryExpression(
                    left = VariableReference("a"),
                    operator = TokenType.Operator.EQUAL_EQUAL,
                    right = VariableReference("b")
                ),
                thenBlock = Block(listOf(
                    VariableDeclaration(
                        name = "equal",
                        initialValue = IntegerLiteral(1)
                    )
                )),
                elseBlock = Block(listOf(
                    VariableDeclaration(
                        name = "equal",
                        initialValue = IntegerLiteral(0)
                    )
                ))
            ),
            IfStatement(
                condition = BinaryExpression(
                    left = VariableReference("value"),
                    operator = TokenType.Operator.GREATER_EQUAL,
                    right = IntegerLiteral(0)
                ),
                thenBlock = Block(listOf(
                    IfStatement(
                        condition = BinaryExpression(
                            left = VariableReference("value"),
                            operator = TokenType.Operator.EQUAL_EQUAL,
                            right = IntegerLiteral(0)
                        ),
                        thenBlock = Block(listOf(
                            VariableDeclaration(
                                name = "message",
                                initialValue = StringLiteral("zero")
                            )
                        )),
                        elseBlock = Block(listOf(
                            VariableDeclaration(
                                name = "message",
                                initialValue = StringLiteral("positive")
                            )
                        ))
                    )
                )),
                elseBlock = null
            )
        ))

        assertEquals(expectedProgram, program)
    }

    @Test
    fun `test parsing from file`() {
        val program = parser.read("src/test/resources/test_program.pt")
        // only some parts of AST is checked. file is big

        // verify function declarations exist
        val functionNames = program.statements
            .filterIsInstance<FunctionDeclaration>()
            .map { it.name }
            .toSet()
        assertEquals(setOf("max", "isEven", "calculateExpression"), functionNames)

        // collect variable declarations from global scope and top-level if statements
        fun collectGlobalVariableNames(statements: List<Statement>): Set<String> {
            val names = mutableSetOf<String>()
            for (statement in statements) {
                when (statement) {
                    is VariableDeclaration -> names.add(statement.name)
                    is IfStatement -> {
                        names.addAll(collectGlobalVariableNames(statement.thenBlock.statements))
                        statement.elseBlock?.let { names.addAll(collectGlobalVariableNames(it.statements)) }
                    }
                    // skipping anything else
                    else -> {}
                }
            }
            return names
        }

        // verify all expected variables exist
        val variableNames = collectGlobalVariableNames(program.statements)
        assertEquals(setOf("number1", "number2", "maxNumber", "isMaxEven", "result", "message"), variableNames)

        // verify the initial values of number1 and number2
        val number1 = program.statements
            .filterIsInstance<VariableDeclaration>()
            .find { it.name == "number1" }
            ?.initialValue as? IntegerLiteral
        assertEquals(15, number1?.value)

        val number2 = program.statements
            .filterIsInstance<VariableDeclaration>()
            .find { it.name == "number2" }
            ?.initialValue as? IntegerLiteral
        assertEquals(10, number2?.value)
    }
}
