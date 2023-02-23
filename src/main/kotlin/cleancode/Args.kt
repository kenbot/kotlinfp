package cleancode

import cleancode.ArgsException.ErrorCode.*
import compositionalcode.ArgumentType
import java.util.*


class Args(schema: String, args: Array<String?>) {
    private val marshalers: MutableMap<Char, ArgumentMarshaler>
    private val argsFound: MutableSet<Char>
    private var currentArgument: MutableListIterator<String>? = null

    init {
        marshalers = HashMap<Char, ArgumentMarshaler>()
        argsFound = HashSet()
        parseSchema(schema)
        parseArgumentStrings(Arrays.asList(*args))
    }

    private fun parseSchema(schema: String) {
        for (element in schema.split(","))
            if (element.length > 0)
                parseSchemaElement(element.trim())
    }

    private fun parseSchemaElement(element: String) {
        val elementId = element[0]
        val elementTail = element.substring(1)
        validateSchemaElementId(elementId)

        val marshaler: ArgumentMarshaler =
            when (elementTail) {
                "" -> BooleanArgumentMarshaler()
                "*" -> StringArgumentMarshaler()
                "#" -> IntegerArgumentMarshaler()
                "##" -> DoubleArgumentMarshaler()
                "[*]" -> StringArrayArgumentMarshaler()
                else -> throw ArgsException(INVALID_ARGUMENT_FORMAT, elementId, elementTail)
            }

        marshalers[elementId] = marshaler
    }

    private fun validateSchemaElementId(elementId: Char) {
        if (!Character.isLetter(elementId)) throw ArgsException(INVALID_ARGUMENT_NAME, elementId, null)
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
            argsFound.add(argChar)
            try {
                m.set(currentArgument)
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

    override fun toString(): String {
        val argumentValues: Map<Char, Any> = buildMap {
            argsFound.forEach {
                when (marshalers[it]) {
                    is BooleanArgumentMarshaler -> put(it, getBoolean(it))
                    is IntegerArgumentMarshaler -> put(it, getInt(it))
                    is DoubleArgumentMarshaler -> put(it, getInt(it))
                    is StringArgumentMarshaler -> put(it, getString(it))
                    is StringArrayArgumentMarshaler -> put(it, getStringArray(it).toList())
                }
            }
        }

        return "Args $argumentValues"
    }


}