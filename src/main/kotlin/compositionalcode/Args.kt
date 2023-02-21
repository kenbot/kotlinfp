package compositionalcode

import compositionalcode.ArgsException.ErrorCode.*


class Args(private val schema: Schema, args: Array<String>) {

    private val argumentValues: Map<Char, Any>
    private val nextArgument: Int

    init {
        val (argumentValues, nextArgument) = parseArgumentStrings(listOf(*args))
        this.argumentValues = argumentValues
        this.nextArgument = nextArgument
    }

    constructor(schemaDsl: String, args: Array<String>): this(Schema.parseFromDsl(schemaDsl), args)

    private fun parseArgumentStrings(argsList: List<String>): Pair<Map<Char, Any>, Int> {
        val currentArgument: ListIterator<String> = argsList.listIterator()
        val argumentValuesSoFar: MutableMap<Char, Any> = mutableMapOf()

        fun parseArgumentCharacter(argChar: Char): Any {
            val m: ArgumentMarshaler? = schema.argumentMap[argChar]?.createArgumentMarshaler()
            if (m == null) {
                throw ArgsException(UNEXPECTED_ARGUMENT, argChar, null)
            } else {
                try {
                    val existingValue: Any? = argumentValuesSoFar[argChar]
                    return m.extract(currentArgument, existingValue)
                } catch (e: ArgsException) {
                    e.errorArgumentId = argChar
                    throw e
                }
            }
        }

        fun parseArgumentCharacters(argChars: String) {
            for (element in argChars) {
                argumentValuesSoFar[element] = parseArgumentCharacter(element)
            }
        }

        while (currentArgument.hasNext()) {
            val argString = currentArgument.next()
            if (argString.startsWith("-")) {
                parseArgumentCharacters(argString.substring(1))
            } else {
                currentArgument.previous()
                break
            }
        }

        return Pair(buildMap { putAll(argumentValuesSoFar) }, currentArgument.nextIndex())
    }



    fun has(arg: Char): Boolean {
        return argumentValues.containsKey(arg)
    }

    fun nextArgument(): Int {
        return nextArgument;
    }

    fun getBoolean(arg: Char): Boolean {
        return BooleanArgumentMarshaler.cast(argumentValues[arg])
    }

    fun getString(arg: Char): String? {
        return StringArgumentMarshaler.cast(argumentValues[arg])
    }

    fun getInt(arg: Char): Int? {
        return IntegerArgumentMarshaler.cast(argumentValues[arg])
    }

    fun getDouble(arg: Char): Double? {
        return DoubleArgumentMarshaler.cast(argumentValues[arg])
    }

    fun getStringArray(arg: Char): Array<String>? {
        return StringArrayArgumentMarshaler.cast(argumentValues[arg])?.toTypedArray()
    }
}