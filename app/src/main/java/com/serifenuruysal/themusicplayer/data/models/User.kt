package com.serifenuruysal.themusicplayer.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(

    @SerializedName("id") val id: Int,
    @SerializedName("permalink") val permalink: String,
    @SerializedName("username") val username: String,
    @SerializedName("uri") val uri: String,
    @SerializedName("permalink_url") val permalink_url: String,
    @SerializedName("avatar_url") val avatar_url: String
) : Parcelable