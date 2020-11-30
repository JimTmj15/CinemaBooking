package com.fave.cinemabooking.ui.view_holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class GenericViewHolder<M>(view: ViewGroup, viewId: Int):
    RecyclerView.ViewHolder(
        LayoutInflater.from(view.context)
            .inflate(viewId, view, false)) {

    abstract fun bindViewHolder(data: M)
}
