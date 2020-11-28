package com.remember.app.ui.chat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ambitt.utils.replaceFragmentSafely
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.remember.app.R
import com.remember.app.Remember.Companion.active
import com.remember.app.data.Constants
import com.remember.app.data.models.*
import com.remember.app.ui.base.BaseFragmentMVVM
import com.remember.app.ui.chat.dialogs.DeleteMessageDialog
import com.remember.app.ui.chat.message.adapter.ChatAdapter
import com.remember.app.utils.ImageUtils.setGlideImageWithError
import com.remember.app.utils.KotlinUtils.getLocaleTime
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.fragment_chat.*
import org.json.JSONException
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.lang.Integer.parseInt

class ChatFragment : BaseFragmentMVVM() {

    override val layoutId: Int = R.layout.fragment_chat
    val chatViewModel: ChatViewModel by sharedViewModel()
    lateinit var chatAdapter: ChatAdapter
    var model: MemoryPageModel? = null
    var chatModel: ChatsModel.Chat? = null
    var type = ""
    var lastSendingMessage: ChatMessages.History? = null
    var lastSendingMessageId = 0
    var deleteMessagePosition = -1
    var visaviID = "0"
    var chatUser: ChatUser? = null

    companion object {
        fun newInstance(bundle: Bundle) =
                ChatFragment().apply {
                    arguments = bundle
                }
        val TAG = "ChatFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        type = arguments!!.getString("type", "")!!
        when (type) {
            "afterList" -> {
                chatModel = arguments!!.getParcelable<ChatsModel.Chat>("chat")!!
           //     model = arguments!!.getParcelable<MemoryPageModel>("model")
            }
            "profile" -> {
                visaviID = arguments!!.getString("visaviID", "")!!
            }
            "afterMenu" -> {
                chatModel = arguments!!.getParcelable<ChatsModel.Chat>("chat")!!
                model = arguments!!.getParcelable<MemoryPageModel>("model")
            }
            "push" -> {
                visaviID = arguments!!.getString("visaviID", "")!!
            }
            else -> {
                model = arguments!!.getParcelable<MemoryPageModel>("model")
            }
        }
        initUI()
        initLiveData()
        initSocketListener()
    }

