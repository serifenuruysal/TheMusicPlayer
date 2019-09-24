package com.serifenuruysal.themusicplayer.utils.pagination.factory

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.serifenuruysal.themusicplayer.data.models.ArtistModel
import com.serifenuruysal.themusicplayer.data.models.Song
import com.serifenuruysal.themusicplayer.data.models.services.NetworkService
import com.serifenuruysal.themusicplayer.utils.pagination.SongsDataSource
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by S.Nur Uysal on 2019-09-21.
 */

class SongsDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val networkService: NetworkService
) : DataSource.Factory<Int, ArtistModel>() {

    val dataSourceLiveData = MutableLiveData<SongsDataSource>()

    override fun create(): DataSource<Int, ArtistModel> {
        val dataSource = SongsDataSource(networkService, compositeDisposable)
        dataSourceLiveData.postValue(dataSource)
        return dataSource
    }
}