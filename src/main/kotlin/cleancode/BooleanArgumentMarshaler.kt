package cleancode


class BooleanArgumentMarshaler : ArgumentMarshaler {
    private var booleanValue = false

    override fun set(currentArgument: Iterator<String?>?) {
        booleanValue = true
    }

    companion object {
        fun getValue(am: ArgumentMarshaler?): Boolean {
            return if (am != null && am is BooleanArgumentMarshaler) am.booleanValue else false
        }
    }
}