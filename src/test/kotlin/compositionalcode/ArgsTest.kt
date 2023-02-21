package compositionalcode

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import compositionalcode.ArgsException.ErrorCode.*


class ArgsTest {

    @Test
    fun testCreateWithNoSchemaOrArguments() {
        val args = Args("", emptyArray())
        assertEquals(0, args.nextArgument())
    }


    @Test
    fun testWithNoSchemaButWithOneArgument() {
        try {
            Args("", arrayOf("-x"))
            fail()
        } catch (e: ArgsException) {
            assertEquals(UNEXPECTED_ARGUMENT, e.errorCode)
            assertEquals('x', e.errorArgumentId)
        }
    }

    @Test
    fun testWithNoSchemaButWithMultipleArguments() {
        try {
            Args("", arrayOf("-x", "-y"))
            fail()
        } catch (e: ArgsException) {
            assertEquals(UNEXPECTED_ARGUMENT, e.errorCode)
            assertEquals('x', e.errorArgumentId)
        }
    }

    @Test
    fun testNonLetterSchema() {
        try {
            Args("*", arrayOf())
            fail("Args constructor should have thrown exception")
        } catch (e: ArgsException) {
            assertEquals(INVALID_ARGUMENT_NAME, e.errorCode)
            assertEquals('*', e.errorArgumentId)
        }
    }

    @Test
    fun testInvalidArgumentFormat() {
        try {
            Args("f~", arrayOf())
            fail("Args constructor should have throws exception")
        } catch (e: ArgsException) {
            assertEquals(INVALID_ARGUMENT_FORMAT, e.errorCode)
            assertEquals('f', e.errorArgumentId)
        }
    }

    @Test
    fun testSimpleBooleanPresent() {
        val args = Args("x", arrayOf("-x"))
        assertEquals(true, args.getBoolean('x'))
        assertEquals(1, args.nextArgument())
    }

    @Test
    fun testSimpleStringPresent() {
        val args = Args("x*", arrayOf("-x", "param"))
        assertTrue(args.has('x'))
        assertEquals("param", args.getString('x'))
        assertEquals(2, args.nextArgument())
    }

    @Test
    fun testMissingStringArgument() {
        try {
            Args("x*", arrayOf("-x"))
            fail()
        } catch (e: ArgsException) {
            assertEquals(MISSING_STRING, e.errorCode)
            assertEquals('x', e.errorArgumentId)
        }
    }

    @Test
    fun testSpacesInFormat() {
        val args = Args("x, y", arrayOf("-xy"))
        assertTrue(args.has('x'))
        assertTrue(args.has('y'))
        assertEquals(1, args.nextArgument())
    }

    @Test
    fun testSimpleIntPresent() {
        val args = Args("x#", arrayOf("-x", "42"))
        assertTrue(args.has('x'))
        assertEquals(42, args.getInt('x'))
        assertEquals(2, args.nextArgument())
    }

    @Test
    fun testInvalidInteger() {
        try {
            Args("x#", arrayOf("-x", "Forty two"))
            fail()
        } catch (e: ArgsException) {
            assertEquals(INVALID_INTEGER, e.errorCode)
            assertEquals('x', e.errorArgumentId)
            assertEquals("Forty two", e.errorParameter)
        }
    }

    @Test
    fun testMissingInteger() {
        try {
            Args("x#", arrayOf("-x"))
            fail()
        } catch (e: ArgsException) {
            assertEquals(MISSING_INTEGER, e.errorCode)
            assertEquals('x', e.errorArgumentId)
        }
    }

    @Test
    fun testSimpleDoublePresent() {
        val args = Args("x##", arrayOf("-x", "42.3"))
        assertTrue(args.has('x'))
        assertEquals(42.3, args.getDouble('x')!!, .001)
    }

    @Test
    fun testInvalidDouble() {
        try {
            Args("x##", arrayOf("-x", "Forty two"))
            fail()
        } catch (e: ArgsException) {
            assertEquals(INVALID_DOUBLE, e.errorCode)
            assertEquals('x', e.errorArgumentId)
            assertEquals("Forty two", e.errorParameter)
        }
    }

    @Test
    fun testMissingDouble() {
        try {
            Args("x##", arrayOf("-x"))
            fail()
        } catch (e: ArgsException) {
            assertEquals(MISSING_DOUBLE, e.errorCode)
            assertEquals('x', e.errorArgumentId)
        }
    }

    @Test
    fun testStringArray() {
        val args = Args("x[*]", arrayOf("-x", "alpha"))
        assertTrue(args.has('x'))
        val result: Array<String>? = args.getStringArray('x')
        assertEquals(1, result?.size)
        assertEquals("alpha", result?.get(0))
    }

    @Test
    fun testMissingStringArrayElement() {
        try {
            Args("x[*]", arrayOf("-x"))
            fail()
        } catch (e: ArgsException) {
            assertEquals(MISSING_STRING, e.errorCode)
            assertEquals('x', e.errorArgumentId)
        }
    }

    @Test
    fun manyStringArrayElements() {
        val args = Args("x[*]", arrayOf("-x", "alpha", "-x", "beta", "-x", "gamma"))
        assertTrue(args.has('x'))
        val result: Array<String> = args.getStringArray('x')!!
        assertEquals(3, result.size)
        assertEquals("alpha", result[0])
        assertEquals("beta", result[1])
        assertEquals("gamma", result[2])
    }

    @Test
    fun testExtraArguments() {
        val args = Args("x,y*", arrayOf("-x", "-y", "alpha", "beta"))
        assertTrue(args.getBoolean('x'))
        assertEquals("alpha", args.getString('y'))
        assertEquals(3, args.nextArgument())
    }

    @Test
    fun testExtraArgumentsThatLookLikeFlags() {
        val args = Args("x,y", arrayOf("-x", "alpha", "-y", "beta"))
        assertTrue(args.has('x'))
        assertFalse(args.has('y'))
        assertTrue(args.getBoolean('x'))
        assertFalse(args.getBoolean('y'))
        assertEquals(1, args.nextArgument())
    }
}