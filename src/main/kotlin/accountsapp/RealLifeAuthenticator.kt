package accountsapp

import arrow.core.Either
import kotlin.random.Random


object RealLifeAuthenticator: Authenticator {
    override fun authenticate(userId: UserId, password: Password): Either<AuthFailed, User> {
        println("Calling actual real life Auth API...")

        return if (Random.nextDouble() < 0.95 && password == Password("biscuits")) {
            Either.Right(User(userId, "Bob"))
        } else {
            Either.Left(AuthFailed(userId))
        }
    }
}