package com.example.voca

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.voca.MyData
import com.example.voca.databinding.ActivityAddVocBinding
import java.io.PrintStream

class AddVocActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddVocBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVocBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLayout()
    }

    private fun initLayout() {
        binding.apply {
            addbtn.setOnClickListener {
                val word = word.text.toString()
                val meaning = meaning.text.toString()
                val output = PrintStream(openFileOutput("voc.txt", Context.MODE_APPEND))

                output.println(word)
                output.println(meaning)
                output.close()

                val i = Intent()
                i.putExtra("voc", MyData(word, meaning))
                setResult(Activity.RESULT_OK, i) // 데이터를 원래 introactivity로 넘김
                finish() // 액티비티 종료
            }


            cancelbtn.setOnClickListener {
                setResult(Activity.RESULT_CANCELED)
                finish() // 액티비티 종료
            }
        }
    }


}