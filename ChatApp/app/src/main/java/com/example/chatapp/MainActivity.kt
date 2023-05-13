package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.chatapp.databinding.ActivityMainBinding
import org.json.JSONObject
import java.lang.reflect.Method

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var url = "https://api.openai.com/v1/completions"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        binding.apply {
            sendBtn.setOnClickListener {
                responseTV.text = "잠시 기다려주세요..."
                if(userQuestion.text.toString().length > 0){
                    getResponse(userQuestion.text.toString())
                }else{
                    Toast.makeText(this@MainActivity, "질문을 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

//    query : 사용자가 질의한 내용
    private fun getResponse(query: String) {
        binding.questionTV.text = query
        binding.userQuestion.setText("")

        val queue = Volley.newRequestQueue(this)
        val jsonObject = JSONObject()
        jsonObject.put("model", "text-davinci-003")
        jsonObject.put("prompt", query)
        jsonObject.put("temperature", 0)// 숫자가 클수록 더 많은 무작위성을 나타냄
        jsonObject.put("max_tokens", 200)

        val postRequest = object : JsonObjectRequest(
            Method.POST, url, jsonObject,
        Response.Listener {
        // 접속 성공 했을 때
          // 받아온 데이터 그대로 출력해보기
          Log.i("check", it.toString())
//                          Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        },
        Response.ErrorListener {
            Log.e("Error", it.message.toString())
        })
        {
            // post방식을 사용하기 떄문에 header 정보를 수정할 필요가 있
            override fun getHeaders(): MutableMap<String, String> {
                val header = HashMap<String, String>()
                header["Content-Type"] = "application/json"
                header["Authorization"] = "Bearer insert-api-key"
                return header
            }
        }

        // request를 queue에 집어넣음
        queue.add(postRequest)

    }
}