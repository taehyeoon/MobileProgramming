package com.example.radiogroupapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import com.example.radiogroupapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private var posX: Float = 0f
    private var num = 0
    private var imgIDlist = mutableListOf<Int>()
    private var buttonIDlist = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init(){
        imgIDlist.add(R.drawable.cat)
        imgIDlist.add(R.drawable.dog)
        imgIDlist.add(R.drawable.racoon)

        buttonIDlist.add(R.id.radioButton1)
        buttonIDlist.add(R.id.radioButton2)
        buttonIDlist.add(R.id.radioButton3)

        binding.radioGroup.setOnCheckedChangeListener{ _, checkID->
            when(checkID){
                R.id.radioButton1 -> binding.imageView.setImageResource(R.drawable.cat)
                R.id.radioButton2 -> binding.imageView.setImageResource(R.drawable.dog)
                R.id.radioButton3 -> binding.imageView.setImageResource(R.drawable.racoon)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (event != null) {
                    posX = event.rawX
                }
            }
            MotionEvent.ACTION_UP -> {
                val disX = posX - event.rawX
                if (disX > 0) {
                    Toast.makeText(this, "swipe left", Toast.LENGTH_SHORT).show()
                    if(num > 0){
                        num -= 1
                        binding.imageView.setImageResource(imgIDlist[num])
                        binding.radioGroup.check(buttonIDlist[num])
                    }
                } else if (disX < 0) {
                    Toast.makeText(this, "swipe right", Toast.LENGTH_SHORT).show()
                    if(num < 2){
                        num += 1
                        binding.imageView.setImageResource(imgIDlist[num])
                        binding.radioGroup.check(buttonIDlist[num])
                    }



                }
            }
        }
        return true
    }

}