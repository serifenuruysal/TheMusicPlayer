package com.serifenuruysal.themusicplayer.presentation.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.serifenuruysal.themusicplayer.data.models.ArtistModel
import com.serifenuruysal.themusicplayer.data.models.services.NetworkService
import com.serifenuruysal.themusicplayer.data.models.services.State
import com.serifenuruysal.themusicplayer.utils.pagination.SongsDataSource
import com.serifenuruysal.themusicplayer.utils.pagination.factory.SongsDataSourceFactory
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by S.Nur Uysal on 2019-09-21.
 */

class SingerListViewModel : ViewModel() {

    private val networkService = NetworkService.getService()
    var userList: LiveData<PagedList<ArtistModel>>
    private val compositeDisposable = CompositeDisposable()
    private val pageSize = 5
    private val dataSourceFactory: SongsDataSourceFactory

    init {
        dataSourceFactory = SongsDataSourceFactory(compositeDisposable, networkService)
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        userList = LivePagedListBuilder(dataSourceFactory, config).build()
    }


    fun getState(): LiveData<State> = Transformations.switchMap<SongsDataSource,
            State>(dataSourceFactory.dataSourceLiveData, SongsDataSource::state)

    fun retry() {
        dataSourceFactory.dataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return userList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}