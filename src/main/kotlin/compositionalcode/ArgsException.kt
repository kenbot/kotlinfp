package compositionalcode

import compositionalcode.ArgsException.ErrorCode.*


class ArgsException : Exception {
    var errorArgumentId: ArgumentId = '\u0000'
    var errorParameter: String? = null
    val errorCode: ErrorCode

    constructor(errorCode: ErrorCode) {
        this.errorCode = errorCode
    }

    constructor(errorCode: ErrorCode, errorParameter: String?) {
        this.errorCode = errorCode
        this.errorParameter = errorParameter
    }

    constructor(errorCode: ErrorCode, errorArgumentId: Char, errorParameter: String?) {
        this.errorCode = errorCode
        this.errorParameter = errorParameter
        this.errorArgumentId = errorArgumentId
    }

    fun errorMessage(): String {
        return when (errorCode) {
            INVALID_ARGUMENT_FORMAT -> "'$errorParameter' is not a valid argument format."
            INVALID_ARGUMENT_NAME -> "'$errorArgumentId' is not a valid argument name."
            UNEXPECTED_ARGUMENT -> "Argument -$errorArgumentId unexpected."
            MISSING_STRING -> "Could not find string parameter for -$errorArgumentId."
            INVALID_INTEGER -> "Argument -$errorArgumentId expects an integer but was '$errorParameter'."
            MISSING_INTEGER -> "Could not find integer parameter for -$errorArgumentId."
            INVALID_DOUBLE -> "Argument -${errorArgumentId} expects a double but was '$errorParameter'."
            MISSING_DOUBLE -> "Could not find double parameter for -$errorArgumentId."
        }
    }

    enum class ErrorCode {
        // Schema parser
        INVALID_ARGUMENT_FORMAT, // Schema parser failed; unrecognised ArgumentType sigil. ArgumentId, parameter
        INVALID_ARGUMENT_NAME, // Schema parser failed; invalid ArgumentId character. ArgumentId

        // Argument parser
        UNEXPECTED_ARGUMENT, // parseArgumentStrings failed; No ArgumentType registered for ArgumentId. ArgumentId

        // Argument type parsers. All of these get re-thrown with ArgumentId added.
        MISSING_STRING, // String[Array]ArgumentType failed; No arguments left to parse when expecting a string. No params
        MISSING_INTEGER, // IntArgumentType failed; no arguments left to parse when expecting an integer. No params.
        INVALID_INTEGER, // IntArgumentType failed; argument couldn't be parsed as an integer. parameter
        MISSING_DOUBLE,  // DoubleArgumentType failed; no arguments left to parse when expecting a double. No params.
        INVALID_DOUBLE  // DoubleArgumentType failed; argument couldn't be parsed as a double. parameter
    }
}