package com.remember.app.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(
    itemView: View,
    private var currentPosition: Int
) : RecyclerView.ViewHolder(itemView) {

    open fun onBind(position: Int) {
        currentPosition = position
    }
}
