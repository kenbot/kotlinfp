package compositionalcode

import compositionalcode.ArgsException.ErrorCode.*


class MapArgumentMarshaler : ArgumentMarshaler {
    private val map: MutableMap<String, String> = HashMap()

    override fun set(currentArgument: Iterator<String>?) {
        try {
            val mapEntries = currentArgument!!.next().split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            for (entry in mapEntries) {
                val entryComponents = entry.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
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