package com.remember.app.data.models


import com.google.gson.annotations.SerializedName

data class ChatInfoResponse(
    @SerializedName("members")
    val members: List<Member>
) {
    data class Member(
        @SerializedName("id")
        val id: Int, // 1023
        @SerializedName("name")
        val name: String, // eeee
        @SerializedName("picture")
        val picture: String // /uploads/accounts/1.jpg
    )
}