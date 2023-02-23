package cleancode


interface ArgumentMarshaler {
    fun set(currentArgument: MutableIterator<String>?)
}