package cleancode

import cleancode.ArgsException.ErrorCode.*
import java.util.*


class Args(schema: String, args: Array<String?>) {
    private val marshalers: MutableMap<Char, ArgumentMarshaler>
    private val argsFound: MutableSet<Char>
    private var currentArgument: ListIterator<String>? = null

    init {
        marshalers = HashMap<Char, ArgumentMarshaler>()
        argsFound = HashSet()
        parseSchema(schema)
        parseArgumentStrings(Arrays.asList(*args))
    }

    private fun parseSchema(schema: String) {
        for (element in schema.split(",".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()) if (element.length > 0) parseSchemaElement(element.trim { it <= ' ' })
    }

    private fun parseSchemaElement(element: String) {
        val elementId = element[0]
        val elementTail = element.substring(1)
        validateSchemaElementId(elementId)
        if (elementTail.length == 0) marshalers[elementId] =
            BooleanArgumentMarshaler() else if (elementTail == "*") marshalers[elementId] =
            StringArgumentMarshaler() else if (elementTail == "#") marshalers[elementId] =
            IntegerArgumentMarshaler() else if (elementTail == "##") marshalers[elementId] =
            DoubleArgumentMarshaler() else if (elementTail == "[*]") marshalers[elementId] =
            StringArrayArgumentMarshaler() else if (elementTail == "&") marshalers[elementId] =
            MapArgumentMarshaler() else throw ArgsException(INVALID_ARGUMENT_FORMAT, elementId, elementTail)
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

    fun getMap(arg: Char): Map<String, String> {
        return MapArgumentMarshaler.getValue(marshalers[arg])
    }
}