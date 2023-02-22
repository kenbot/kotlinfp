package compositionalcode

import compositionalcode.ArgsException.ErrorCode.MISSING_STRING


object StringArgumentType : ArgumentType {
    @Throws(ArgsException::class)
    override fun parseArgumentValue(currentArgument: Iterator<String>, existing: Any?): String {
        try {
            return currentArgument.next()
        } catch (e: NoSuchElementException) {
            throw ArgsException(MISSING_STRING)
        }
    }

    fun cast(value: Any?): String? =
        value as? String
}