package com.example.runningrecord

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.runningrecord.MainActivity.Companion.data
import com.example.runningrecord.databinding.ExcerciseAddBinding

class AddActivity : AppCompatActivity(){
    lateinit var addBinding: ExcerciseAddBinding
    lateinit var weatherStringList : Array<String>
    lateinit var contents : ArrayList<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addBinding = ExcerciseAddBinding.inflate(layoutInflater)
        weatherStringList = resources.getStringArray(R.array.weather_list)
        addBinding.apply {
            contents = arrayListOf(
                dateText,startTimeText, endTimeText, placeText, distanceText)}

        setContentView(addBinding.root)

        initLayout()
    }

    private fun checkContentValid(): Boolean {
        if (contents[0].text.toString().isBlank()) {
            Toast.makeText(this@AddActivity, "fill date", Toast.LENGTH_SHORT).show()
            return false
        }
        if (contents[1].text.toString().isBlank()) {
            Toast.makeText(this@AddActivity, "fill startTime", Toast.LENGTH_SHORT).show()
            return false
        }
        if (contents[2].text.toString().isBlank()) {
            Toast.makeText(this@AddActivity, "fill endTime", Toast.LENGTH_SHORT).show()
            return false
        }
        if (contents[3].text.toString().isBlank()) {
            Toast.makeText(this@AddActivity, "fill place", Toast.LENGTH_SHORT).show()
            return false
        }
        if (contents[4].text.toString().toDoubleOrNull() == null) {
            Toast.makeText(this@AddActivity, "distance sould be float", Toast.LENGTH_SHORT).show()
            return false
        }

        if (addBinding.levelRadioGroup.checkedRadioButtonId == -1) {
            Toast.makeText(this@AddActivity, "check one level", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun initLayout() {
        addBinding.apply {
//            recordBtn을 클릭하면 기록 리스트 페이지로 이동
            recordBtn.setOnClickListener {
                val intent = Intent(this@AddActivity, ListActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

//            날씨를 드롭 메뉴로 설정
            weatherSpinner.adapter = ArrayAdapter(this@AddActivity, android.R.layout.simple_spinner_dropdown_item, weatherStringList)
            weatherSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

            saveBtn.setOnClickListener {
                if(checkContentValid()){
                    Toast.makeText(this@AddActivity, "allVaild", Toast.LENGTH_SHORT).show()
                    val date = contents[0].text.toString()
                    val startTime = contents[1].text.toString()
                    val endTime = contents[2].text.toString()
                    val place = contents[3].text.toString()
                    val distance = contents[4].text.toString().toFloat()
                    val level = when(addBinding.levelRadioGroup.checkedRadioButtonId){
                        addBinding.verySlow.id -> 1
                        addBinding.slow.id -> 2
                        addBinding.normal.id -> 3
                        addBinding.fast.id -> 4
                        addBinding.veryFast.id -> 5
                        else -> 1
                    }
//                    val level = addBinding.levelRadioGroup.checkedRadioButtonId + 1
                    val weather = addBinding.weatherSpinner.selectedItemPosition + 1

//                    정보 저장
                    MainActivity.data.add(RunData(date, startTime, endTime, place, distance, level, weather))

//                    초기화
                    contents[0].setText("")
                    contents[1].setText("")
                    contents[2].setText("")
                    contents[3].setText("")
                    contents[4].setText("")

                    findViewById<RadioButton>(addBinding.levelRadioGroup.checkedRadioButtonId).isChecked = false
                    addBinding.weatherSpinner.setSelection(0)
                }else{
                    Toast.makeText(this@AddActivity, "InVaild", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


}