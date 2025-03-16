package parsotongue

import ij.demo.parsotongue.lexer.LanguageLexer
import ij.demo.parsotongue.parser.LanguageParser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ParsoTongueParserTest {
    private lateinit var parser: ParsoTongueParser

    @BeforeEach
    fun setUp() {
        parser = ParsoTongueParser(
            lexer = LanguageLexer(),
            parser = LanguageParser()
        )
    }

    @Test
    fun parse() {

    }

}