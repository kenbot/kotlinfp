package compositionalcode

import compositionalcode.ArgsException.ErrorCode.*


object DoubleArgumentMarshaler : ArgumentMarshaler {
    override fun extract(currentArgument: Iterator<String>, existing: Any?): Double {
        var parameter: String? = null
        try {
            parameter = currentArgument.next()
            return parameter.toDouble()
        } catch (e: NoSuchElementException) {
            throw ArgsException(MISSING_DOUBLE)
        } catch (e: NumberFormatException) {
            throw ArgsException(INVALID_DOUBLE, parameter)
        }
    }

    fun cast(value: Any?): Double? =
        value as? Double
}