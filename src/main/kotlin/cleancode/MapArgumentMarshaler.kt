package cleancode

import cleancode.ArgsException.ErrorCode.*


class MapArgumentMarshaler : ArgumentMarshaler {
    private val map: MutableMap<String, String> = HashMap()

    override fun set(currentArgument: Iterator<String>?) {
        try {
            val mapEntries = currentArgument!!.next().split(",")
            for (entry in mapEntries) {
                val entryComponents = entry.split(":")
                if (entryComponents.size != 2) throw ArgsException(MALFORMED_MAP)
                map[entryComponents[0]] = entryComponents[1]
            }
        } catch (e: NoSuchElementException) {
            throw ArgsException(MISSING_MAP)
        }
    }

    companion object {
        fun getValue(am: ArgumentMarshaler?): Map<String, String> {
            return if (am != null && am is MapArgumentMarshaler) am.map else HashMap()
        }
    }
}