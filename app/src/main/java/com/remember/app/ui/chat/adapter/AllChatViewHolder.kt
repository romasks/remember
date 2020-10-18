package com.remember.app.ui.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.remember.app.R
import com.remember.app.data.Constants
import com.remember.app.data.models.ChatsModel
import com.remember.app.utils.KotlinUtils
import com.remember.app.utils.KotlinUtils.getCurrentTimezoneOffset
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_chat.view.*

class AllChatViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

    fun onBind(item: ChatsModel.Chat, onClick: (position: Int, model: ChatsModel.Chat) -> Unit) {
        with(itemView) {
            setOnClickListener { onClick(adapterPosition, item) }
            tvChatName.text = item.name
            if (item.lastMsg.text == null)
                tvLastMessage.text = "Сообщений нет"
            else
                tvLastMessage.text = item.lastMsg.text
            tvTime.text = KotlinUtils.parseTimeStampToTime(item.lastMessage)
            if (item.unreadCount > 0) {
                tvCountUnreadMessage.visibility = View.VISIBLE
                tvCountUnreadMessage.text = item.unreadCount.toString()
            } else tvCountUnreadMessage.visibility = View.GONE
            getCurrentTimezoneOffset()
            Glide.with(context!!).load(Constants.BASE_URL_FROM_PHOTO + item.picture).error(R.drawable.darth_vader).transform(CenterInside(), RoundedCorners(100)).into(imgProfile)
        }
    }

    companion object {
        fun create(parent: ViewGroup): AllChatViewHolder =
                AllChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false))
    }
}