package com.remember.app.data.dataFlow

import com.remember.app.data.models.ChatMessages
import com.remember.app.data.models.SuccessSendMessage
import com.remember.app.data.models.ChatsModel
import com.remember.app.data.models.SuccessReadMessage
import okhttp3.RequestBody

interface IDataManager {
    fun getChatList(
            token: String,
            limit: Int,
            offset: Int,
            onSuccess: (response: ChatsModel) -> Unit,
            onFailure: (error: Throwable) -> Unit
    )

    fun getChatMessage(
            token: String,
            id: Int,
            limit: Int,
            offset: Int,
            onSuccess: (response: ChatMessages) -> Unit,
            onFailure: (error: Throwable) -> Unit
    )

    fun sendMessage(
            token: String,
            id: String,
            data: String,
            onSuccess: (response: SuccessSendMessage) -> Unit,
            onFailure: (error: Throwable) -> Unit
    )

    fun deleteChat(token: String,
                   id: Int,
                   onSuccess: (response: SuccessSendMessage) -> Unit,
                   onFailure: (error: Throwable) -> Unit
    )

    fun getChatInfo(token: String,
                    id: Int,
                    onSuccess: (response: SuccessSendMessage) -> Unit,
                    onFailure: (error: Throwable) -> Unit
    )

    fun deleteMessage(
            token: String,
            id: Int,
            messageID : Int,
            deleteAll : Boolean,
            onSuccess: (response: SuccessSendMessage) -> Unit,
            onFailure: (error: Throwable) -> Unit
    )

    fun editMessage(
            token: String,
            id: Int,
            messageID : Int,
            data: RequestBody,
            onSuccess: (response: SuccessSendMessage) -> Unit,
            onFailure: (error: Throwable) -> Unit
    )

    fun readMark(
            token: String,
            id: Int,
            messageID : Int,
            onSuccess: (response: SuccessReadMessage) -> Unit,
            onFailure: (error: Throwable) -> Unit
    )
}