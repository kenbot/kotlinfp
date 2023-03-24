package accountsapp

import java.util.*


@JvmInline
value class UserId(private val value: String)

@JvmInline
value class Password(private val value: String)

@JvmInline
value class Json(val string: String)

data class ReportEntry(val a: Int, val b: String)


data class AnnualReportRequest(val something: String)
data class AnnualReportResponse(val something: String)
data class User(val id: UserId, val name: String)

sealed interface Failure
data class AuthFailed(val userId: UserId): Failure
object JsonParseFailed: Failure