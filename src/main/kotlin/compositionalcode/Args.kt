package compositionalcode

import compositionalcode.ArgsException.ErrorCode.*
import java.util.*

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

    constructor(schemaDsl: String, args: Array<String?>): this(Schema.parseFromDsl(schemaDsl), args)


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
}