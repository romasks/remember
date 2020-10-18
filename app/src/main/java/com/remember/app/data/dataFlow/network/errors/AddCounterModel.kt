package com.ambitt.dataFlow.network.errors


import com.google.gson.annotations.SerializedName

data class AddCounterModel(
    @SerializedName("code")
    val code: String, // 200
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("status")
    val status: String // success
) {
    data class Data(
        @SerializedName("createdAt")
        val createdAt: Long, // 1597148207000
        @SerializedName("id")
        val id: String, // b6be5235-f7b8-4159-ad65-cb193e62b5ac
        @SerializedName("numberCounter")
        val numberCounter: Int, // 324
        @SerializedName("typeCounter")
        val typeCounter: String // electricity
    )
}