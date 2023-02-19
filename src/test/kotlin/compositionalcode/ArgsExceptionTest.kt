package compositionalcode

import compositionalcode.ArgsException.ErrorCode.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ArgsExceptionTest {
    @Test
    fun testUnexpectedMessage() {
        val e = ArgsException(UNEXPECTED_ARGUMENT, 'x', null)
        assertEquals("Argument -x unexpected.", e.errorMessage())
    }

    @Test
    fun testMissingStringMessage() {
        val e = ArgsException(MISSING_STRING, 'x', null)
        assertEquals("Could not find string parameter for -x.", e.errorMessage())
    }

    @Test
    fun testInvalidIntegerMessage() {
        val e = ArgsException(INVALID_INTEGER, 'x', "Forty two")
        assertEquals("Argument -x expects an integer but was 'Forty two'.", e.errorMessage())
    }

    @Test
    fun testMissingIntegerMessage() {
        val e = ArgsException(MISSING_INTEGER, 'x', null)
        assertEquals("Could not find integer parameter for -x.", e.errorMessage())
    }

    @Test
    fun testInvalidDoubleMessage() {
        val e = ArgsException(INVALID_DOUBLE, 'x', "Forty two")
        assertEquals("Argument -x expects a double but was 'Forty two'.", e.errorMessage())
    }

    @Test
    fun testMissingDoubleMessage() {
        val e = ArgsException(MISSING_DOUBLE, 'x', null)
        assertEquals("Could not find double parameter for -x.", e.errorMessage())
    }

    @Test
    fun testMissingMapMessage() {
        val e = ArgsException(MISSING_MAP, 'x', null)
        assertEquals("Could not find map string for -x.", e.errorMessage())
    }

    @Test
    fun testMalformedMapMessage() {
        val e = ArgsException(MALFORMED_MAP, 'x', null)
        assertEquals("Map string for -x is not of form k1:v1,k2:v2...", e.errorMessage())
    }

    @Test
    fun testInvalidArgumentName() {
        val e = ArgsException(INVALID_ARGUMENT_NAME, '#', null)
        assertEquals("'#' is not a valid argument name.", e.errorMessage())
    }

    @Test
    fun testInvalidFormat() {
        val e = ArgsException(INVALID_ARGUMENT_FORMAT, 'x', "$")
        assertEquals("'$' is not a valid argument format.", e.errorMessage())
    }
}