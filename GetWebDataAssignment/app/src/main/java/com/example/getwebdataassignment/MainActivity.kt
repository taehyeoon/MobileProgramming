package com.example.getwebdataassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.getwebdataassignment.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    val movieurl = "http://www.cgv.co.kr/movies/?lt=1&ft=0"
    val scope = CoroutineScope(Dispatchers.IO)

    var titlesText:String = ""
    var openingsText:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLayout()
    }

    private fun initLayout() {
        getMovies()
        getOpening()
    }

    private fun getOpening() {
        scope.launch {
            val doc = Jsoup.connect(movieurl).get()
            val openings = doc.select("span.txt-info")

            for(opening in openings){
                openingsText += opening.select("strong").text().toString() + "\n"
            }

            withContext(Dispatchers.Main){
                binding.singers.text = openingsText
            }
        }
    }

    private fun getMovies() {
        scope.launch {
            val doc = Jsoup.connect(movieurl).get()
            val moviesTitles = doc.select("div.box-contents>a")

            for(title in moviesTitles){
                titlesText += title.text().toString() + "\n"
            }

            withContext(Dispatchers.Main){
                binding.titles.text = titlesText
            }
        }
    }

}