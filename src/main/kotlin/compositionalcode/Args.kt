package compositionalcode

import compositionalcode.ArgsException.ErrorCode.*

typealias ArgumentIndex = Int
typealias ArgumentId = Char

class Args(private val schema: Schema, args: Array<String>) {

    private val argumentValues: Map<ArgumentId, Any>
    private val nextArgument: ArgumentIndex

    init {
        val (argumentValues, nextArgument) = parseArgumentStrings(listOf(*args))
        this.argumentValues = argumentValues
        this.nextArgument = nextArgument
    }

    constructor(schemaDsl: String, args: Array<String>): this(Schema.parseFromDsl(schemaDsl), args)

    @Throws(ArgsException::class)
    private fun parseArgumentStrings(argsList: List<String>): Pair<Map<ArgumentId, Any>, ArgumentIndex> {
        val currentArgument: ListIterator<String> = argsList.listIterator()
        val argumentValuesSoFar: MutableMap<ArgumentId, Any> = mutableMapOf()

        @Throws(ArgsException::class)
        fun parseArgumentCharacter(argChar: ArgumentId): Any {
            val parser: ArgumentType? = schema.getArgumentType(argChar)
            if (parser == null) {
                throw ArgsException(UNEXPECTED_ARGUMENT, argChar, null)
            } else {
                try {
                    val existingValue: Any? = argumentValuesSoFar[argChar]
                    return parser.parseArgumentValue(currentArgument, existingValue)
                } catch (e: ArgsException) {
                    e.errorArgumentId = argChar
                    throw e
                }
            }
        }

        @Throws(ArgsException::class)
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



    fun has(arg: ArgumentId): Boolean =
        argumentValues.containsKey(arg)

    fun nextArgument(): Int =
        nextArgument;

    fun getBoolean(arg: ArgumentId): Boolean =
        BooleanArgumentType.cast(argumentValues[arg])

    fun getString(arg: ArgumentId): String? =
        StringArgumentType.cast(argumentValues[arg])

    fun getInt(arg: ArgumentId): Int? =
        IntegerArgumentType.cast(argumentValues[arg])

    fun getDouble(arg: ArgumentId): Double? =
        DoubleArgumentType.cast(argumentValues[arg])

    fun getStringArray(arg: ArgumentId): Array<String>? =
        StringArrayArgumentType.cast(argumentValues[arg])?.toTypedArray()
}