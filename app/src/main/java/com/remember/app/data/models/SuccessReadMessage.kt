package com.remember.app.data.models


import com.google.gson.annotations.SerializedName

data class SuccessReadMessage(
    @SerializedName("ok")
    val ok: Boolean // true
)