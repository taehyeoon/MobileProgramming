package com.example.broadcastapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.broadcastapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var broadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initPermission()
        initLayout()

        /*
            이 액티비티는 다양하게 실행되어질 수 있는데
            액티비티가 실행 될 때, intent정보도 같이 넘어온다
            intent정보를 가지고 br에서 실행되었는지 판단하여 Toastmsg를 띄운다
        */
        checkSettingOverlayWindow(intent)
    }

    // 이 앱이 현재 실행중이라면 oncreate가 아닌 onNewIntent 부터 실행된다.
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        checkSettingOverlayWindow(intent)
    }
    fun getMessage(intent : Intent?){
        if(intent != null){
            // br에 의해서 전달된 intent가 맞는지 확인
            if(intent.hasExtra("msgSender") and intent.hasExtra("msgBody")){
                val msgSender = intent.getStringExtra("msgSender")
                val msgBody = intent.getStringExtra("msgBody")
                Toast.makeText(this, "보낸번호 : $msgSender\n$msgBody", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // requestSettingLauncher를 실행하기 위한 함
    fun checkSettingOverlayWindow(intent : Intent?){
        if(Settings.canDrawOverlays(this)){
            getMessage(intent)
        }else{
            // Settings.ACTION_MANAGE_OVERLAY_PERMISSION 이 창으로 화면 전환 하려고 함
            val overlayIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            requestSettingLauncher.launch(overlayIntent)
        }
    }

    val requestSettingLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            // 햔재 오버레이에 대한 설정이 되어 있는지에 대한 정보를 가져옴
            if(Settings.canDrawOverlays(this)){
                getMessage(this.intent)
            }else{
                Toast.makeText(this, "[requestSettingLauncher] 권한 승인이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }



    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                initPermission()
            }else{
                Toast.makeText(this, "[requestPermissionLauncher 권한 승인이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    private fun initPermission() {
        when{
            // 권한이 승인된 경우
            (ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) ->{
                Toast.makeText(this, "문자 수신 동의함", Toast.LENGTH_SHORT).show()
            }

            // 권한이 명시적으로 거부된 경우
            ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.RECEIVE_SMS)->{
                permissionDlg()
            }

            // 권한에 대해서 처음 물어보는 경
            else->{
                requestPermissionLauncher.launch(android.Manifest.permission.RECEIVE_SMS)
            }
        }
    }

    private fun permissionDlg() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("반드시 문자 수신에 대한 권한이 허용되어야 합니다.")
            .setTitle("권한 체크")
            .setPositiveButton("OK"){
                _,_->
                requestPermissionLauncher.launch(android.Manifest.permission.RECEIVE_SMS)
            }
            .setNegativeButton("Canel"){
                dlg,_->dlg.dismiss()
            }
        val dlg = builder.create()
        dlg.show()
    }

    private fun initLayout() {
        broadcastReceiver = object:BroadcastReceiver(){
            // 특정 메시지가 전달되면 context 변수로 전달된다.
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent != null){
                    if(intent.action.equals(Intent.ACTION_POWER_CONNECTED)){
                        Toast.makeText(context, "충전 시작", Toast.LENGTH_SHORT).show()
                    }else if(intent.action.equals(Intent.ACTION_POWER_DISCONNECTED)){
                        Toast.makeText(context, "충전 해제", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onResume() {
        // running 상태에 진입하기 직전에 호출됨
        super.onResume()
        // 충전기에 꽃았을 때 정보를 수신
        val intentFilter = IntentFilter(Intent.ACTION_POWER_CONNECTED)
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED)
        registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(broadcastReceiver)
    }
}