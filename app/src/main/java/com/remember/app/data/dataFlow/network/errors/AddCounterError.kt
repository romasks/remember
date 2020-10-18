package com.ambitt.dataFlow.network.errors

import com.google.gson.annotations.SerializedName

data class AddCounterError(
    @SerializedName("code")
    val code: Int, // 200
    @SerializedName("status")
    val status: String,
    @SerializedName("fail")
    val fail: ArrayList<Fail>
) {
    data class Fail(
        @SerializedName("type_counter")
        val type_counter: String
    )
}