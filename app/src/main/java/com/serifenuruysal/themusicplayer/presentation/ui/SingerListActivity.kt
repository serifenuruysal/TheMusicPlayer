package com.serifenuruysal.themusicplayer.presentation.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.serifenuruysal.themusicplayer.R
import com.serifenuruysal.themusicplayer.data.models.ArtistModel
import com.serifenuruysal.themusicplayer.data.models.Song
import com.serifenuruysal.themusicplayer.data.models.services.State
import com.serifenuruysal.themusicplayer.presentation.ui.adapter.SingerListAdapter
import com.serifenuruysal.themusicplayer.presentation.ui.adapter.SongListAdapter
import kotlinx.android.synthetic.main.activity_list.*


class SingerListActivity : AppCompatActivity() {

    private lateinit var viewModel: SingerListViewModel
    private lateinit var singerListAdapter: SingerListAdapter

    object Broadcast {
        val Broadcast_PLAY_NEW_AUDIO = "audioplayer.PlayNewAudio"
    }


    private lateinit var songListAdapter: SongListAdapter
    private lateinit var songList: ArrayList<Song>
    internal var serviceBound = false

    internal lateinit var player: MediaPlayerService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_home)
        toolbar.setTitle("Popular Artist")
        toolbar.tag = 0
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (toolbar.tag == 1) {
                    toolbar.tag = 0
                    initSingerAdapter()
                    toolbar.setTitle("Popular Artist")
                    toolbar.setNavigationIcon(R.drawable.ic_home)
                }

            }
        })

        viewModel = ViewModelProviders.of(this)
            .get(SingerListViewModel::class.java)
        initSingerAdapter()
        initState()
        bindAudio()
    }

    private fun setPopularSong() {
        toolbar.tag = 1
        initSongAdapter()
        toolbar.setTitle("Popular Songs")
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow)
    }

    private fun initSingerAdapter() {
        singerListAdapter = SingerListAdapter(object :
            SingerListAdapter.ItemClickListener {
            override fun onItemClicked(model: ArtistModel) {
                songList = model.songList
                setPopularSong()
                initSongAdapter()


            }
        })
        rvList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvList.adapter = singerListAdapter
        viewModel.userList.observe(this, Observer {

            singerListAdapter.submitList(it)
        })
    }

    private fun initState() {
        txt_error.setOnClickListener { viewModel.retry() }
        viewModel.getState().observe(this, Observer { state ->
            progress_bar.visibility = if (viewModel.listIsEmpty() && state == State.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (viewModel.listIsEmpty() && state == State.ERROR) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                singerListAdapter.setState(state ?: State.DONE)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)

    }

    //Binding this Client to the AudioPlayer Service
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as MediaPlayerService.LocalBinder
            player = binder.service
            serviceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            serviceBound = false
        }
    }

    private fun bindAudio() {
        //Check is service is active
        if (!serviceBound) {
            //Store Serializable audioList to SharedPreferences

            val playerIntent = Intent(this, MediaPlayerService::class.java)
            startService(playerIntent)
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }


    private fun initSongAdapter() {
        songListAdapter = SongListAdapter(object :
            SongListAdapter.ItemClickListener {
            override fun onItemClicked(song: Song) {

                //Service is active
                //Send a broadcast to the service -> PLAY_NEW_AUDIO
                val broadcastIntent = Intent(Broadcast.Broadcast_PLAY_NEW_AUDIO)
                broadcastIntent.putExtra("SONG", song)
                sendBroadcast(broadcastIntent)
            }
        }, songList)
        rvList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvList.adapter = songListAdapter

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putBoolean("serviceStatus", serviceBound)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        serviceBound = savedInstanceState.getBoolean("serviceStatus")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (serviceBound) {
            unbindService(serviceConnection)
            //service is active
            player.stopSelf()
        }
    }

}