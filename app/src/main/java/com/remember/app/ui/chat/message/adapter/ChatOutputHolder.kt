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
import kotlinx.android.synthetic.main.item_output_message.view.*


class ChatOutputHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun onBind(item: ChatMessages.History) {
        with(itemView) {
            if (adapterPosition == 0) {
                (layoutParams as RecyclerView.LayoutParams).setMargins(0, 30, 0, 30)
                requestLayout()
            }
            tvMessage.text = item.content
            if (item.isRead) {
                messageStatusBottom.setImageDrawable(itemView.context.getDrawable(R.drawable.read))
                messageStatus.setImageDrawable(itemView.context.getDrawable(R.drawable.read))
            } else {
                messageStatusBottom.setImageDrawable(itemView.context.getDrawable(R.drawable.unread))
                messageStatus.setImageDrawable(itemView.context.getDrawable(R.drawable.unread))
            }
            if (item.content.length < 30) {
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
                    (layoutParams as ConstraintLayout.LayoutParams).setMargins(0, 0, 0, 65)
                    requestLayout()
                }
            }
        }
    }


    private fun showTimeLayout(itemView: View, item: ChatMessages.History) {
        with(itemView) {
            tvMessage.text = item.content
            if (item.content.length < 15) {
                itemView.containerLine.visibility = View.VISIBLE
                itemView.containerBottom.visibility = View.GONE
                tvTime.text = parseTimeStampToTime(item.date)
            } else {
                itemView.containerLine.visibility = View.GONE
                itemView.containerBottom.visibility = View.VISIBLE
                tvTimeBottom.text = parseTimeStampToTime(item.date)
            }
        }

    }

    companion object {
        fun create(parent: ViewGroup): ChatOutputHolder =
                ChatOutputHolder(
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.item_output_message, parent, false)
                )
    }
}