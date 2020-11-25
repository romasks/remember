package com.remember.app.ui.chat

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.remember.app.R
import com.remember.app.data.models.ChatsModel
import com.remember.app.data.models.MemoryPageModel
import com.remember.app.ui.base.BaseFragmentMVVM
import com.remember.app.ui.chat.adapter.AllChatAdapter
import kotlinx.android.synthetic.main.fragment_main_chat.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MainChatFragment : BaseFragmentMVVM() {
    override val layoutId: Int = R.layout.fragment_main_chat
    lateinit var allChatAdapter: AllChatAdapter
    val chatViewModel: ChatViewModel by sharedViewModel()
    var model: MemoryPageModel? = null
    var type = ""

    companion object {
        fun newInstance(bundle: Bundle) =
                MainChatFragment().apply {
                    arguments = bundle
                }
        val TAG = "MainChatFragment"
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        type = arguments?.getString("type", "")!!
        if (type == "")
            model = arguments!!.getParcelable("model")!!
        initUI()

        initLiveData()
    }

    private fun initUI() {
        initAdapter()
        btnBack.setOnClickListener {
            (activity as ChatActivity).onBackPressed()
        }
    }

    private fun initLiveData() {
        chatViewModel.getChats()
        chatViewModel.allChatModel.observe(viewLifecycleOwner, Observer {
            it?.let {
                allChatAdapter.setList(it.chats)
                if (it.chats.isNotEmpty()) {
                    containerEmpty.visibility = View.GONE
                    rvChat.visibility = View.VISIBLE
                } else {
                    containerEmpty.visibility = View.VISIBLE
                    rvChat.visibility = View.GONE
                }
            }
        })
    }

    private fun initAdapter() {
        allChatAdapter =
                AllChatAdapter(onClick = { position, model -> onChatClick(position, model) })
        rvChat.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = allChatAdapter
        }
    }

    private fun onChatClick(position: Int, chatModel: ChatsModel.Chat) {
        val bundle = Bundle()
        bundle.putParcelable("chat", chatModel)
        if (model != null)
            bundle.putParcelable("model", model)
        if (type == "menu")
            bundle.putString("type", "afterMenu")
        else
            bundle.putString("type", "afterList")
        openChieldFragment(ChatFragment.newInstance(bundle), R.id.allChats)
    }
}