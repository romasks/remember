package com.remember.app.data.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatsModel(
        @SerializedName("chats")
        val chats: List<Chat>,
        @SerializedName("count")
        val count: Int // 1
) : Parcelable {
    @Parcelize
    data class Chat(
            @SerializedName("id")
            val id: Int, // 1
            @SerializedName("last_message")
            val lastMessage: String, // 2020-10-18T14:50:48.000Z
            @SerializedName("last_msg")
            val lastMsg: LastMsg?,
            @SerializedName("name")
            val name: String, // Помню
            @SerializedName("picture")
            val picture: String?, // /uploads/accounts/1.jpg
            @SerializedName("unread_count")
            val unreadCount: Int // 50
    ) : Parcelable {
        @Parcelize
        data class LastMsg(
                @SerializedName("is_read")
                val isRead: Boolean, // false
                @SerializedName("text")
                val text: String, // ghhg
                @SerializedName("type")
                val type: String // IN
        ) : Parcelable
    }
}