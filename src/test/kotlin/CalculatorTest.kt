import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Test
import tokenizer.tokenize
import visitor.CalcVisitor
import visitor.ParserVisitor
import java.lang.Exception
import java.lang.IllegalArgumentException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CalculatorTest {
    private fun calc(source: String): Int {
        return CalcVisitor().visitAll(ParserVisitor().visitAll(tokenize(source)))
    }

    private fun testValid(source: String, expectedResult: Int) =
        assertEquals(expectedResult, calc(source))

    private inline fun <reified ExceptionT : Exception> testException(source: String) =
        assertThrows<ExceptionT> { calc(source) }

    @Test
    fun testSimple() {
        testValid("1", 1)
        testValid("  1  ", 1)
        testValid("1337", 1337)
        testValid(" 1 + 2 ", 3)
        testValid(" 2 - 1 ", 1)
        testValid(" 2 * 2 ", 4)
        testValid(" 6 / 3 ", 2)
        testValid(" (   1 + 1) ", 2)
    }

    @Test
    fun testBig() {
        testValid("(1 + 2) / 3 * 10", 10)
        testValid("(1 + 2) / (1 + 0) * 6 - 10 / (3 + 2) * 4", 10)
        testValid("(((15 + 4) * 10) - 58) + 7 * 1 * 10 - 14 * ((28 + 5) - 1)", -246)
    }

    @Test
    fun testException() {
        testException<IllegalArgumentException>("(2")
        testException<IllegalArgumentException>(" + 1")
        testException<IllegalArgumentException>("((1)")
        testException<IllegalStateException>("1 bruh 2")
    }
}