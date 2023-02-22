package compositionalcode


sealed interface ArgumentType {
    @Throws(ArgsException::class)
    fun parseArgumentValue(currentArgument: Iterator<String>, existing: Any?): Any
}