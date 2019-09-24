package com.serifenuruysal.themusicplayer.data.models.services

import com.serifenuruysal.themusicplayer.data.models.Song
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by S.Nur Uysal on 2019-09-21.
 */

interface NetworkService {


    @GET("feed/")
    fun getSongs(
        @Query("type") type: String,
        @Query("page") page: Int,
        @Query("count") count: Int
    ): Single<List<Song>>

    companion object {
        fun getService(): NetworkService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api-v2.hearthis.at/feed/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(NetworkService::class.java)
        }
    }

}