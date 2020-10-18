package com.remember.app.ui.chat

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.remember.app.R
import com.remember.app.data.models.ChatsModel
import com.remember.app.ui.base.BaseFragmentMVVM
import com.remember.app.ui.chat.adapter.AllChatAdapter
import kotlinx.android.synthetic.main.fragment_main_chat.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MainChatFragment : BaseFragmentMVVM() {
    override val layoutId: Int = R.layout.fragment_main_chat
    lateinit var allChatAdapter: AllChatAdapter
    val chatViewModel: ChatViewModel by sharedViewModel()

    companion object {
        fun newInstance(bundle: Bundle) =
                MainChatFragment().apply {
                    arguments = bundle
                }

        val TAG = "MainChatFragment"
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    private fun onChatClick(position: Int, model: ChatsModel.Chat) {
        val bundle = Bundle()
        bundle.putParcelable("chat", model)
        bundle.putString("type", "afterList")
        openChieldFragment(ChatFragment.newInstance(bundle), R.id.allChats)
    }
}