package com.example.runningrecord

import android.content.Intent
import android.os.Bundle
import android.os.Debug
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.runningrecord.MainActivity.Companion.data
import com.example.runningrecord.databinding.ActivityMainBinding
import com.example.runningrecord.databinding.ExerciseRecordListBinding
import com.example.runningrecord.databinding.RowBinding

class ListActivity : AppCompatActivity() {

    lateinit var listBinding: ExerciseRecordListBinding
    lateinit var adapter: RunDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listBinding = ExerciseRecordListBinding.inflate(layoutInflater)
        setContentView(listBinding.root)

        initLayout()
        initRecyclerView()
    }

    private fun initLayout() {
        listBinding.addBtn.setOnClickListener {
            val intent = Intent(this@ListActivity, AddActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

    }

    private fun initRecyclerView(){
        listBinding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = RunDataAdapter(data)
        adapter.itemClickListener = object : RunDataAdapter.OnItemClickListener{
            override fun OnItemClick(data: RunData, position:Int) {
                data.isShawn = !data.isShawn
                adapter.notifyItemChanged(position)
            }
        }
        listBinding.recyclerView.adapter = adapter


        val simpleCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                adapter.moveItem(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // 이 부분에 데이터가 삭제 되었다는 것을 알림
                adapter.removeItem(viewHolder.adapterPosition)
                Toast.makeText(this@ListActivity, "swiped", Toast.LENGTH_SHORT).show()
            }
        }

//         위의 콜백을 이용하여 itemtouchhelper 객체를 만들어야함
        val itemTouchHelper = ItemTouchHelper(simpleCallback)

        // 이 itemTouchHelper를 recylcerview에 attach 해야한다
        itemTouchHelper.attachToRecyclerView(listBinding.recyclerView)
    }





}