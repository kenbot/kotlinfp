package cleancode

import cleancode.ArgsException.ErrorCode.MISSING_STRING


class StringArgumentMarshaler : ArgumentMarshaler {
    private var stringValue: String? = ""

    @Throws(ArgsException::class)
    override fun set(currentArgument: Iterator<String?>?) {
        stringValue = try {
            currentArgument!!.next()
        } catch (e: NoSuchElementException) {
            throw ArgsException(MISSING_STRING)
        }
    }

    companion object {
        fun getValue(am: ArgumentMarshaler?): String {
            return if (am != null && am is StringArgumentMarshaler) (am.stringValue ?: "") else ""
        }
    }
}