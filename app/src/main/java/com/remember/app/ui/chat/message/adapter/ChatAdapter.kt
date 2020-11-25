package com.remember.app.ui.chat.message.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.remember.app.data.models.ChatMessages
import java.lang.Integer.parseInt

const val OUTPUT = 1
const val INPUT = 2

class ChatAdapter(private val currentUserId: String, private val onLongClick: (position: Int, model: ChatMessages.History) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var messages = mutableListOf<ChatMessages.History>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == INPUT)
            return ChatInputHolder.create(parent)
        else {
            return ChatOutputHolder.create(parent)
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            INPUT -> {
                (holder as ChatInputHolder).onBind(messages[position], onLongClick)
            }
            OUTPUT -> {
                (holder as ChatOutputHolder).onBind(messages[position])
            }
        }
    }

    fun setList(data: List<ChatMessages.History>) {
        messages = data.toMutableList()
        notifyDataSetChanged()
    }

    fun addItems(data: ChatMessages.History) {
        messages.add(data)
        notifyDataSetChanged()
    }

    fun updateItem(data: ChatMessages.History) {
        messages.find { it.id == data.id }?.content = data.content
        notifyItemChanged(messages.indexOf((messages.find { it.id == data.id })))
    }

    fun updateItemById(messageId: Int) {
        messages.find { it.id == messageId }?.isRead = true
        for (i in messages.indices) {
            if (messages[i].userId != parseInt(currentUserId)) {
                messages[i].isRead == true
            }
        }
        notifyItemRangeChanged(0 , messages.size)
    }

    fun removeItem(data: ChatMessages.History) {
        messages.remove(data)
        notifyItemRemoved(messages.indexOf((messages.find { it.id == data.id })))
    }

    fun removeItemByPosition(position: Int) {
        messages.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeItemById(id: Int) {
        val position = getPositionDeleteItem(id)
        if (position >= 0) {
            messages.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    private fun getPositionDeleteItem(id: Int): Int {
        var position = -1
        for (i in messages.indices) {
            if (messages[i].id == id)
                position = i
            else
                position = -1
        }
        return position
    }

    fun getItems() = messages

    override fun getItemViewType(position: Int): Int {
        return when (messages[position].userId == currentUserId.toInt()) {
            false -> OUTPUT
            else -> INPUT
        }
    }
}