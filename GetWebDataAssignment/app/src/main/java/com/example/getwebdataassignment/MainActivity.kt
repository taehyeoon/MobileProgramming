package com.example.getwebdataassignment

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.getwebdataassignment.databinding.ActivityMainBinding
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import org.jsoup.Jsoup

class MainActivity : AppCompatActivity() {

    var movieData: ArrayList<MovieData> = ArrayList()
    lateinit var binding: ActivityMainBinding

    val naverQuery =
        "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&ie=utf8&query="
    lateinit var originAdapter: MyAdapter
    lateinit var searchAdapter: MyAdapter

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

                withContext(Dispatchers.Main) {
                    movieData.add(item)
                    originAdapter.notifyDataSetChanged()
                }

            }
        }
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        originAdapter = MyAdapter(movieData)
        Log.d("data Num", movieData.size.toString())
        originAdapter.itemClickListener = object : MyAdapter.OnItemClickListener {
            override fun OnItemClick(data: MovieData, pos: Int) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(naverQuery + data.title))
                startActivity(intent)
            }
        }

        binding.recyclerView.adapter = originAdapter



        binding.apply {
            searchBtn.setOnClickListener {
                if(editText.text.isNotEmpty()){
                    val tempArr = ArrayList<MovieData>()
                    for(x in movieData){
                        if(x.title.contains(editText.text)){
                            tempArr.add(x)
                        }
                    }


                    searchAdapter = MyAdapter(tempArr)

                    recyclerView.adapter = searchAdapter
                }else{
                    recyclerView.adapter = originAdapter
                }
            }
        }

    }
}
