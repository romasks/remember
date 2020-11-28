package com.remember.app.ui.chat.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.remember.app.data.models.ChatsModel

class AllChatAdapter(val onClick: (position: Int, model: ChatsModel.Chat) -> Unit) :
    RecyclerView.Adapter<AllChatViewHolder>() {

    var chats = mutableListOf<ChatsModel.Chat>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllChatViewHolder =
        AllChatViewHolder.create(parent)

    override fun getItemCount(): Int = chats.size

    override fun onBindViewHolder(holder: AllChatViewHolder, position: Int) {
        holder.onBind(chats[position], onClick)
    }

    fun setList(data: List<ChatsModel.Chat>) {
        chats = data.toMutableList()
        notifyDataSetChanged()
    }

    fun addItem(modelAll: ChatsModel.Chat) {
        chats.add(modelAll)
        notifyDataSetChanged()
    }

}