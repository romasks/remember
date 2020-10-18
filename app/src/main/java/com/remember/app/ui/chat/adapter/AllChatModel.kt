package com.remember.app.ui.chat.adapter

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AllChatModel(
    val chatType: String?, // ukChat
    val id: String?, // b3731c3d-1fc0-4e0b-84d2-b225f1dc8418
    val isMy: Boolean?, // false
    val lastSeen: Long?, // 1600417043671
    var message: String?, // null
    val messageTime: Long?, // null
    val name: String?, // Тест УК
    var newMessages: Int?, // 0
    val numberId: Int? // 22
) : Parcelable