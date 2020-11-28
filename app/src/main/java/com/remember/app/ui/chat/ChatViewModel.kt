package com.remember.app.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pixplicity.easyprefs.library.Prefs
import com.remember.app.data.dataFlow.DataManager
import com.remember.app.data.models.*
import io.socket.client.Socket


class ChatViewModel(private val dataManager: DataManager) :
        ViewModel() {

    private lateinit var socket: Socket
    var allChatModel: MutableLiveData<ChatsModel> = MutableLiveData()
    var successSendMessage: MutableLiveData<SuccessSendMessage> = MutableLiveData()
    var successReadMessage: MutableLiveData<SuccessReadMessage> = MutableLiveData()
    var successDeleteMessage: MutableLiveData<SuccessDeleteMessage> = MutableLiveData()
    var chatMessages: MutableLiveData<ChatMessages> = MutableLiveData()
    var chatInfo: MutableLiveData<ChatInfoResponse> = MutableLiveData()
    var chatUser: MutableLiveData<ChatUser> = MutableLiveData()
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

    fun sendMessage(chatID: String, message: String) {
        dataManager.sendMessage(id = chatID, token = getUserToken(), message = message, onSuccess = {
            it.let {
                successSendMessage.postValue(it)
            }
        }, onFailure = {
            error.postValue(true)
        })
    }

    fun getChatMessages(chatID: Int, limit: Int = 20, offset: Int = 0) {
        dataManager.getChatMessage(id = chatID, token = getUserToken(), limit = limit, offset = offset, onSuccess = {
            it.let {
                chatMessages.postValue(it)
            }
        }, onFailure = {
            it.message
        })
    }

    fun getChatInfo(chatID: Int) {
        dataManager.getChatInfo(id = chatID, token = getUserToken(), onSuccess = {
            it.let {
                chatInfo.postValue(it)
            }
        }, onFailure = {
            it.message
        })
    }


    fun getChatUser(chatID: Int) {
        dataManager.getChatUser(id = chatID, token = getUserToken(), onSuccess = {
            it.let {
                chatUser.postValue(it)
            }
        }, onFailure = {
            it.message
        })
    }

    fun changeStatusUnreadMessages(chatID: Int, messageID: Int) {
        dataManager.readMark(id = chatID, token = getUserToken(), messageID = messageID, onSuccess = {
            it.let {
                successReadMessage.postValue(it)
            }
        }, onFailure = {
            it.message
        })
    }

    fun deleteMessage(chatID: Int, messageID: Int, deleteAll: Boolean) {
        dataManager.deleteMessage(id = chatID, token = getUserToken(), messageID = messageID, deleteAll = deleteAll, onSuccess = {
            it.let {
                successDeleteMessage.postValue(it)
            }
        }, onFailure = {
            it.message
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