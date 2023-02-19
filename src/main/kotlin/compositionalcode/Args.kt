package compositionalcode

import compositionalcode.ArgsException.ErrorCode.*
import java.util.*


data class Schema(val argumentMap: Map<Char, ArgumentType>)

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


class Args(schema: Schema, args: Array<String?>) {
    private val marshalers: Map<Char, ArgumentMarshaler> =
        schema.argumentMap.mapValues {
            it.value.createArgumentMarshaler()
        }

    private val argsFound: MutableSet<Char> = HashSet()

    private var currentArgument: ListIterator<String>? = null

    init {
        parseArgumentStrings(Arrays.asList(*args))
    }

    constructor(schemaDsl: String, args: Array<String?>): this(parseSchema(schemaDsl), args)

    companion object {

        private fun parseSchema(schema: String): Schema {
            val elements =
                schema.split(",")
                    .filter { it.isNotEmpty() }
                    .map { it.trim() }

            val argumentMap: Map<Char, ArgumentType> = buildMap {
                for (e in elements) {
                    val (char, arg) = parseSchemaElement(e.trim())
                    put(char, arg)
                }
            }

            return Schema(argumentMap)
        }

        private fun parseSchemaElement(element: String): Pair<Char, ArgumentType> {
            val elementId = element[0]
            val elementTail = element.substring(1)
            validateSchemaElementId(elementId)

            val argType: ArgumentType =
                if (elementTail.isEmpty()) ArgumentType.BOOLEAN
                else if (elementTail == "*") ArgumentType.STRING
                else if (elementTail == "#") ArgumentType.INTEGER
                else if (elementTail == "##") ArgumentType.DOUBLE
                else if (elementTail == "[*]") ArgumentType.STRING_ARRAY
                else if (elementTail == "&") ArgumentType.MAP
                else throw ArgsException(INVALID_ARGUMENT_FORMAT, elementId, elementTail)

            return Pair(elementId, argType)
        }

        private fun validateSchemaElementId(elementId: Char) {
            if (!Character.isLetter(elementId))
                throw ArgsException(INVALID_ARGUMENT_NAME, elementId, null)
        }
    }

    private fun parseArgumentStrings(argsList: MutableList<String>) {
        currentArgument = argsList.listIterator()
        while (currentArgument!!.hasNext()) {
            val argString = currentArgument!!.next()
            if (argString.startsWith("-")) {
                parseArgumentCharacters(argString.substring(1))
            } else {
                currentArgument!!.previous()
                break
            }
        }
    }

    private fun parseArgumentCharacters(argChars: String) {
        for (element in argChars)
            parseArgumentCharacter(element)
    }

    private fun parseArgumentCharacter(argChar: Char) {
        val m: ArgumentMarshaler? = marshalers[argChar]
        if (m == null) {
            throw ArgsException(UNEXPECTED_ARGUMENT, argChar, null)
        } else {
            argsFound.add(argChar) // CORE EFFECT!!!!!!
            try {
                m.set(currentArgument) // CORE EFFECT!!!!!!
            } catch (e: ArgsException) {
                e.errorArgumentId = argChar
                throw e
            }
        }
    }

    fun has(arg: Char): Boolean {
        return argsFound.contains(arg)
    }

    fun nextArgument(): Int {
        return currentArgument!!.nextIndex()
    }

    fun getBoolean(arg: Char): Boolean {
        return BooleanArgumentMarshaler.getValue(marshalers[arg])
    }

    fun getString(arg: Char): String {
        return StringArgumentMarshaler.getValue(marshalers[arg])
    }

    fun getInt(arg: Char): Int {
        return IntegerArgumentMarshaler.getValue(marshalers[arg])
    }

    fun getDouble(arg: Char): Double {
        return DoubleArgumentMarshaler.getValue(marshalers[arg])
    }

    fun getStringArray(arg: Char): Array<String> {
        return StringArrayArgumentMarshaler.getValue(marshalers[arg])
    }

    fun getMap(arg: Char): Map<String, String> {
        return MapArgumentMarshaler.getValue(marshalers[arg])
    }
}