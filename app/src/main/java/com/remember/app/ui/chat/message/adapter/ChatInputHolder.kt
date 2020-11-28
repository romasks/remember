package com.remember.app.ui.chat.message.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.remember.app.R
import com.remember.app.data.models.ChatMessages
import com.remember.app.utils.KotlinUtils.parseTimeStampToTime
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_input_message.view.*

class ChatInputHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun onBind(item: ChatMessages.History, onLongClick: (position: Int, model: ChatMessages.History) -> Unit) {
        with(itemView) {
            tvMessage.text = item.content
            if (item.isSelect)
                messageContainer.background = itemView.context.getDrawable(R.drawable.select_chat_user_bg)
            else
                messageContainer.background = itemView.context.getDrawable(R.drawable.chat_user_bg)
            itemView.setOnLongClickListener {
                messageContainer.background = itemView.context.getDrawable(R.drawable.select_chat_user_bg)
                item.isSelect = true
                onLongClick(adapterPosition, item)
                false
            }
            if (item.isRead) {
                messageStatusBottom.setImageDrawable(itemView.context.getDrawable(R.drawable.read))
                messageStatus.setImageDrawable(itemView.context.getDrawable(R.drawable.read))
            } else {
                messageStatusBottom.setImageDrawable(itemView.context.getDrawable(R.drawable.unread))
                messageStatus.setImageDrawable(itemView.context.getDrawable(R.drawable.unread))
            }
            if (item.content.length < 30) {
                if (adapterPosition == 0) {
                    (layoutParams as RecyclerView.LayoutParams).setMargins(0, 30, 30, 30)
                    requestLayout()
                }
                containerLine.visibility = View.VISIBLE
                containerBottom.visibility = View.GONE
                //containerBottom.maxWidth = 10
                // containerLine.maxWidth = LinearLayout.LayoutParams.WRAP_CONTENT
                tvTime.text = parseTimeStampToTime(item.date)
            } else {
                containerLine.visibility = View.GONE
                containerBottom.visibility = View.VISIBLE
                // containerLine.maxWidth = 10
                // containerBottom.maxWidth = LinearLayout.LayoutParams.WRAP_CONTENT
                tvTimeBottom.text = parseTimeStampToTime(item.date)
                with(tvMessage) {
                    (layoutParams as ConstraintLayout.LayoutParams).setMargins(0, 0, 30, 65)
                    requestLayout()
                }
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): ChatInputHolder =
                ChatInputHolder(
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.item_input_message, parent, false)
                )
    }
}