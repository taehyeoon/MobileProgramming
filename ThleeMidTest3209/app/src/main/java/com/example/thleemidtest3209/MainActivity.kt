package com.example.thleemidtest3209

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.accessibility.AccessibilityViewCommand.ScrollToPositionArguments
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thleemidtest3209.databinding.ActivityMainBinding
import com.example.thleemidtest3209.databinding.OrderrowBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val saledata : ArrayList<SaleData> = ArrayList()
    private val mydata : ArrayList<MyData> = ArrayList()

    lateinit var saleAdapter: SaleDataAdapter
    lateinit var myAdapter: MyDataAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        initLayout()
        initData()
    }

    private fun initData() {
        saledata.add(SaleData("새우깡", 200, 3))
        saledata.add(SaleData("감자깡", 500, 2))
        saledata.add(SaleData("고구마깡", 400, 5))
        saledata.add(SaleData("자갈치", 600, 2))
        saledata.add(SaleData("포스틱", 800, 3))
        saledata.add(SaleData("감자깡", 500, 2))
        saledata.add(SaleData("고구마깡", 400, 5))
        saledata.add(SaleData("자갈치", 600, 2))
        saledata.add(SaleData("포스틱", 800, 3))
    }

    fun initRecyclerView(){
        binding.recyclerView1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        saleAdapter = SaleDataAdapter(saledata)
        saleAdapter.itemClickListener = object : SaleDataAdapter.OnItemClickListener{
            override fun OnItemClick(data: SaleData, position:Int, binding:OrderrowBinding) {
                if(data.num > 0){
                    for(ddd in mydata){
                        if(ddd.name == data.name){
                            ddd.num += 1
                            saleAdapter.notifyDataSetChanged()
                            myAdapter.notifyDataSetChanged()
                            saledata[position].num -= 1
                            return
                        }
                    }
                    mydata.add(MyData(data.name, data.price, 1))
                    myAdapter.notifyDataSetChanged()

                    saledata[position].num -= 1
                    saleAdapter.notifyItemChanged(position)
                }else if(data.num == 0){
                    Toast.makeText(this@MainActivity, "재고부족", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.recyclerView1.adapter = saleAdapter









        binding.recyclerView2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        myAdapter = MyDataAdapter(mydata)
        myAdapter.itemClickListener = object : MyDataAdapter.OnItemClickListener{
            override fun OnItemClick(data: MyData, position:Int, binding:OrderrowBinding) {
                mydata[position].num -= 1
                for(sss in saledata){
                    if(sss.name == data.name){
                        sss.num += 1
                        saleAdapter.notifyDataSetChanged()
                        myAdapter.notifyDataSetChanged()
                        break
                    }
                }


                Toast.makeText(this@MainActivity, mydata[position].num.toString(), Toast.LENGTH_SHORT).show()
                if(mydata[position].num == 0)
                    myAdapter.removeitem(position)
                else{
                    myAdapter.notifyDataSetChanged()
                }
            }
        }
        binding.recyclerView2.adapter = myAdapter




    }

    private fun initLayout() {
        binding.adminbtn.setOnClickListener {
            Toast.makeText(this, "201811563 이태현",Toast.LENGTH_SHORT).show()
            callAction()
        }



    }
    fun callphonePermissionGranted() = ActivityCompat.checkSelfPermission(this,
        android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED

    fun callAction(){
        val number = Uri.parse("tel:010-1234-1234")
        val callIntent = Intent(Intent.ACTION_CALL, number)
        if(callphonePermissionGranted())
            startActivity(callIntent)
        else
            checkPermissions()
    }

    fun checkPermissions(){
        when{
            (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED)
            ->{
                // call_Phone, camera 모든 권한이 승인된 경우
                Toast.makeText(this, "모든 권한 승인됨", Toast.LENGTH_SHORT).show()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.CALL_PHONE)
            ->{
//            명시적으로 권한이 거부된 경우
                Toast.makeText(this, "권한 승인을 거부했습니다.", Toast.LENGTH_SHORT).show()
                callAlertDlg()

            }
            else ->{
//            앱이 처음 실행된 경우 (명시적으로 허용되지도 않고, 거부되지도 않은 경우)
                permissionLauncher.launch(android.Manifest.permission.CALL_PHONE)
            }
        }
    }

    fun callAlertDlg(){
        // builder 객체를 이용해서 AlertDlg의 다양한 정보를 쉽게 설정할 수 있음
        val builder = AlertDialog.Builder(this)
        builder.setMessage("반드시 CALL_PHONE권한이 허용되어야합니다.")
            .setTitle("권한체크")
            .setPositiveButton("OK"){
                    _,_->
                permissionLauncher.launch(android.Manifest.permission.CALL_PHONE)
            }.setNegativeButton("Cancel"){
                    dlg,_->
                dlg.dismiss() // 다이얼로그 종료
            }

        // 반드시 create하여 생성하고 show해야 보여진다
        val dlg = builder.create()
        dlg.show()
    }

    //    승인을 요청하고자 하는 callback을 등록하면 된다
    val permissionLauncher=
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
//        callback 함수에서 수행할 내용 작성
            if(it){
//            권한 승인이 된 경우
                callAction()
            }else{
                Toast.makeText(this,
                    "권한 승인이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
}
}