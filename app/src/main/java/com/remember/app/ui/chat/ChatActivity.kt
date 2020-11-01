package com.remember.app.ui.chat

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ambitt.utils.replaceFragmentSafely
import com.remember.app.BuildConfig
import com.remember.app.R
import com.remember.app.data.models.MemoryPageModel
import com.remember.app.socket.ChatMessage
import com.remember.app.socket.SocketClient
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatActivity : AppCompatActivity(), SocketClient.ISignalingEvents {

    private var socketClient: SocketClient? = null
    private val viewModel: ChatViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        if (savedInstanceState == null) {
            val type = intent.extras!!.getString("type", "")
            val model = intent.extras!!.getParcelable<MemoryPageModel>("model")
            openFragmentByType(type, model)
        }
        initSocketConnection()
    }

    private fun initSocketConnection() {
        socketClient = SocketClient(BuildConfig.SCOKET_URL, viewModel.getToken()!!, this) //TODO
        socketClient!!.connect()
        viewModel.instantiateSocket(socketClient!!.getSocket())
    }

    private fun openFragmentByType(type: String, model: MemoryPageModel?) {
        val bundle = Bundle()
        when (type) {
            "profile" -> {
                bundle.putParcelable("model", model)
                replaceFragmentSafely(MaintainerPageFragment.newInstance(bundle), "MaintainerPageFragment", false, true, R.id.container)
            }
            "chat" -> {
                bundle.putParcelable("model", model)
                replaceFragmentSafely(ChatFragment.newInstance(bundle), "ChatFragment", false, true, R.id.container)
            }
            "allchat" -> {
                bundle.putParcelable("model", model)
                replaceFragmentSafely(MainChatFragment.newInstance(bundle), "MainChatFragment", false, true, R.id.container)
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount <= 1)
            finish()
        else
            supportFragmentManager.popBackStack()
        // overridePendingTransition(R.anim.left, R.anim.right)
        //overridePendingTransition(R.anim.right, R.anim.left)
    }

    override fun onConnect(token: String) {
        Log.d("TAG", "CONNECTED")
        socketClient?.authorization(token)
    }

    override fun onDisconnect(error: Array<Any>) {
        Log.d("TAG", error.size.toString())
    }

    override fun onMessage(message: ChatMessage) {
        // viewModel.addMessage(message)
        Log.d("TAG", "onMessage")
    }

    override fun onDestroy() {
        socketClient?.disconnect()
        socketClient?.getSocket()?.off("auth")
        socketClient?.getSocket()?.off("err")
        socketClient?.getSocket()?.off("chat.new")
        socketClient?.getSocket()?.off("chat.read")
        socketClient?.getSocket()?.off("chat.remove")
        socketClient?.getSocket()?.off("chat.edit")
        super.onDestroy()
    }
}