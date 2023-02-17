package cleancode

import cleancode.ArgsException.ErrorCode.*


class StringArrayArgumentMarshaler : ArgumentMarshaler {
    private val strings: MutableList<String?> = ArrayList()

    @Throws(ArgsException::class)
    override fun set(currentArgument: Iterator<String?>?) {
        try {
            strings.add(currentArgument!!.next())
        } catch (e: NoSuchElementException) {
            throw ArgsException(MISSING_STRING)
        }
    }

    companion object {
        fun getValue(am: ArgumentMarshaler?): Array<String?> {
            return if (am != null && am is StringArrayArgumentMarshaler) am.strings.toTypedArray() else arrayOfNulls(0)
        }
    }
}