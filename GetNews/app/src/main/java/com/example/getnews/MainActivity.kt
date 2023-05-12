package com.example.getnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.getnews.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.parser.Parser

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: MyAdapter
    val url = "https://news.daum.net"
    val rssurl = "http://fs.jtbc.joins.com//RSS/culture.xml"
    val melonurl = "https://www.melon.com/chart/index.htm"
    val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLayout()
    }

    private fun initLayout() {
        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = true // progressbar가 화면에 보여지는 상태
            getmelonchart() // 새로고침
        }


        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        // row 사이에 구분자 추가
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))


        adapter = MyAdapter(ArrayList<MyData>())
        adapter.itemClickListener = object : MyAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
//                val intent = Intent(ACTION_VIEW, Uri.parse(adapter.items[position].url))
//                startActivity(intent)
                Toast.makeText(this@MainActivity, adapter.items[position].url, Toast.LENGTH_SHORT).show()
            }
        }
        binding.recyclerView.adapter = adapter
//        getnews()
//        getrssnews()
        getmelonchart()
    }

    private fun getmelonchart() {
        scope.launch {
            adapter.items.clear()
            // .get()은 디폴트가 html paser이다.
            val doc = Jsoup.connect(melonurl).get()
            Log.i("check", doc.toString())
//            val songs = doc.select("tbody>tr>td:nth-child(6)>div>div") // div.wrap_song_info
            val songs = doc.select("div.wrap_song_info") // div.wrap_song_info

//            Log.d("number" songs.size.toString());
            for(song in songs){
//                adapter.items.add(MyData(song.select("div:nth-child(1)>span>a").text(), song.select("div:nth-child(3)>a").text()))
                adapter.items.add(MyData(song.select("div.ellipsis.rank01").text(), song.select("div.ellipsis.rank02").text()))
            }
            withContext(Dispatchers.Main){
                adapter.notifyDataSetChanged()
                binding.swipe.isRefreshing = false
            }
        }
    }


    private fun getrssnews() {
        scope.launch {
            adapter.items.clear()
            val doc = Jsoup.connect(rssurl).parser(Parser.xmlParser()).get()
//            Log.i("check", doc.toString())
            val headlines = doc.select("item")
            for(news in headlines){
                adapter.items.add(MyData(news.select("title").text(), news.select("link").text()))

            }
            withContext(Dispatchers.Main){
                adapter.notifyDataSetChanged()
                binding.swipe.isRefreshing = false
            }
        }
    }

    // 다음 뉴스
    private fun getnews() {
        scope.launch {
            adapter.items.clear()
            // .get()은 디폴트가 html paser이다.
            val doc = Jsoup.connect(url).get()
//            Log.i("check", doc.toString())
            val headlines = doc.select("ul.list_newsissue>li>div.item_issue>div>strong.tit_g>a")
            for(news in headlines){
                adapter.items.add(MyData(news.text(), news.absUrl("href")))
                news.text()
            }
            withContext(Dispatchers.Main){
                adapter.notifyDataSetChanged()
                binding.swipe.isRefreshing = false
            }
        }
    }
}