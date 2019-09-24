package com.serifenuruysal.themusicplayer.presentation.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.serifenuruysal.themusicplayer.R
import com.serifenuruysal.themusicplayer.data.models.Song
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_singer.view.*
import kotlinx.android.synthetic.main.item_song.view.*

/**
 * Created by S.Nur Uysal on 2019-09-21.
 */

class SongListAdapter(private var listener: SongListAdapter.ItemClickListener, private val myDataset: List<Song>) :
    RecyclerView.Adapter<SongListAdapter.SongViewHolder>() {

    interface ItemClickListener {
        fun onItemClicked(song: Song)
    }

    class SongViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SongListAdapter.SongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        var song = myDataset.get(position)
        holder.view.tv_song_name.text = song.title
        holder.view.tv_song_description.text = song.description
        holder.view.tv_song_duration.text = song.duration.toString()
        holder.view.tv_song_genre.text = song.genre+"/"+song.genre_slush
        holder.view.tv_song_relasedate.text = song.created_at
        Picasso.get().load(song.artwork_url).into( holder.view.img_artwork)
        holder.view.setOnClickListener({ listener.onItemClicked(song) })
    }

    override fun getItemCount() = myDataset.size
}