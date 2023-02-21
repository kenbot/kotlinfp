package cleancode

import cleancode.ArgsException.ErrorCode.*


class DoubleArgumentMarshaler : ArgumentMarshaler {
    private var doubleValue = 0.0

    override fun set(currentArgument: Iterator<String>?) {
        var parameter: String? = null
        try {
            parameter = currentArgument!!.next()
            doubleValue = parameter.toDouble()
        } catch (e: NoSuchElementException) {
            throw ArgsException(MISSING_DOUBLE)
        } catch (e: NumberFormatException) {
            throw ArgsException(INVALID_DOUBLE, parameter)
        }
    }

    companion object {
        fun getValue(am: ArgumentMarshaler?): Double {
            return if (am != null && am is DoubleArgumentMarshaler) am.doubleValue else 0.0
        }
    }
}