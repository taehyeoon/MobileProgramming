package com.example.getnews

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.getnews.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: MyAdapter
    val url = "https://news.daum.net"
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
            getnews() // 새로고침
        }


        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        // row 사이에 구분자 추가
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))


        adapter = MyAdapter(ArrayList<MyData>())
        adapter.itemClickListener = object : MyAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
                val intent = Intent(ACTION_VIEW, Uri.parse(adapter.items[position].url))
                startActivity(intent)
            }
        }
        binding.recyclerView.adapter = adapter
        getnews()
    }

    private fun getnews() {
        scope.launch {
            adapter.items.clear()
            val doc = Jsoup.connect(url).get()
            Log.i("check", doc.toString())
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