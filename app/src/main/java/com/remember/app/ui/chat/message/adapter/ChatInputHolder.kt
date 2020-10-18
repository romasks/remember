package com.remember.app.ui.chat.message.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.remember.app.R
import com.remember.app.data.models.ChatMessages
import com.remember.app.utils.KotlinUtils.parseTimeStampToTime
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_input_message.view.*

class ChatInputHolder  (override val containerView : View): RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun onBind(item : ChatMessages.History){
        with(itemView){
            setOnClickListener {  }
            tvMessage.text = item.content
            tvTime.text = parseTimeStampToTime(item.date)
        }
    }

    companion object{
        fun create(parent: ViewGroup): ChatInputHolder =
            ChatInputHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_input_message, parent, false)
            )
    }
}