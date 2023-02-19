package compositionalcode

enum class ArgumentType {
    INTEGER,
    STRING,
    DOUBLE,
    BOOLEAN,
    STRING_ARRAY,
    MAP;

    fun createArgumentMarshaler(): ArgumentMarshaler =
        when (this) {
            INTEGER -> IntegerArgumentMarshaler()
            STRING -> StringArgumentMarshaler()
            DOUBLE -> DoubleArgumentMarshaler()
            BOOLEAN -> BooleanArgumentMarshaler()
            STRING_ARRAY -> StringArrayArgumentMarshaler()
            MAP -> MapArgumentMarshaler()
        }
}