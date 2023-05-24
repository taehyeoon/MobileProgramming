package com.example.broadcastapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyReceiver : BroadcastReceiver() {

    // ^는 시작 부분이라는 의미
    val pattern1 = Regex("""^\d{2}/\d{2}\s([01][0-9]|2[0-3]):([0-5][0-9])\s\d{1,3}(,\d{3})*원""")

    val scope = CoroutineScope(Dispatchers.IO)
    override fun onReceive(context: Context, intent: Intent) {

        // 직접 종료하기 전까지는 시스템에 의해서 이 reciever가 종료되지 않는다
        val pendingResult = goAsync()
        scope.launch {


            if (intent.action.equals("android.provider.Telephony.SMS_RECEIVED")) {
                // 메시지 정보를 가지고 옴
                val msg = Telephony.Sms.Intents.getMessagesFromIntent(intent)

                val message = msg[0].messageBody

                if (message.contains("건국카드")) {
                    // 메시지 문자열을 줄 단위로 분리
                    val tempStr = message.split("\n")

                    var result = false
                    // 건국카드라고 되어있는 첫줄을 제외하고 2번째 줄부터 가져옴
                    for (str in tempStr.subList(1, tempStr.size)) {
                        if (pattern1.containsMatchIn(str)) {
                            result = true
                            break
                        }
                    }

                    if (result) {
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
                /*
            // 수신된 메시지를 toast msg로 출력
            for(smsMessage in msg){
                Log.i("msg", smsMessage.displayMessageBody)
            }
             */
            }
        }
        // launch 내부의 작업들이 모두 끝나면, 시스템이 의해 제거가 될수 있는 상태로 변함
        pendingResult.finish()
    }
}