package com.example.getwebdataassignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.getwebdataassignment.databinding.ActivityMainBinding
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import org.jsoup.Jsoup

class MainActivity : AppCompatActivity() {

    var movieData: ArrayList<MovieData> = ArrayList()
    lateinit var binding: ActivityMainBinding
    val myViewModel: MyViewModel by viewModels()

    val movieListFragment = MovieListFragment()
    val posterFragment = PosterFragment()

    lateinit var adapter: MyAdapter


    val movieurl = "http://www.cgv.co.kr/movies/?lt=1&ft=0"
    val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLayout()
    }

    private fun initLayout() {
        scope.launch {
            val doc = Jsoup.connect(movieurl).get()
            val openings = doc.select("span.txt-info>strong")
            val titles = doc.select("div.box-contents>a")

            for (i in 0..openings.size - 1) {
                val item = MovieData(titles[i].text(), openings[i].text(), "")
                Log.d("dataasdf", titles[i].text() + " " + openings[i].text())

                withContext(Dispatchers.Main){
                    movieData.add(item)
                    adapter.notifyDataSetChanged()
                }

            }
        }
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        adapter = MyAdapter(movieData)
        Log.d("data Num", movieData.size.toString())
        adapter.itemClickListener = object : MyAdapter.OnItemClickListener {
            override fun OnItemClick(data: MovieData, pos: Int) {
                Toast.makeText(this@MainActivity, "clicked", Toast.LENGTH_SHORT).show()
            }
        }

        binding.recyclerView.adapter = adapter
    }

}
//        binding.chartBtn.setOnClickListener {
//            val fragment = supportFragmentManager.beginTransaction()
//            fragment.replace(R.id.frameLayout, movieListFragment)
//            fragment.commit()
//        }

//        getMovies()
//        getOpening()
//        val imageView = binding.imageView
//        val imageUrl = "https://img.cgv.co.kr/Movie/Thumbnail/Poster/000086/86883/86883_320.jpg"

//        Glide.with(this)
//            .load(imageUrl)
//            .into(imageView)


//    private fun getOpening() {
//        scope.launch {
//            val doc = Jsoup.connect(movieurl).get()
//            val openings = doc.select("span.txt-info")
//
//            for(opening in openings){
//                openingsText += opening.select("strong").text().toString() + "\n"
//            }
//
//            withContext(Dispatchers.Main){
//                binding.singers.text = openingsText
//            }
//        }
//    }

//    private fun getMovies() {
//        scope.launch {
//            val doc = Jsoup.connect(movieurl).get()
//            val moviesTitles = doc.select("div.box-contents>a")
//
//            for(title in moviesTitles){
//                titlesText += title.text().toString() + "\n"
//            }
//
//            withContext(Dispatchers.Main){
//                binding.titles.text = titlesText
//            }
//        }
//    }

