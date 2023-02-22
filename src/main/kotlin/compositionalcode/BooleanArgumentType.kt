package compositionalcode


object BooleanArgumentType : ArgumentType {
    override fun parseArgumentValue(currentArgument: Iterator<String>, existing: Any?): Boolean {
        return true
    }

    fun cast(value: Any?): Boolean =
        if (value == null) false
        else value as Boolean

}