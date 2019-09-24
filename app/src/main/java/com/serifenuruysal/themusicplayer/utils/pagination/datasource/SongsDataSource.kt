package com.serifenuruysal.themusicplayer.utils.pagination

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import com.serifenuruysal.themusicplayer.data.models.ArtistModel
import com.serifenuruysal.themusicplayer.data.models.Song
import com.serifenuruysal.themusicplayer.data.models.User
import com.serifenuruysal.themusicplayer.data.models.services.NetworkService
import com.serifenuruysal.themusicplayer.data.models.services.State
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

/**
 * Created by S.Nur Uysal on 2019-09-21.
 */

class SongsDataSource(
    private val networkService: NetworkService,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, ArtistModel>() {
    var songMap: HashMap<User, ArrayList<Song>> = hashMapOf()

    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null


    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, ArtistModel>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            networkService.getSongs("popular", 0, 20)
                .subscribe(
                    { response ->
                        updateState(State.DONE)

                        callback.onResult(
                            mapToArtistModel(response),
                            null,
                            1
                        )
                    },
                    {
                        updateState(State.ERROR)
                        setRetry(Action { loadInitial(params, callback) })
                    }
                )
        )
    }


    private fun mapToArtistModel(response: List<Song>?): MutableList<ArtistModel> {
        response!!.forEach { song: Song ->
            if (songMap.containsKey(song.user)) {
                val songList = ArrayList(songMap.get(song.user))
                songList.add(song)
                songMap.put(song.user, songList)

            } else {
                val songList: ArrayList<Song> = arrayListOf<Song>(song)
                songMap.put(song.user, songList)
            }

        }
        val artistModelList: MutableList<ArtistModel> = mutableListOf()
        songMap.forEach { user, songList -> artistModelList.add(ArtistModel(user = user, songList = songList)) }

        return artistModelList
    }


    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ArtistModel>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            networkService.getSongs("popular", params.key, params.requestedLoadSize)
                .subscribe(
                    { response ->
                        updateState(State.DONE)
                        mapToArtistModel(response)
                        callback.onResult(
                            mapToArtistModel(response),
                            params.key + 1
                        )
                    },
                    {
                        updateState(State.ERROR)
                        setRetry(Action { loadAfter(params, callback) })
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ArtistModel>) {
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(
                retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

}