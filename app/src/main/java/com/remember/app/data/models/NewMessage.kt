package com.remember.app.data.models


import com.google.gson.annotations.SerializedName

data class NewMessage(
    @SerializedName("from")
    val from: From,
    @SerializedName("message")
    val message: Message
) {
    data class From(
        @SerializedName("id")
        val id: Int // 1
    )

    data class Message(
        @SerializedName("id")
        val id: Int, // 22
        @SerializedName("text")
        val text: String // yuulkjlфываыва
    )
}