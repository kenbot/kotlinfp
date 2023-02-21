package compositionalcode

import compositionalcode.ArgsException.ErrorCode.MISSING_STRING


class StringArgumentMarshaler : ArgumentMarshaler {
    private var stringValue: String = ""

    override fun extract(currentArgument: Iterator<String>?): String {
        stringValue = try {
            currentArgument!!.next()
        } catch (e: NoSuchElementException) {
            throw ArgsException(MISSING_STRING)
        }
        return stringValue
    }

    companion object {
        fun getValue(am: ArgumentMarshaler?): String {
            return if (am != null && am is StringArgumentMarshaler) am.stringValue else ""
        }

        fun cast(value: Any?): String? =
            value as? String
    }
}