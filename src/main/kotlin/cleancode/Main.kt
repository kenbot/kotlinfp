package cleancode

fun main(args: Array<String>) {
    try {
        val arg = Args("l,p#,d*,x[*]", args as Array<String?>)
        val logging: Boolean = arg.getBoolean('l')
        val port: Int = arg.getInt('p')
        val directory: String = arg.getString('d')
        val comments: Array<String> = arg.getStringArray('x')

        executeApplication(logging, port, directory)
        logApplicationArgs(arg)
    } catch (e: ArgsException) {
        println("Argument error: ${e.message}")
    }
}

fun logApplicationArgs(arg: Args) {
    println("[INFO] $arg")
}

fun executeApplication(logging: Boolean, port: Int, directory: String) {
    println("Executing cleancode application with logging=$logging, port=$port, directory=$directory...")
}

