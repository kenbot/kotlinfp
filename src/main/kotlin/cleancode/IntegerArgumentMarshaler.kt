package cleancode

import cleancode.ArgsException.ErrorCode.*


class IntegerArgumentMarshaler : ArgumentMarshaler {
    private var intValue = 0

    override fun set(currentArgument: MutableIterator<String>?) {
        var parameter: String? = null
        try {
            parameter = currentArgument!!.next()
            intValue = parameter.toInt()
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
    }
}