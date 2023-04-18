package com.example.autotextviewapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.autotextviewapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding :ActivityMainBinding

    private val countries = mutableListOf<String>(
        "United States",
        "China",
        "Japan",
        "Germany",
        "United Kingdom",
        "India",
        "France",
        "Italy",
        "Brazil",
        "Canada",
        "South Korea",
        "Australia",
        "Russia",
        "Spain",
        "Mexico",
    )

    lateinit var countries2 : Array<String>

    lateinit var adapter1 : ArrayAdapter<String>
    lateinit var adapter2 : ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        countries2 = resources.getStringArray(R.array.countries_array)
        adapter1 = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, countries)
        adapter2 = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, countries2)

        // autoCompleteTextView
        binding.autoCompleteTextView.setAdapter(adapter1)
        binding.autoCompleteTextView.setOnItemClickListener { adapterView, view, i, l ->
            val item = adapterView.getItemAtPosition(i).toString()
            Toast.makeText(this, "선택된 나라 : $item", Toast.LENGTH_SHORT).show()
        }

        // multiAutoCompleteTextView
        binding.multiAutoCompleteTextView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        binding.multiAutoCompleteTextView.setAdapter(adapter2)

        /*
        editText에 textWatcher를 사용하는 첫번째 방법

        binding.editText.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val str = p0.toString()
                binding.button.isEnabled = str.isNotEmpty()
            }
        })
        editText에 textWatcher를 사용하는 첫번째 방법 end
 */

        /* editText에 textWatcher를 사용하는 두번째 방법
        // 이미 구현은 다 되어있고, 필요한 함수만 오버라이딩하면
        binding.editText.addTextChangedListener(
            afterTextChanged = {
                val str = it.toString()
                binding.button.isEnabled = str.isNotEmpty()
            }
        )

        editText에 textWatcher를 사용하는 두번째 방법 end

         */


        // /* editText에 textWatcher를 사용하는 세번째 방법
        // "afterTextChanged" 함수가 자주 쓰이는 기능이기 떄문에 아래의 중괄호 안에 쓰는
        // 코드들은 "afterTextChanged"를 오버라이딩 하는 것과 같은 의미
        binding.editText.addTextChangedListener {
            val str = it.toString()
            binding.button.isEnabled = str.isNotEmpty()
        }
        // editText에 textWatcher를 사용하는 세번째 방법 end
        //*/

        binding.button.setOnClickListener {
            // 주의 : countries 리스트에 추가하는 것이 아니라, adapter에 추가 해야한다
            adapter1.add(binding.editText.text.toString())
            adapter1.notifyDataSetChanged() // 데이터 셋이 변경된 것을 알려줌, 데이터의 레이아웃이 바뀜
            binding.editText.text.clear()

            Toast.makeText(this, "현재 리스트 size : ${countries.size.toString()}", Toast.LENGTH_SHORT).show()
            Log.d("리스트 체크", countries.size.toString())
        }





    }


}