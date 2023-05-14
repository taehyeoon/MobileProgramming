package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLayout()
    }


    private fun initLayout() {
        binding.apply {
            plusTodolistBtn.setOnClickListener {
                val i = Intent(this@MainActivity, TodolistActivity::class.java)
                startActivity(i)
            }

            calendarBtn.setOnClickListener {
                val i = Intent(this@MainActivity, TodolistActivity::class.java)
                startActivity(i)
            }
        }

    }


}