package compositionalcode


sealed interface ArgumentType {
    fun parseArgumentValue(currentArgument: Iterator<String>, existing: Any?): Any
}