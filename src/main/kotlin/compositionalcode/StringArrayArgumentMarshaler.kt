package compositionalcode

import compositionalcode.ArgsException.ErrorCode.*


class StringArrayArgumentMarshaler : ArgumentMarshaler {
    private val strings: MutableList<String> = ArrayList()

    override fun extract(currentArgument: Iterator<String>?): Array<String> {
        try {
            strings.add(currentArgument!!.next())
            return strings.toTypedArray()
        } catch (e: NoSuchElementException) {
            throw ArgsException(MISSING_STRING)
        }
    }

    companion object {
        fun getValue(am: ArgumentMarshaler?): Array<String> {
            return if (am != null && am is StringArrayArgumentMarshaler) am.strings.toTypedArray() else arrayOf()
        }

        fun cast(value: Any?): Array<String>? =
            value as? Array<String>
    }
}