package compositionalcode


interface ArgumentMarshaler {
    fun extract(currentArgument: Iterator<String>?): Any
}