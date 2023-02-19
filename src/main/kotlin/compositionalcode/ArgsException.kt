package compositionalcode

import compositionalcode.ArgsException.ErrorCode.*


class ArgsException : Exception {
    var errorArgumentId = '\u0000'
    var errorParameter: String? = null
    var errorCode: ErrorCode = OK

    constructor()
    constructor(message: String?) : super(message)
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
            ErrorCode.OK -> "TILT: Should not get here."
            ErrorCode.UNEXPECTED_ARGUMENT -> String.format("Argument -%c unexpected.", errorArgumentId)
            ErrorCode.MISSING_STRING -> String.format("Could not find string parameter for -%c.", errorArgumentId)
            ErrorCode.INVALID_INTEGER -> String.format(
                "Argument -%c expects an integer but was '%s'.",
                errorArgumentId,
                errorParameter
            )

            ErrorCode.MISSING_INTEGER -> String.format("Could not find integer parameter for -%c.", errorArgumentId)
            ErrorCode.INVALID_DOUBLE -> String.format(
                "Argument -%c expects a double but was '%s'.",
                errorArgumentId,
                errorParameter
            )

            ErrorCode.MISSING_DOUBLE -> String.format("Could not find double parameter for -%c.", errorArgumentId)
            ErrorCode.INVALID_ARGUMENT_NAME -> String.format("'%c' is not a valid argument name.", errorArgumentId)
            ErrorCode.INVALID_ARGUMENT_FORMAT -> String.format("'%s' is not a valid argument format.", errorParameter)
            ErrorCode.MISSING_MAP -> String.format("Could not find map string for -%c.", errorArgumentId)
            ErrorCode.MALFORMED_MAP -> String.format(
                "Map string for -%c is not of form k1:v1,k2:v2...",
                errorArgumentId
            )
        }
        return ""
    }

    enum class ErrorCode {
        OK, INVALID_ARGUMENT_FORMAT, UNEXPECTED_ARGUMENT, INVALID_ARGUMENT_NAME,
        MISSING_STRING, MISSING_INTEGER, INVALID_INTEGER, MISSING_DOUBLE,
        MALFORMED_MAP, MISSING_MAP, INVALID_DOUBLE
    }
}