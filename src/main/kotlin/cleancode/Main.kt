package cleancode

fun main(args: Array<String>) {
    try {
        val arg = Args("l,p#,d*", args as Array<String?>)
        val logging: Boolean = arg.getBoolean('l')
        val port: Int = arg.getInt('p')
        val directory: String = arg.getString('d')
        executeApplication(logging, port, directory)
    } catch (e: ArgsException) {
        println("Argument error: ${e.message}")
    }
}

fun executeApplication(logging: Boolean, port: Int, directory: String) {
    println("Executing application with logging=$logging, port=$port, directory=$directory...")
}

