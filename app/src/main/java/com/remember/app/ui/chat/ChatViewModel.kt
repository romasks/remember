package com.remember.app.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pixplicity.easyprefs.library.Prefs
import com.remember.app.data.dataFlow.DataManager
import com.remember.app.data.models.ChatMessages
import com.remember.app.data.models.SuccessSendMessage
import com.remember.app.data.models.ChatsModel
import io.socket.client.Socket
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody


class ChatViewModel(private val dataManager: DataManager) :
    ViewModel() {

    private lateinit var socket: Socket
    private var firstCurrentChatMessage = false
    var allChatModel: MutableLiveData<ChatsModel> = MutableLiveData()
    var successSendMessage: MutableLiveData<SuccessSendMessage> = MutableLiveData()
    var chatMessages: MutableLiveData<ChatMessages> = MutableLiveData()
    var error: MutableLiveData<Boolean> = MutableLiveData()

    fun getChats(limit: Int = 20, offset: Int = 0) {
        dataManager.getChatList(limit = limit, offset = offset, token = getUserToken(), onSuccess = {
            it.let {
                allChatModel.postValue(it)
            }
        }, onFailure = {
            it.message
            //  loginErrorMessage.postValue(parseLoginError(it))
        })
    }

    fun sendMessage(chatID : String, message : String){
        val descr =
                message.toRequestBody(MediaType.let { "text/plain".toMediaTypeOrNull() })
        dataManager.sendMessage(id = chatID, token = getUserToken(), message = message, onSuccess = {
            it.let {
                successSendMessage.postValue(it)
            }
        }, onFailure = {
            error.postValue(true)
            //  loginErrorMessage.postValue(parseLoginError(it))
        })
    }

    fun getChatMessages(chatID : Int,limit: Int = 20, offset: Int = 0){
        dataManager.getChatMessage(id = chatID, token = getUserToken(),limit = limit, offset = offset, onSuccess = {
            it.let {
                chatMessages.postValue(it)
            }
        }, onFailure = {
            it.message
            //  loginErrorMessage.postValue(parseLoginError(it))
        })
    }

    fun instantiateSocket(sockett: Socket) {
        socket = sockett
    }

    fun getSocketConnection(): Socket = socket

    private fun getUserToken(): String = "Bearer ${Prefs.getString("TOKEN", "")}"
    fun getID(): String? = Prefs.getString("USER_ID", "")
    fun getToken(): String? = Prefs.getString("TOKEN", "")
}