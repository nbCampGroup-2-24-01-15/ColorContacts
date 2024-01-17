package com.example.colorcontacts


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.colorcontacts.data.User


/**
 *  TODO : 알람 관련 클래스(Broadcast 수신)
 *  채널은 하나만 생성하고 해당되는 ID값 -> user.id 에 배당 하여 알람이 울린다.
 */
class Notification(private val context: Context) : BroadcastReceiver() {

    private lateinit var notificationManager :NotificationManager
    //알람이 울리면 이 메소드 호출
    override fun onReceive(context: Context?, intent: Intent?) {

    }
    fun createChannel(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channel= NotificationChannel(
                "1",
                "colorContactsNotificationChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            // NotificationManager에 Notification Channel을 등록
            notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }


    //Notification 생성 후 표시
    fun showNotification(user: User){

    }

}