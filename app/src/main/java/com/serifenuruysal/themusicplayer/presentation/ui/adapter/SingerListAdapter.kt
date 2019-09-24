package com.serifenuruysal.themusicplayer.presentation.ui.adapter

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.serifenuruysal.themusicplayer.data.models.ArtistModel
import com.serifenuruysal.themusicplayer.data.models.services.State
import com.serifenuruysal.themusicplayer.presentation.ui.ListFooterViewHolder
import com.serifenuruysal.themusicplayer.presentation.ui.SingerViewHolder

/**
 * Created by S.Nur Uysal on 2019-09-21.
 */

class SingerListAdapter(var listener: ItemClickListener) :
    PagedListAdapter<ArtistModel, RecyclerView.ViewHolder>(DiffCallback) {
    interface ItemClickListener {
        fun onItemClicked(user: ArtistModel)
    }

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) SingerViewHolder.create(
            parent,
            listener
        ) else ListFooterViewHolder.create(
            { Unit },
            parent
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as SingerViewHolder).bind(getItem(position))
        else (holder as ListFooterViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<ArtistModel>() {
            override fun areItemsTheSame(oldItem: ArtistModel, item: ArtistModel): Boolean {
                return oldItem.user.id == item.user.id
            }

            override fun areContentsTheSame(oldItem: ArtistModel, newItem: ArtistModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.LOADING || state == State.ERROR)
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }
}