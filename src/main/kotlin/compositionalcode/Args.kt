package compositionalcode

import compositionalcode.ArgsException.ErrorCode.*
import java.util.*
import kotlin.collections.HashSet

class Args(private val schema: Schema, args: Array<String?>) {
    private val marshalers: Map<Char, ArgumentMarshaler> =
        schema.argumentMap.mapValues {
            it.value.createArgumentMarshaler()
        }

    private val argsFound: MutableSet<Char> = HashSet()

    private val argumentValues: Map<Char, Any>

    private var currentArgument: ListIterator<String>? = null

    init {
        argumentValues = parseArgumentStrings(Arrays.asList(*args))
    }

    constructor(schemaDsl: String, args: Array<String?>): this(Schema.parseFromDsl(schemaDsl), args)

    private fun parseArgumentStrings(argsList: MutableList<String>): Map<Char, Any> {
        currentArgument = argsList.listIterator()

        return buildMap {
            while (currentArgument!!.hasNext()) {
                val argString = currentArgument!!.next()
                if (argString.startsWith("-")) {
                    val argMap = parseArgumentCharacters(argString.substring(1))
                    putAll(argMap)
                } else {
                    currentArgument!!.previous()
                    break
                }
            }
        }
    }

    private fun parseArgumentCharacters(argChars: String): Map<Char, Any> {
        return buildMap {
            for (element in argChars)
                put(element, parseArgumentCharacter(element))
        }
    }

    private fun parseArgumentCharacter(argChar: Char): Any {
        val m: ArgumentMarshaler? = marshalers[argChar]
        if (m == null) {
            throw ArgsException(UNEXPECTED_ARGUMENT, argChar, null)
        } else {
            argsFound.add(argChar) // CORE EFFECT!!!!!!
            try {
                return m.extract(currentArgument) // CORE EFFECT!!!!!!
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
        return StringArrayArgumentMarshaler.cast(argumentValues[arg])
    }
}