package com.example.runningrecord

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.runningrecord.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        val data : ArrayList<RunData> = ArrayList()
        val coefficantCalories = 4
//        칼로리 = 4* 달린 거리
    }
    lateinit var binding: ActivityMainBinding
//    lateinit var adapter: RunDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initLayout()
    }

    private fun initData() {
        val scan = Scanner(resources.openRawResource(R.raw.runningdata))
        while(scan.hasNextLine()){
            val date = scan.nextLine()
            val startTime = scan.nextLine()
            val endTime = scan.nextLine()
            val place = scan.nextLine()
            val distance = scan.nextLine().toFloat()
            val level = scan.nextLine().toInt()
            val weather = scan.nextLine().toInt()
            data.add(RunData(date, startTime, endTime, place,distance,level,weather))
        }
    }

    private fun initLayout() {
        binding.apply {
            startButton.setOnClickListener {
                val intent = Intent(this@MainActivity, AddActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }




    }
}