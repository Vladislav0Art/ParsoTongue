package parsotongue.lexer

import ij.demo.parsotongue.lexer.LanguageLexer
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LanguageLexerTest {
    private lateinit var lexer: Lexer

    @BeforeEach
    fun setUp() {
        lexer = LanguageLexer()
    }

    @Test
    fun tokenize() = runTest {
        println("tokenize")
    }

}