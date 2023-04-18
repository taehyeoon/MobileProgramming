package com.example.intenttest

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.intenttest.databinding.ActivityMainBinding
import java.security.Permission
import java.security.Permissions

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
        checkPermissions()
    }

    val permissions = arrayOf(
        android.Manifest.permission.CALL_PHONE, android.Manifest.permission.CAMERA)

    // 여러 요청된 권한에 대한 승인 여부 Map정보로 반환
    val multiplePermissionLauncher=
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            val resultPermission = it.all{map ->
                // 여러개의 value 중 하나만 false이더라도 이 resultPermission은 false값을 가짐
                map.value
            }

            if(!resultPermission){
                /*
                모든 권한이 승인 되지 않은 경우, 앱을 종료 시킨다
                finish()
                 */
                Toast.makeText(this, "모든 권한이 승인 되어야 함", Toast.LENGTH_SHORT).show()
            }
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
    fun allPermissionGranted() = permissions.all{
        ActivityCompat.checkSelfPermission(this,it) == PackageManager.PERMISSION_GRANTED
    }
    fun callphonePermissionGranted() = ActivityCompat.checkSelfPermission(this,
        android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED

    fun cameraPermissionGranted() = ActivityCompat.checkSelfPermission(this,
        android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED


    fun checkPermissions(){
        when{
            (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) &&
            (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED)
                ->{
                    // call_Phone, camera 모든 권한이 승인된 경우
                    Toast.makeText(this, "모든 권한 승인됨", Toast.LENGTH_SHORT).show()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.CALL_PHONE) ||
            ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.CAMERA)
            ->{
//            명시적으로 권한이 거부된 경우
                permissionCheckAlertDlg()
            }
            else ->{
//            앱이 처음 실행된 경우 (명시적으로 허용되지도 않고, 거부되지도 않은 경우)
                multiplePermissionLauncher.launch(permissions)
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

    fun permissionCheckAlertDlg(){
        // builder 객체를 이용해서 AlertDlg의 다양한 정보를 쉽게 설정할 수 있음
        val builder = AlertDialog.Builder(this)
        builder.setMessage("반드시 CALL_PHONE과 CAMERA 권한이 모두 허용되어야 합니다.")
            .setTitle("권한체크")
            .setPositiveButton("OK"){
                    _,_->
                multiplePermissionLauncher.launch(permissions)
            }.setNegativeButton("Cancel"){
                    dlg,_->
                dlg.dismiss() // 다이얼로그 종료
            }

        // 반드시 create하여 생성하고 show해야 보여진다
        val dlg = builder.create()
        dlg.show()
    }
    private fun cameraAction() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        if(cameraPermissionGranted())
        if(allPermissionGranted())
            startActivity(intent)
        else
            checkPermissions()
    }

    fun callAction(){
        val number = Uri.parse("tel:010-1234-1234")
        val callIntent = Intent(Intent.ACTION_CALL, number)
//        if(callphonePermissionGranted())
        if(allPermissionGranted()) // 이렇게하면 카메라, 폰콜 모두 허가 되어야 실행 가능하다
            startActivity(callIntent)
        else
            checkPermissions()
    }

/*
    fun callAction(){
        val number = Uri.parse("tel:010-1234-1234")
        val callIntent = Intent(Intent.ACTION_CALL, number)

        when{
            ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED ->{
//            권한이 허용된 경우
                        startActivity(callIntent)
                    }
            ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.CALL_PHONE)
                    ->{
//            명시적으로 권한이 거부된 경우
                callAlertDlg()
                    }
            else ->{
//            앱이 처음 실행된 경우 (명시적으로 허용되지도 않고, 거부되지도 않은 경우)
                permissionLauncher.launch(android.Manifest.permission.CALL_PHONE)
            }
        }
    }
    */
    private fun initLayout() {
        binding.apply {
            callBtn.setOnClickListener {
                callAction()
            }

            msgBtn.setOnClickListener {
                val message = Uri.parse("sms:010-4321-4321")
                val messageIntent = Intent(Intent.ACTION_SENDTO, message)
                messageIntent.putExtra("sms_body", "빨리 다음꺼 하자..")
                startActivity(messageIntent)
            }

            webBtn.setOnClickListener {
                val webpage = Uri.parse("https://www.naver.com")
                val webIntent = Intent(Intent.ACTION_VIEW, webpage)
                startActivity(webIntent)
            }

            mapBtn.setOnClickListener {
                val location = Uri.parse("geo:37.543684,127.077130")
                val mapIntent = Intent(Intent.ACTION_VIEW, location)
                startActivity(mapIntent)
            }

            cameraBtn.setOnClickListener {
                cameraAction()
            }
        }
    }


}