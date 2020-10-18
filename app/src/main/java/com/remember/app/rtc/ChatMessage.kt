package com.remember.app.rtc

data class ChatMessage (
    val isOwner: Boolean,
    val userName: String,
    val date: String,
    val message: String
)