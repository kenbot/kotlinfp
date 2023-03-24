package accountsapp

suspend fun main() {
    val generateReportEndpoint = GenerateAnnualReportEndpoint(RealLifeAuthenticator)

    generateReportEndpoint.generateAnnualReport(
        UserId("bob85"),
        Password("biscuits"),
        Json("{}")
    );
}
