package parsotongue.parser

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import parsotongue.providers.LanguageParserProvider

class LanguageParserTest {
    private lateinit var parserProvider: LanguageParserProvider

    @BeforeEach
    fun setUp() {
        parserProvider = LanguageParserProvider
    }

    @Test
    fun parse() {
    }

}