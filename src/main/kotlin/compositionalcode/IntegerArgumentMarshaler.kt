package compositionalcode

import compositionalcode.ArgsException.ErrorCode.*


object IntegerArgumentMarshaler : ArgumentMarshaler {
    override fun extract(currentArgument: Iterator<String>, existing: Any?): Int {
        var parameter: String? = null
        try {
            parameter = currentArgument.next()
            return parameter.toInt()
        } catch (e: NoSuchElementException) {
            throw ArgsException(MISSING_INTEGER)
        } catch (e: NumberFormatException) {
            throw ArgsException(INVALID_INTEGER, parameter)
        }
    }

    fun cast(value: Any?): Int? =
        value as? Int
}