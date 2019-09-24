package com.serifenuruysal.themusicplayer.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Song(

    @SerializedName("id") val id: Int,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("duration") val duration: Int,
    @SerializedName("permalink") val permalink: String,
    @SerializedName("description") val description: String,
    @SerializedName("downloadable") val downloadable: Int,
    @SerializedName("genre") val genre: String,
    @SerializedName("genre_slush") val genre_slush: String,
    @SerializedName("title") val title: String,
    @SerializedName("uri") val uri: String,
    @SerializedName("permalink_url") val permalink_url: String,
    @SerializedName("artwork_url") val artwork_url: String,
    @SerializedName("background_url") val background_url: String,
    @SerializedName("waveform_data") val waveform_data: String,
    @SerializedName("waveform_url") val waveform_url: String,
    @SerializedName("user") val user: User,
    @SerializedName("stream_url") val stream_url: String,
    @SerializedName("download_url") val download_url: String,
    @SerializedName("playback_count") val playback_count: Int,
    @SerializedName("download_count") val download_count: Int,
    @SerializedName("favoritings_count") val favoritings_count: Int,
    @SerializedName("favorited") val favorited: Boolean,
    @SerializedName("comment_count") val comment_count: Int
) : Parcelable