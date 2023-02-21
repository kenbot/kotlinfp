package compositionalcode

import compositionalcode.ArgsException.ErrorCode.*


object StringArrayArgumentMarshaler : ArgumentMarshaler {
    override fun extract(currentArgument: Iterator<String>, existing: Any?): List<String> {
        try {
            val latestValue = currentArgument.next()
            return buildList {
                if (existing as? List<String> != null)
                    addAll(existing)
                add(latestValue)
            }
        } catch (e: NoSuchElementException) {
            throw ArgsException(MISSING_STRING)
        }
    }

    fun cast(value: Any?): List<String>? =
        value as? List<String>

}