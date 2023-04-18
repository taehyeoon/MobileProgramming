package com.example.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.recyclerview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    var data:ArrayList<MyData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu1, menu)
//        메뉴 부착
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuitem1 -> {
                binding.recyclerView.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.VERTICAL, false)
            }
            R.id.menuitem2 -> {
                binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
            }
            R.id.menuitem3 -> {
                binding.recyclerView.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun initRecyclerView(){
        binding.recyclerView.layoutManager =  LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = MyDataAdapter(data)
    }

    fun initData(){
        data.add(MyData("item1", 10))
        data.add(MyData("item2", 6))
        data.add(MyData("item3", 14))
        data.add(MyData("item4", 12))
        data.add(MyData("item5", 6))
        data.add(MyData("item6", 11))
        data.add(MyData("item7", 12))
        data.add(MyData("item8", 8))
        data.add(MyData("item9", 12))
        data.add(MyData("item10", 8))
        data.add(MyData("item11", 18))
        data.add(MyData("item12", 12))
    }
}