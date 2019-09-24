package com.serifenuruysal.themusicplayer.presentation.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.serifenuruysal.themusicplayer.R
import com.serifenuruysal.themusicplayer.data.models.ArtistModel
import com.serifenuruysal.themusicplayer.presentation.ui.adapter.SingerListAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_singer.view.*

/**
 * Created by S.Nur Uysal on 2019-09-21.
 */

class SingerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(model: ArtistModel?) {
        if (model != null) {
            itemView.tv_artist_name.text = model.user.username
            itemView.txt_track_count.text = model.songList.size.toString()
            Picasso.get().load(model.user.avatar_url).into(itemView.img_artist_banner)
            itemView.setOnClickListener(View.OnClickListener { listener.onItemClicked(model) })
        }
    }

    companion object {
        lateinit var listener: SingerListAdapter.ItemClickListener
        fun create(parent: ViewGroup, listener: SingerListAdapter.ItemClickListener): SingerViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_singer, parent, false)
            this.listener = listener
            return SingerViewHolder(view)
        }
    }
}