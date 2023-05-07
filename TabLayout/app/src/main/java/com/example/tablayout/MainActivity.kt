package com.example.tablayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.tablayout.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val textArr = arrayListOf("이미지", "리스트", "팀")
    val imgArr = arrayListOf(R.drawable.baseline_email_24, R.drawable.baseline_car_repair_24, R.drawable.baseline_perm_contact_calendar_24)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }


    private fun initLayout() {
//        MainActivity 는 FragmentActivity 를 상속 받기 때문에 this를 인자로 전달이 가능
        binding.viewpager.adapter = MyViewPagerAdapter(this)

        // 세번째 인자는 인터페이스 함수이고, 하나의 함수만 구현하면 되기 때문에 중괄호로 표현 가능
        TabLayoutMediator(binding.tablayout, binding.viewpager){
            tab, pos ->
            tab.text = textArr[pos]
            tab.setIcon(imgArr[pos])
        }.attach()
        // tablayout과 viewpager를 붙이는 attch를 반드시 마지막에 추가해야함
    }
}