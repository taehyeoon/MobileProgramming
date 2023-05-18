package com.example.broadcastapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import android.widget.Toast

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action.equals("android.provider.Telephony.SMS_RECEIVED")){
            // 메시지 정보를 가지고 옴
            val msg = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            /*
            // 수신된 메시지를 toastmsg로 출력
            for(smsMessage in msg){
                Log.i("msg", smsMessage.displayMessageBody)
            }
             */

            val newIntent = Intent(context, MainActivity::class.java)

            newIntent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or // task 새롭게 만들어서 실행
                Intent.FLAG_ACTIVITY_SINGLE_TOP or // 실행 중인데 task가 top에 있었다면 재사용
                Intent.FLAG_ACTIVITY_CLEAR_TOP // 위에 다른 모든 task 제거하고, 현재 task를 top에 올림
            // msg[0].originatingAddress : 문자 메시지를 보낸 사람에 대한 정보
            newIntent.putExtra("msgSender", msg[0].originatingAddress)
            newIntent.putExtra("msgBody", msg[0].messageBody)
            context.startActivity(newIntent)
        }

    }
}