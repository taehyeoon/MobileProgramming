package com.example.prepareformidterm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.prepareformidterm.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditBinding
    lateinit var existingData : EmployeeData
    var indexData = -1
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        existingData =  intent.getSerializableExtra("existingData") as EmployeeData
        indexData = intent.getIntExtra("pos", -1)
        setContentView(binding.root)


        initLayout()
    }

    private fun initLayout() {
        binding.apply {

            name.setText(existingData.name)
            company.setText(existingData.company)
            phoneNumber.setText(existingData.phoneNumber)

            editBtn.setOnClickListener {
                val name = name.text.toString()
                val company = company.text.toString()
                val phoneNumber = phoneNumber.text.toString()

                val i = Intent()
                i.putExtra("editedData", EmployeeData(name, company, phoneNumber))
                i.putExtra("pos", indexData)

                setResult(Activity.RESULT_OK, i)
                finish()
            }

            cancelBtn.setOnClickListener {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }
    }
}