package compositionalcode


object BooleanArgumentMarshaler : ArgumentMarshaler {
    override fun extract(currentArgument: Iterator<String>, existing: Any?): Boolean {
        return true
    }

    fun cast(value: Any?): Boolean =
        if (value == null) false
        else value as Boolean

}