package com.example.voca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voca.databinding.ActivityMainBinding
import com.example.voca.databinding.RowBinding
import java.io.FileNotFoundException
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val data : ArrayList<MyData> = ArrayList()
    lateinit var adapter: MyDataAdapter
    lateinit var tts: TextToSpeech
    var isTTsReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initRecyclerView()
        initTTS()
    }


    private fun initTTS() {
        tts = TextToSpeech(this){
            // 두번째 인자가 함수인 경우, 밖으로 빼서 표현할 수 있다.
            // 이 부분은 TextToSpeech.OnInitListener 부분
            // TTS 사용 준비가 된 경우 call
            isTTsReady = true
            tts.language = Locale.US // 영어로 읽을 수 있음
        }
    }
    fun readScanFile(scan:Scanner){
        while(scan.hasNextLine()){
            val word = scan.nextLine()
            val meaning = scan.nextLine()
            data.add(MyData(word, meaning))
        }
    }
    fun initData(){

        try{
            val scan2 = Scanner(openFileInput("voc.txt"))
            readScanFile(scan2)
        }catch (e : FileNotFoundException){

        }
        val scan = Scanner(resources.openRawResource(R.raw.words))
        readScanFile(scan)

    }
    fun initRecyclerView(){
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = MyDataAdapter(data)
        adapter.itemClickListener = object : MyDataAdapter.OnItemClickListener{
            override fun OnItemClick(data: MyData, position:Int, binding:RowBinding) {
                Toast.makeText(this@MainActivity, data.meaning,Toast.LENGTH_SHORT).show()
                if(isTTsReady){
                    tts.speak(data.word, TextToSpeech.QUEUE_ADD, null, null)
                }
                // 단어 뜻 보여주기
                adapter.changeItemOpenState(position, binding)

            }
        }
        binding.recyclerView.adapter = adapter


        val simpleCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT){
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
                Toast.makeText(this@MainActivity, "swiped", Toast.LENGTH_SHORT).show()
            }
        }

        // 위의 콜백을 이용하여 itemtouchhelper 객체를 만들어야함
        val itemTouchHelper = ItemTouchHelper(simpleCallback)

        // 이 itemTouchHelper를 recylcerview에 attach 해야한다
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    override fun onStop() {
        super.onStop()
        tts.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        // TTS 사용이 끝나면 메모리 누수를 막기 위해 직접 TTS서비스를 종료 시켜야한다
        tts.shutdown()
    }
}