    private fun initLiveData() {
        getChatMessages()
        chatViewModel.successReadMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                chatAdapter.updateItemById(it.last_message_id)
            }
        })
        chatViewModel.chatMessages.observe(viewLifecycleOwner, Observer {
            it?.let {
                chatAdapter.setList(it.history.reversed())
                scrollToBottom()
                when (type) {
                    "afterList" -> {
                        chatViewModel.changeStatusUnreadMessages(chatModel?.id!!, it.history.first().id)
                    }
                    "profile" -> {
                        chatViewModel.changeStatusUnreadMessages(parseInt(visaviID), it.history.first().id)
                    }
                    "push" -> {
                        chatViewModel.changeStatusUnreadMessages(parseInt(visaviID), it.history.first().id)
                    }
                    else -> {
                        chatViewModel.changeStatusUnreadMessages(chatModel?.id!!, it.history.first().id)
                    }
                }
            }
        })
        chatViewModel.error.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    Toast.makeText(context, "Ошибка отправки сообщения, попробуйте снова", Toast.LENGTH_SHORT).show()
                    chatViewModel.error.postValue(null)
                    imgSend.isEnabled = true
                }
            }
        })
        chatViewModel.successSendMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                imgSend.isEnabled = true
                lastSendingMessage = ChatMessages.History(content = etComment.text.toString(), id = it.id, userId = chatViewModel.getID()?.toInt()!!, date = getLocaleTime())
                lastSendingMessage?.let {
                    addMessage(it)
                    lastSendingMessage = null
                }
                chatViewModel.successSendMessage.postValue(null)
            }
        })
        chatViewModel.successDeleteMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (deleteMessagePosition > -1) {
                    chatAdapter.removeItemByPosition(deleteMessagePosition)
                    chatViewModel.successDeleteMessage.postValue(null)
                    deleteMessagePosition = -1
                    containerAction.visibility = View.GONE
                }
            }
        })
        getChatInfo()
        chatViewModel.chatUser.observe(viewLifecycleOwner, Observer {
            it?.let {
                initChatInfo(it)
                chatUser = it
            }
        })
    }

    private fun getChatInfo() {
        when (type) {
            "afterList" -> {
                chatViewModel.getChatUser(chatModel?.id!!)
            }
            "profile" -> {
                chatViewModel.getChatUser(parseInt(visaviID))
            }
            "push" -> {
                chatViewModel.getChatUser(parseInt(visaviID))
            }
            else -> {
                chatViewModel.getChatUser(chatModel?.id!!)
            }
        }
    }

    private fun getChatMessages() {
        when {
            type == "" -> model?.userId?.toInt()?.let { chatViewModel.getChatMessages(it) }
            type == "profile" -> chatViewModel.getChatMessages(parseInt(visaviID))
            visaviID != "0" -> {
                chatViewModel.getChatMessages(parseInt(visaviID))
            }
            else -> chatViewModel.getChatMessages(chatModel?.id!!.toInt())
        }
    }

    private fun initSocketListener() {
        chatViewModel.getSocketConnection().on("err", onError)
        chatViewModel.getSocketConnection().on("chat.new", onNewMessage)
        chatViewModel.getSocketConnection().on("auth", onAuth)
        chatViewModel.getSocketConnection().on("chat.edit", onEditMessage)
        chatViewModel.getSocketConnection().on("chat.remove", onRemoveMessage)
        chatViewModel.getSocketConnection().on("chat.read", onReadMessage)
    }

    private fun scrollToBottom() {
        rvChat.scrollToPosition(chatAdapter.itemCount - 1)
    }

    private fun initUI() {
        Glide.with(context!!).load(R.drawable.darth_vader).transform(CenterInside(), RoundedCorners(70)).into(imgProfile)
        initAdapter()
        btnBack.setOnClickListener {
            (activity as ChatActivity).onBackPressed()
        }
        etComment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
            ) {
                if (s.isNotEmpty())
                    imgSend.visibility = View.VISIBLE
                else
                    imgSend.visibility = View.GONE
            }
        })
        imgSend.setOnClickListener {
            imgSend.isEnabled = false
            when {
                type == "" -> model?.userId?.toInt()?.let {
                    lastSendingMessage = model?.userId?.let { it1 -> parseInt(it1) }?.let { it2 -> ChatMessages.History(content = etComment.text.toString(), id = lastSendingMessageId, userId = it2, date = getLocaleTime()) }
                    chatViewModel.sendMessage(model?.userId.toString(), etComment.text.toString())
                }
                type == "profile" -> {
                    lastSendingMessage = ChatMessages.History(content = etComment.text.toString(), id = lastSendingMessageId, userId = parseInt(visaviID), date = getLocaleTime())
                    chatViewModel.sendMessage(model?.userId.toString(), etComment.text.toString())
                }
                visaviID != "0" -> {
                    chatViewModel.sendMessage(visaviID, etComment.text.toString())
                    lastSendingMessage = ChatMessages.History(content = etComment.text.toString(), id = lastSendingMessageId, userId = parseInt(visaviID), date = getLocaleTime())
                }
                else -> {
                    chatViewModel.sendMessage(chatModel?.id.toString(), etComment.text.toString())
                    lastSendingMessage = ChatMessages.History(content = etComment.text.toString(), id = lastSendingMessageId, userId = chatModel?.id!!, date = getLocaleTime())
                }
            }
        }
        imgProfile.setOnClickListener {
            openProfile()
        }
    }

    private fun initChatInfo(chatInfo: ChatUser) {
        if (chatInfo.picture != null && !chatInfo.picture.contains("http"))
            setGlideImageWithError(context, Constants.BASE_URL_FROM_PHOTO + chatInfo.picture, imgProfile)
        else
            setGlideImageWithError(context, chatInfo.picture, imgProfile)
        toolbarTitle.text = chatInfo.name
    }

    private fun openProfile() {
        val bundle = Bundle()
        chatUser?.let {
            bundle.putParcelable("user", chatUser)
        }
        bundle.putString("type", "user")
        bundle.putString("visaviID", chatUser?.id.toString())
        replaceFragmentSafely(MaintainerPageFragment.newInstance(bundle), "MaintainerPageFragment", false, true, R.id.container)
    }

    private fun initAdapter() {
        chatAdapter = ChatAdapter(chatViewModel.getID()!!, onLongClick = { position, model -> showActionLayout(position, model) })
        rvChat.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = chatAdapter
        }
