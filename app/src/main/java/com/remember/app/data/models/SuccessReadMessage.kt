package com.remember.app.data.models


import com.google.gson.annotations.SerializedName

data class SuccessReadMessage(
    @SerializedName("ok")
    val ok: Boolean, // true
    @SerializedName("count")
    val count: Int, // true
    @SerializedName("last_message_id")
    val last_message_id: Int // true
)