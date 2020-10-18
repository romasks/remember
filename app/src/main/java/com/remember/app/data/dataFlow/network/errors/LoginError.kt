package com.ambitt.dataFlow.network.errors


import com.google.gson.annotations.SerializedName

data class LoginError(
    @SerializedName("errors")
    val errors: Errors
) {
    data class Errors(
        @SerializedName("password")
        val password: List<String>,
        @SerializedName("detail")
        val detail: String
    )
}