package com.example.mymp3service;

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import com.example.mymp3service.databinding.ActivityMainForBindServiceBinding

class MainActivityForBindService : AppCompatActivity() {
    lateinit var binding: ActivityMainForBindServiceBinding

    lateinit var songlist:Array<String>
    lateinit var song:String

    // progressbar 스레드를 운영하기 위한 변수
    var runThread=false
    var thread: MainActivityForBindService.ProgressThread?=null

    //
    lateinit var myBindeService:MyBindService
    // 서비스에 연결됬는지 여부를 나타냄
    var mBound = false

    var connection = object :ServiceConnection{
        // 서비스에 연결되면 호출되는 함수
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MyBindService.MyBinder
            myBindeService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainForBindServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        songlist= resources.getStringArray(R.array.songlist)
        song = songlist[0]

        binding.apply {
            listview.setOnItemClickListener { parent, view, position, id ->
                song = songlist[position]
                startPlay()
            }

            btnpaly.setOnClickListener {
                startPlay()
            }

            btnstop.setOnClickListener {
                stopPlay()
            }
        }

        val intent = Intent(this, MyBindService::class.java)
//        MainActivityForBindService 와MyBindService가 연결된 상태
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mBound){
            stopPlay()
            unbindService(connection)
        }
        mBound = false
    }

    private fun stopPlay() {
        if(mBound)
            myBindeService.stopPlay()
        runThread = false
        binding.progressBar.progress=0
    }

    private fun startPlay() {
        runThread=true
        if(thread==null || !thread!!.isAlive){
            if(mBound){

                // 연결된 Bindservice에 song 정보를 넘김
                myBindeService.startPlay(song)
                binding.progressBar.max = myBindeService.getMaxDuration()


                binding.progressBar.progress=0
                thread = ProgressThread()
                thread!!.start()
            }
        }else{
            // 기존에 플레이되고 있던 음악 stop
            myBindeService.stopPlay()

            myBindeService.startPlay(song)
            binding.progressBar.max = myBindeService.getMaxDuration()


            binding.progressBar.progress=0
        }
    }

    inner class ProgressThread:Thread(){
        override fun run() {
            while(runThread){
                binding.progressBar.incrementProgressBy(1000)
                SystemClock.sleep(1000)
                if(binding.progressBar.progress == binding.progressBar.max) {
                    runThread = false
                    binding.progressBar.progress=0
                }
            }
        }
    }

}