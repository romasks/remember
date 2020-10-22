package com.remember.app.socket

data class ChatMessage (
    val isOwner: Boolean,
    val userName: String,
    val date: String,
    val message: String
)