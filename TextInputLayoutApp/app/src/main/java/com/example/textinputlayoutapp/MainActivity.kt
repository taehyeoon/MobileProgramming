package com.example.textinputlayoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.textinputlayoutapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout(){
        binding.emailtext.addTextChangedListener {
            if(it.toString().contains("@")){
                binding.textInputLayout.error = null
            }else{
                binding.textInputLayout.error = "이메일 형식이 올바르지 않습니다."
            }
        }
    }
}