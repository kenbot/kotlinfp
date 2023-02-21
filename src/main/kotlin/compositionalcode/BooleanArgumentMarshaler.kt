package compositionalcode


class BooleanArgumentMarshaler : ArgumentMarshaler {
    private var booleanValue = false

    override fun extract(currentArgument: Iterator<String>?): Boolean {
        booleanValue = true
        return booleanValue
    }

    companion object {
        fun getValue(am: ArgumentMarshaler?): Boolean {
            return if (am != null && am is BooleanArgumentMarshaler) am.booleanValue else false
        }

        fun cast(value: Any?): Boolean =
            if (value == null) false
            else value as Boolean

    }
}