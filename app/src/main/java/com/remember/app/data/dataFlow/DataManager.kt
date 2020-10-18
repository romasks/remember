package com.remember.app.data.dataFlow

import com.remember.app.data.dataFlow.network.Api
import com.remember.app.data.models.ChatMessages
import com.remember.app.data.models.SuccessSendMessage
import com.remember.app.data.models.ChatsModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.RequestBody

class DataManager(private val api: Api) : IDataManager {

    override fun getChatList(token: String, limit: Int, offset: Int, onSuccess: (response: ChatsModel) -> Unit, onFailure: (error: Throwable) -> Unit) {
        val request = api.getChatList(token, limit, offset).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onSuccess(it)
                }, { error ->
                    onFailure(error)
                })
    }

    override fun getChatMessage(token: String, id: Int, limit: Int, offset: Int, onSuccess: (response: ChatMessages) -> Unit, onFailure: (error: Throwable) -> Unit) {
        val request = api.getChatMessage(token, id, limit, offset).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onSuccess(it)
                }, { error ->
                    onFailure(error)
                })
    }

    override fun sendMessage(token: String, id: String, message: String, onSuccess: (response: SuccessSendMessage) -> Unit, onFailure: (error: Throwable) -> Unit) {
        val request = api.sendMessage(token, id, message).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onSuccess(it)
                }, { error ->
                    onFailure(error)
                })
    }

    override fun deleteChat(token: String, id: Int, onSuccess: (response: SuccessSendMessage) -> Unit, onFailure: (error: Throwable) -> Unit) {
        val request = api.deleteChat(token, id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onSuccess(it)
                }, { error ->
                    onFailure(error)
                })
    }

    override fun getChatInfo(token: String, id: Int, onSuccess: (response: SuccessSendMessage) -> Unit, onFailure: (error: Throwable) -> Unit) {
        val request = api.getChatInfo(token, id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onSuccess(it)
                }, { error ->
                    onFailure(error)
                })
    }

    override fun deleteMessage(token: String, id: Int, messageID: Int, deleteAll: Boolean, onSuccess: (response: SuccessSendMessage) -> Unit, onFailure: (error: Throwable) -> Unit) {
        val request = api.deleteMessage(token, id, messageID, deleteAll = false).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onSuccess(it)
                }, { error ->
                    onFailure(error)
                })
    }

    override fun editMessage(token: String, id: Int, messageID: Int, data: RequestBody, onSuccess: (response: SuccessSendMessage) -> Unit, onFailure: (error: Throwable) -> Unit) {
        val request = api.editMessage(token, id, messageID, data).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onSuccess(it)
                }, { error ->
                    onFailure(error)
                })
    }

    override fun readMark(token: String, id: Int, messageID: Int, onSuccess: (response: SuccessSendMessage) -> Unit, onFailure: (error: Throwable) -> Unit) {
        val request = api.readMark(token, id, messageID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onSuccess(it)
                }, { error ->
                    onFailure(error)
                })
    }
}