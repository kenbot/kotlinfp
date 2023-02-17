
sealed interface Attempt<out A> {

    fun <B> map(f: (A) -> B): Attempt<B>
    fun <B> flatMap(f: (A) -> Attempt<B>): Attempt<B>

    data class Success<A>(val value: A) : Attempt<A> {
        override fun <B> map(f: (A) -> B): Attempt<B> {
            return Success(f(value))
        }

        override fun <B> flatMap(f: (A) -> Attempt<B>): Attempt<B> {
            return f(value)
        }
    }

    object Failure: Attempt<Nothing> {
        override fun <B> map(f: (Nothing) -> B): Attempt<B> {
            return Failure
        }

        override fun <B> flatMap(f: (Nothing) -> Attempt<B>): Attempt<B> {
            return Failure
        }

        override fun toString(): String = "Failure"
        override fun equals(o: Any?): Boolean = o == this
    }
}


fun <A> List<A>.safeGet(i: Int): Attempt<A> {
    return if (this.size > i) Attempt.Success(this[i])
    else Attempt.Failure
}
