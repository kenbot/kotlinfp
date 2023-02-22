package compositionalcode

data class Schema(private val argumentMap: Map<ArgumentId, ArgumentType>) {

    fun getArgumentType(argumentId: ArgumentId): ArgumentType? =
        argumentMap[argumentId]

    companion object {
        @Throws(ArgsException::class)
        fun parseFromDsl(schemaDsl: String): Schema {
            val elements = schemaDsl.split(",")
                .filter { it.isNotEmpty() }
                .map { it.trim() }

            val argumentMap: Map<ArgumentId, ArgumentType> = buildMap {
                for (e in elements) {
                    val (char, arg) = parseSchemaElement(e.trim())
                    put(char, arg)
                }
            }

            return Schema(argumentMap)
        }

        @Throws(ArgsException::class)
        private fun parseSchemaElement(element: String): Pair<ArgumentId, ArgumentType> {
            val elementId = element[0]
            val elementTail = element.substring(1)
            validateSchemaElementId(elementId)

            val argType: ArgumentType =
                when (elementTail) {
                    "" -> BooleanArgumentType
                    "*" -> StringArgumentType
                    "#" -> IntegerArgumentType
                    "##" -> DoubleArgumentType
                    "[*]" -> StringArrayArgumentType
                    else -> throw ArgsException(ArgsException.ErrorCode.INVALID_ARGUMENT_FORMAT, elementId, elementTail)
                }

            return Pair(elementId, argType)
        }

        @Throws(ArgsException::class)
        private fun validateSchemaElementId(elementId: ArgumentId) {
            if (!Character.isLetter(elementId))
                throw ArgsException(ArgsException.ErrorCode.INVALID_ARGUMENT_NAME, elementId, null)
        }
    }

}