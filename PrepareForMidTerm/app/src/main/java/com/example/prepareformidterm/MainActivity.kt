package com.example.prepareformidterm

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepareformidterm.databinding.ActivityMainBinding
import com.example.prepareformidterm.databinding.RowBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var adapter: EmployeeDataAdapter

    val data: ArrayList<EmployeeData> = ArrayList()
    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            @Suppress("DEPRECATION")
            val editedData = it.data?.getSerializableExtra("editedData") as EmployeeData
            val pos = it.data?.getIntExtra("pos", -1)
            if (pos == -1) {
                Toast.makeText(this@MainActivity, "editedData is wrong", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this@MainActivity, editedData.company, Toast.LENGTH_SHORT).show()
                if (pos != null) {
                    data[pos].name = editedData.name
                    data[pos].company = editedData.company
                    data[pos].phoneNumber = editedData.phoneNumber

                    adapter.notifyItemChanged(pos)
                }
            }
        } else {
            Toast.makeText(this@MainActivity, "canceled", Toast.LENGTH_SHORT).show()
        }
    }

    val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
//        callback 함수에서 수행할 내용 작성
            if (it) {
//            권한 승인이 된 경우
                callAction()
            } else {
                Toast.makeText(
                    this,
                    "권한 승인이 거부되었습니다.", Toast.LENGTH_SHORT
                ).show()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initData()
        initRecyclerView()
        initLayout()
    }

    private fun initData() {
        data.add(EmployeeData("a","com","010-1234-5678"))
        data.add(EmployeeData("b","com","010-1234-5678"))
        data.add(EmployeeData("c","com","010-1234-5678"))
        data.add(EmployeeData("d","com","010-1234-5678"))
        data.add(EmployeeData("e","com","010-1234-5678"))
        data.add(EmployeeData("f","com","010-1234-5678"))
        data.add(EmployeeData("g","com","010-1234-5678"))
        data.add(EmployeeData("h","com","010-1234-5678"))
        data.add(EmployeeData("i","com","010-1234-5678"))
        data.add(EmployeeData("j","com","010-1234-5678"))
        data.add(EmployeeData("k","com","010-1234-5678"))
        data.add(EmployeeData("l","com","010-1234-5678"))
        data.add(EmployeeData("m","com","010-1234-5678"))
        data.add(EmployeeData("n","com","010-1234-5678"))
        data.add(EmployeeData("o","com","010-1234-5678"))
        data.add(EmployeeData("p","com","010-1234-5678"))
        data.add(EmployeeData("q","com","010-1234-5678"))
    }


    private fun initRecyclerView() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = EmployeeDataAdapter(data)
        adapter.itemClickListener = object : EmployeeDataAdapter.OnItemClickListener {
            override fun OnitemClick(data: EmployeeData, pos: Int, binding: RowBinding) {
                Toast.makeText(this@MainActivity, data.name, Toast.LENGTH_SHORT).show()

                val i = Intent(this@MainActivity, EditActivity::class.java)
                i.putExtra(
                    "existingData",
                    EmployeeData(data.name, data.company, data.phoneNumber)
                )
                i.putExtra("pos", pos)
                launcher.launch(i)
            }

            override fun onImageClick(data: EmployeeData, pos: Int, binding: RowBinding) {
                Toast.makeText(this@MainActivity, "image clicked", Toast.LENGTH_SHORT)
                    .show()

                if(callphonePermissionGranted())
                    callAction()
                else
                    callAlertDlg()
                data.callNumber += 1

                adapter.notifyItemChanged(pos)
            }
        }

        binding.recyclerView.adapter = adapter
    }


    private fun initLayout() {

        binding.apply {
            registerBtn.setOnClickListener {
                val name = name.text.toString()
                val company = company.text.toString()
                val phoneNumber = phoneNumber.text.toString()

                data.add(EmployeeData(name, company, phoneNumber))
                adapter.notifyItemChanged(adapter.itemCount)

                binding.name.setText("")
                binding.company.setText("")
                binding.phoneNumber.setText("")
            }

        }
    }


    fun callphonePermissionGranted() = ActivityCompat.checkSelfPermission(
        this,
        android.Manifest.permission.CALL_PHONE
    ) == PackageManager.PERMISSION_GRANTED

    private fun callAction() {
        val number = Uri.parse("tel:010-1234-1234")
        val callIntent = Intent(Intent.ACTION_CALL, number)
        if (callphonePermissionGranted())
            startActivity(callIntent)
        else
            checkCallPermission()
    }

    fun callAlertDlg() {
        // builder 객체를 이용해서 AlertDlg의 다양한 정보를 쉽게 설정할 수 있음
        val builder = AlertDialog.Builder(this)
        builder.setMessage("반드시 CALL_PHONE권한이 허용되어야합니다.")
            .setTitle("권한체크")
            .setPositiveButton("OK") { _, _ ->
                permissionLauncher.launch(android.Manifest.permission.CALL_PHONE)
            }.setNegativeButton("Cancel") { dlg, _ ->
                dlg.dismiss() // 다이얼로그 종료
            }

        // 반드시 create하여 생성하고 show해야 보여진다
        val dlg = builder.create()
        dlg.show()
    }

    private fun checkCallPermission() {
        when {
            (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED)
            -> {
                Toast.makeText(this@MainActivity, "전화권한 승인됨", Toast.LENGTH_SHORT).show()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.CALL_PHONE
            )
            -> {
                callAlertDlg()
            }
        }
    }
}
