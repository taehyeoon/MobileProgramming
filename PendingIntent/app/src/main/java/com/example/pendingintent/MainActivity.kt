package com.example.pendingintent

import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.pendingintent.databinding.ActivityMainBinding
import com.example.pendingintent.databinding.MytimepickerdlgBinding
import java.util.*
import kotlin.math.min

class MainActivity : AppCompatActivity(), OnTimeSetListener {

    lateinit var binding: ActivityMainBinding
    var msg:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        initLayout()
        initLayout2()
    }

    private fun initLayout2() {
        binding.calendarView.setOnDateChangeListener { view,
                                                       year, month, dayOfMonth ->
            msg = "${year}년 ${month + 1}월 ${dayOfMonth}일 "

            // 현재 시간 정보를 가지고 옴
            val cal = Calendar.getInstance()
//            val hour = cal.get(Calendar.HOUR) // AM, PM 형태
            val hour = cal.get(Calendar.HOUR_OF_DAY) // 24 형태
            val minute = cal.get(Calendar.MINUTE)
            val dlgBinding = MytimepickerdlgBinding.inflate(layoutInflater)
            dlgBinding.timePicker.hour = hour
            dlgBinding.timePicker.minute = minute
            val dlgBuilder = AlertDialog.Builder(this)
            dlgBuilder.setView(dlgBinding.root)
                .setPositiveButton("추가" ){_,_->
                    msg += "${dlgBinding.timePicker.hour}시 ${dlgBinding.timePicker.minute}분 =>"
                    msg += dlgBinding.message.text.toString()
                    val timerTask = object:TimerTask(){
                        override fun run() {
                            makeNotification()
                        }
                    }

                    val timer = Timer()
                    timer.schedule(timerTask, 2000) // 2초 후에 run()을 실행 시킨다는 의
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("취소"){_,_->
                    //
                }
                .show()
        }
    }

    private fun initLayout() {
        binding.calendarView.setOnDateChangeListener { view,
                                                       year, month, dayOfMonth ->
            msg = "${year}년 ${month + 1}월 ${dayOfMonth}일 "

            // 현재 시간 정보를 가지고 옴
            val cal = Calendar.getInstance()
//            val hour = cal.get(Calendar.HOUR) // AM, PM 형태
            val hour = cal.get(Calendar.HOUR_OF_DAY) // 24 형태
            val minute = cal.get(Calendar.MINUTE)

            val timePicker = TimePickerDialog(this, this, hour, minute, true)

            timePicker.show()
        }
    }

    // 시간을 설정하고 다이얼로그에서 OK 버튼을 선택하면 실행되는 함수
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        if(view != null){
            msg += "${hourOfDay}시 ${minute}분"

            val timerTask = object:TimerTask(){
                override fun run() {
                    makeNotification()
                }
            }

            val timer = Timer()
            timer.schedule(timerTask, 2000) // 2초 후에 run()을 실행 시킨다는 의
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun makeNotification() {
        val id = "MyChannel"
        val name = "TimeCheckChannel"
        val notificationChannel = NotificationChannel(id, name,
        NotificationManager.IMPORTANCE_DEFAULT)
        // IMPORTANCE_DEFAULT : 알람은 뜨고, head up 메시지는 안 뜸
        notificationChannel.enableVibration(true)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.BLUE
        // 알림은 뜨지만 headup 메시지는 안뜸
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        val builder  = NotificationCompat.Builder(this, id)
            .setSmallIcon(R.drawable.alarm_image)
            .setContentTitle("일정 알람")
            .setContentText(msg)
            .setAutoCancel(true)

        // 화면 전환하고 싶은 인텐트 생
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("time", msg)
        // FLAG_ACTIVITY_NEW_TASK: 이 앱이 실행되지 않을 때, 알림을 클릭하면, 앱 task를 새로 만들어서 앱을 실행한다
        // FLAG_ACTIVITY_CLEAR_TOP : 이 앱이 top 상태가 아닌 경우 top 상태로 만든다
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        val pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        builder.setContentIntent(pendingIntent)

        val notification = builder.build()

        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(notificationChannel)
        manager.notify(10, notification)
    }
}