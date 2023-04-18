package com.example.mp3app

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mp3app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    var mediaPlayer:MediaPlayer?= null
    var vol = 0.5f
    var flag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    override fun onResume() {
        super.onResume()
        if(flag)
            mediaPlayer?.start()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
    }

    fun initLayout(){
        binding.apply {
            imageView.setVolumeListener(object :VolumeControlView.VolumeListener{
                override fun onChanged(angle: Float) {
                    Toast.makeText(this@MainActivity, angle.toString(), Toast.LENGTH_SHORT).show()
                    vol = if(angle>0){
                        angle/360
                    }else{
                        (360+angle)/360
                    }
                    mediaPlayer?.setVolume(vol, vol)
                }

            })
            playBtn.setOnClickListener {
                if(mediaPlayer == null){
                    mediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.song)
                    mediaPlayer?.setVolume(vol, vol)
                }
                mediaPlayer?.start() // pause한 지점 이후부터 자동으로 재생됨
                flag = true
            }
            pauseBtn.setOnClickListener {
                mediaPlayer?.pause()
                flag = false
            }
            stopBtn.setOnClickListener {
                mediaPlayer?.stop()
                mediaPlayer?.release() // 메모리 해제 필수
                mediaPlayer = null
                flag = false
            }

        }
    }

}