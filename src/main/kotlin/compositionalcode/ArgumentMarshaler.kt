package compositionalcode


interface ArgumentMarshaler {
    fun set(currentArgument: Iterator<String>?)
}