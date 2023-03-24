package accountsapp

import java.util.*

data class AnnualReport(
    val financialYear: Int,
    val dateGenerated: Date,
    val entries: List<ReportEntry>,
    val generatedBy: UserId
) {

    companion object {
        fun generate(user: User, request: AnnualReportRequest): AnnualReport {
            TODO("not implemented")
        }
    }
}