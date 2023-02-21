package compositionalcode

import compositionalcode.ArgsException.ErrorCode.*


class DoubleArgumentMarshaler : ArgumentMarshaler {
    private var doubleValue = 0.0

    override fun extract(currentArgument: Iterator<String>?): Double {
        var parameter: String? = null
        try {
            parameter = currentArgument!!.next()
            doubleValue = parameter.toDouble()
            return doubleValue
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

        fun cast(value: Any?): Double? =
            value as? Double
    }
}