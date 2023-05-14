package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.insert
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.ActivityTodolistBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.Month
import java.util.Calendar

class TodolistActivity : AppCompatActivity() {

    lateinit var binding: ActivityTodolistBinding
    lateinit var todoDB :TodoItemDatabase
    var recordset = ArrayList<TodoItem>()
    var todoAdapter = TodoAdapter(ArrayList<TodoItem>())

    var year: Int = 0
    var month: Int = 0
    var day: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodolistBinding.inflate(layoutInflater)
        setContentView(binding.root)



//        todoDB.todoDao().deleteAllRecord()




        initDate()
        initRecyclerView()
        initLayout()
    }

    private fun initDate() {
        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        todoAdapter.itemClickListener = object :TodoAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
                val data = todoAdapter.items[position]
                val msg = "${data.content}/ ${data.year}.${data.month+1}.${data.day}/ ${data.priority}"
                Toast.makeText(this@TodolistActivity, msg, Toast.LENGTH_SHORT).show()
            }
        }
        binding.recyclerView.adapter = todoAdapter
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )

    }
    private fun initLayout() {
        todoDB = TodoItemDatabase.getDatabase(this)

        CoroutineScope(Dispatchers.IO).launch {
            getAllRecord()
        }


        binding.apply {
            calendarView.setOnDateChangeListener { view, c_year, c_month, c_dayOfMonth ->
                year = c_year
                month = c_month
                day = c_dayOfMonth

                val msg = "${c_year}.${c_month}.${c_dayOfMonth}"
                Toast.makeText(this@TodolistActivity,msg, Toast.LENGTH_SHORT).show()
            }

            insertBtn.setOnClickListener {
                val content = contentEdit.text.toString()
//                val dateTime = dateTimeEdit.text.toString()
                val priority = priorityEdit.text.toString().toInt()
//                val localDateTime = LocalDateTime.of(year, month, day, 0,0,0)
                val item = TodoItem(0, year, month, day, content, false, priority)
                CoroutineScope(Dispatchers.IO).launch {
                    insert(item)
                }
            }

        }
    }

    private fun insert(item: TodoItem) {
        todoDB.todoDao().insertTodoItem(item)
        getAllRecord()
    }

    private fun getAllRecord() {
        recordset = todoDB.todoDao().getAllRecord() as ArrayList<TodoItem>
        todoAdapter.items = recordset
        CoroutineScope(Dispatchers.Main).launch {
            todoAdapter.notifyDataSetChanged()
        }
    }
}