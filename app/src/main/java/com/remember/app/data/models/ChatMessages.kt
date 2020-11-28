package com.remember.app.data.models


import com.google.gson.annotations.SerializedName

data class ChatMessages(
    @SerializedName("count")
    val count: Int, // 10
    @SerializedName("history")
    val history: List<History>
) {
    data class History(
        @SerializedName("content")
        var content: String, // eqweqwe
        @SerializedName("date")
        var date: String, // 2020-10-07T07:03:43.000Z
        @SerializedName("id")
        val id: Int, // 1
        @SerializedName("is_read")
        var isRead: Boolean = false, // false
        @SerializedName("user_id")
        val userId: Int, // 1023
        var isSelect : Boolean = false
    )
}