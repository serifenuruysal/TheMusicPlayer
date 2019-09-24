package com.serifenuruysal.themusicplayer.presentation.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.serifenuruysal.themusicplayer.R
import com.serifenuruysal.themusicplayer.data.models.Song
import com.serifenuruysal.themusicplayer.presentation.ui.adapter.SongListAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_singer.view.*

/**
 * Created by S.Nur Uysal on 2019-09-21.
 */

class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(song: Song?) {
        if (song != null) {
            itemView.tv_artist_name.text = song.title
            Picasso.get().load(song.artwork_url).into(itemView.img_artist_banner)
            itemView.setOnClickListener(View.OnClickListener { listener.onItemClicked(song) })
        }
    }

    companion object {
        lateinit var listener: SongListAdapter.ItemClickListener
        fun create(parent: ViewGroup, listener: SongListAdapter.ItemClickListener): SongViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_singer, parent, false)
            this.listener = listener
            return SongViewHolder(view)
        }
    }
}