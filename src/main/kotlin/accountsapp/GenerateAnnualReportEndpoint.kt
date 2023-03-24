package accountsapp
import arrow.core.Either
import arrow.core.computations.either



class GenerateAnnualReportEndpoint(private val authenticator: Authenticator) {

    suspend fun generateAnnualReport(userId: UserId, password: Password, jsonRequest: Json): Json {
        val userOrFail = authenticator.authenticate(userId, password)
        val requestOrFail = parseRequest(jsonRequest)

        val reportOrFail = either {
            val user = userOrFail.bind()
            val request = requestOrFail.bind()
            AnnualReport.generate(user, request)
        }

        return when (reportOrFail) {
            is Either.Left -> generateFailureJson(reportOrFail.value)
            is Either.Right -> generateResponseJson(reportOrFail.value)
        }
    }

    companion object {
        fun parseRequest(requestJson: Json): Either<JsonParseFailed, AnnualReportRequest> {
            TODO("not implemented")
        }

        private fun generateResponseJson(response: AnnualReport): Json {
            TODO("not implemented")
        }

        private fun generateFailureJson(failure: Failure): Json =
            when (failure) {
                is AuthFailed -> Json("""{ "errorMessage": "Couldn't authenticate user ${failure.userId}" }""")
                is JsonParseFailed -> Json("""{ "errorMessage": "Couldn't parse JSON" }""")
            }
    }
}






