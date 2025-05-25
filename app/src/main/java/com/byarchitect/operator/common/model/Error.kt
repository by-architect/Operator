package com.byarchitect.operator.common.model


data class Error(
    val message: String,
    val localizedMessage: String,
    val cause: String,
    val errorLocation: String,
    val time: Long,
    val description: String?,
) {


    val json:String = """{"message": "$message", "localizedMessage": "$localizedMessage", "cause": "$cause", "errorLocation": "$errorLocation", "time": $time, "description": "$description"}"""

    override fun toString(): String {
        return "Error(message='$message', localizedMessage='$localizedMessage', cause='$cause', errorLocation='$errorLocation', time=$time, description=$description)"
    }
}