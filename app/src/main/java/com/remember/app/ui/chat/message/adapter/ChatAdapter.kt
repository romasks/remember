package com.remember.app.ui.chat.message.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.remember.app.data.models.ChatMessages
import com.remember.app.ui.chat.ChatFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

const val OUTPUT = 1
const val INPUT = 2

class ChatAdapter(val fragment : ChatFragment, val id : String
                  //,val lastChatMessage : List<LastChatMessage>
     ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var messages = mutableListOf<ChatMessages.History>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == INPUT)
            return ChatInputHolder.create(
                parent
            )
        else {
            return ChatOutputHolder.create(
                parent
            )
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            INPUT -> {
                (holder as ChatInputHolder).onBind(messages[position])
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

    override fun getItemViewType(position: Int): Int {
        return when (messages[position].userId == id.toInt()) {
            false -> OUTPUT
            else -> INPUT
        }
    }
}