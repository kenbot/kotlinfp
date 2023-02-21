package compositionalcode

import compositionalcode.ArgsException.ErrorCode.*


class IntegerArgumentMarshaler : ArgumentMarshaler {
    private var intValue = 0

    override fun extract(currentArgument: Iterator<String>?): Int {
        var parameter: String? = null
        try {
            parameter = currentArgument!!.next()
            intValue = parameter.toInt()
            return intValue
        } catch (e: NoSuchElementException) {
            throw ArgsException(MISSING_INTEGER)
        } catch (e: NumberFormatException) {
            throw ArgsException(INVALID_INTEGER, parameter)
        }
    }

    companion object {
        fun getValue(am: ArgumentMarshaler?): Int {
            return if (am != null && am is IntegerArgumentMarshaler) am.intValue else 0
        }

        fun cast(value: Any?): Int? =
            value as? Int
    }
}