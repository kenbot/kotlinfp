package accountsapp

import arrow.core.Either

fun interface Authenticator {
    fun authenticate(userId: UserId, password: Password): Either<AuthFailed, User>
}
