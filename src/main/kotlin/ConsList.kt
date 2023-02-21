
sealed interface ConsList<out A> {
    fun <B> foldLeft(initial: B, f: (B, A) -> B): B =
        when (this) {
            is Cons -> f(this.tail.foldLeft(initial, f), this.head)
            is Empty -> initial
        }
}

data class Cons<A>(val head: A, val tail: ConsList<A>) : ConsList<A>

object Empty : ConsList<Nothing> {
    override fun toString(): String = "Empty"
}

infix fun <A> A.cons(tail: ConsList<A>): ConsList<A> =
    Cons(this, tail)