//        rvChat.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val s =
//                        (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
//                if (s == 0 && dy < 0) {
//                    Log.d("TAG", s.toString())
//                    getPreviousMessage()
//                    // Scrolling up
//                } else {
//                    // Scrolling down
//                }
//            }
//        })
    }

    private fun addMessage(message: ChatMessages.History) {
        chatAdapter.addItems(message)
        etComment.text?.clear()
        chatViewModel.successSendMessage.postValue(null)
        scrollToBottom()
    }

    private fun showActionLayout(position: Int, model: ChatMessages.History) {
        containerAction.visibility = View.VISIBLE
        imgDelete.setOnClickListener {
            showDeleteDialog(position, model)
        }
        tvDeleteMsg.setOnClickListener {
            showDeleteDialog(position, model)
        }
    }

    private fun showDeleteDialog(position: Int, model: ChatMessages.History) {
        val dialog = DeleteMessageDialog()
        dialog.listener = object : DeleteMessageDialog.DeleteMessageListener {
            override fun deleteForAll() {
                deleteMessagePosition = position
                chatViewModel.deleteMessage(chatModel?.id!!, model.id, true)
            }
            override fun deleteForMe() {
                deleteMessagePosition = position
                chatViewModel.deleteMessage(chatModel?.id!!, model.id, false)
            }
        }
        val bundle = Bundle()
        bundle.putString("type", type)
        dialog.arguments = bundle
        dialog.show(activity!!.supportFragmentManager, "askRegDialog")
    }

    private val onNewMessage = Emitter.Listener { args ->
        activity!!.runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            try {
                val json = data.toString()
                val modelType = object : TypeToken<NewMessage>() {}.type
                val response = Gson().fromJson<NewMessage>(json, modelType)
                val message = ChatMessages.History(content = response.message.text, id = response.message.id.toInt(), userId = response.from.id, date = response.message.date)
                addMessage(message)
                when (type) {
                    "afterList" -> {
                        chatViewModel.changeStatusUnreadMessages(chatModel?.id!!, message.id)
                    }
                    "profile" -> {
                        chatViewModel.changeStatusUnreadMessages(parseInt(visaviID), message.id)
                    }
                    "push" -> {
                        chatViewModel.changeStatusUnreadMessages(parseInt(visaviID), message.id)
                    }
                    else -> {
                        chatViewModel.changeStatusUnreadMessages(chatModel?.id!!, message.id)
                    }
                }
            } catch (e: JSONException) {
                Log.e("TAG", e.message)
                return@Runnable
            }
        })
    }

    var onError = Emitter.Listener {
        val data = it[0] as JSONObject
        val name = data.toString()
        chatViewModel.getSocketConnection().emit("err")

    }
    var onAuth = Emitter.Listener {
        val data = it[0] as JSONObject
        val name = data.toString()
        chatViewModel.getSocketConnection().emit("auth")
    }

    private val onEditMessage = Emitter.Listener { args ->
        activity!!.runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            try {
                val json = data.toString()
                val type = object : TypeToken<NewMessage>() {}.type
                val response = Gson().fromJson<NewMessage>(json, type)
                val message = ChatMessages.History(content = response.message.text, id = response.message.id.toInt(), userId = response.from.id, date = response.message.date)
                chatAdapter.updateItem(message)
            } catch (e: JSONException) {
                Log.e("TAG", e.message)
                return@Runnable
            }
        })
    }

    private val onReadMessage = Emitter.Listener { args ->
        activity!!.runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            try {
                val json = data.toString()
                val type = object : TypeToken<NewMessage>() {}.type
                val response = Gson().fromJson<NewMessage>(json, type)
                chatAdapter.updateItemById(response.message.id)
            } catch (e: JSONException) {
                Log.e("TAG", e.message)
                return@Runnable
            }
        })
    }

    private val onRemoveMessage = Emitter.Listener { args ->
        activity!!.runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            try {
                val json = data.toString()
                val type = object : TypeToken<NewMessage>() {}.type
                val response = Gson().fromJson<NewMessage>(json, type)
                //  chatAdapter.removeItemById(response.message.id)
                val message = ChatMessages.History(content = response.message.text, id = response.message.id.toInt(), userId = response.from.id, date = response.message.date)
                chatAdapter.removeItem(message)
            } catch (e: JSONException) {
                Log.e("TAG", e.message)
                return@Runnable
            }
        })
    }

    override fun onStart() {
        super.onStart()
        active = true
    }

    override fun onStop() {
        super.onStop()
        active = false
    }
}