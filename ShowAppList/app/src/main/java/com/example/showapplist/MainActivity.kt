package com.example.showapplist

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.showapplist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        // 빈 데이터 리스트를 생성해서 adapter에서 넘김
        adapter = MyAdapter(ArrayList<MyData>())

        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        // 앱의 버전이 "티라미수"(API13)보다 높다면 ResilveInfoFlags함수를 실행
        val applist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.queryIntentActivities(intent, PackageManager.ResolveInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            packageManager.queryIntentActivities(intent, 0)
        }

        // intent를 이용하여 얻어온 앱 리스트가 1개 이상일 때
        if(applist.size > 0){
            // 앱에 대한 정보를 adapter클래스의 items에 저장한다
            for (appinfo in applist){
                var applabel = appinfo.loadLabel(packageManager).toString()
                var appclass = appinfo.activityInfo.name
                var apppackname = appinfo.activityInfo.packageName
                var appicon = appinfo.loadIcon(packageManager)
                adapter.items.add(MyData(applabel, appclass, apppackname, appicon))
            }
        }

        adapter.itemClickListener = object: MyAdapter.OnItemClickListener{
            override fun OnItemClick(data: MyData) {
                // 패키지 이름 정보를 가지고 있는 인텐트 생성
                val intent = packageManager.getLaunchIntentForPackage(data.apppackname)
                startActivity(intent)
            }
        }

        binding.recyclerView.adapter = adapter
    }
}