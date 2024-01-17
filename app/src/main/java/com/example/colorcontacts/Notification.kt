package com.example.colorcontacts


import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.colorcontacts.data.User


/**
 *  TODO : 알람 관련 클래스(Broadcast 수신)
 *  1. Notification 채널 설정(최초 한번)
 *  2. 알람 등록
 *  3. 알람 등록시 동작 정의 -> notifcation 생성하고 표시
 *
 */
class Notification : BroadcastReceiver() {


    companion object {
        private const val channelID = "1"
        private const val channelName = "컬러 컨텐트 알람"
        private var count = 0
    }

    //onCreate() 에서 등록 해야할것들
    private lateinit var alarmManager: AlarmManager
    private lateinit var notificationManager: NotificationManager


    fun settingNotification(context: Context) {
        setSystemService(context)
        createChannel()
    }

    private fun setSystemService(context: Context) {

        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    }


    // 최초 채널 생성
    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelID,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            // NotificationManager에 Notification Channel을 등록
            notificationManager.createNotificationChannel(channel)

        }
    }


    // 알람이 울리면 이 메소드 호출
    override fun onReceive(context: Context?, intent: Intent?) {
        val name = intent?.getStringExtra("NAME")
        val event = intent?.getStringExtra("EVENT")
        val builder = NotificationCompat.Builder(context!!, channelID)
        setSystemService(context)
        builder.run {
            setSmallIcon(R.drawable.ic_logo)
            setContentTitle(name + "님의 알림")
            setContentText(name + "에 설정한 시간 : ${event}가 되었습니다")
            setPriority(NotificationCompat.PRIORITY_DEFAULT)//알람 중요도
        }
        notificationManager.notify(count++, builder.build())


    }

    // user 인스턴스 의 알람 등록
    fun setUserAlarm(user: User, context: Context) {
        val alarmTimeMillis = parseTimeMillis(user.event.toString())


        val alarmIntent = Intent(context, Notification::class.java)
        alarmIntent.putExtra("NAME", user.name)
        alarmIntent.putExtra("EVENT", user.event)
        //나중에 실행하는 작업
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            count, //pendingIntent 를 식별하는 코드
            alarmIntent,// 알람이 트리거 될때 실행되는 intent
            PendingIntent.FLAG_MUTABLE // 생성된 PendingIntent 는 가변성이다.
        )

        alarmManager.setExactAndAllowWhileIdle( // 정확한 시간에 알람이 발생하는 메소드
            AlarmManager.RTC_WAKEUP,//실시간 시계 시스템 기반으로 하는 알람을 설정
            alarmTimeMillis, // 지정된 시간에 알람이 발생
            pendingIntent
        )
    }


    //등록된 문자열에 따른 millisecond 단위로 변환
    private fun parseTimeMillis(event: String): Long {
        //1초 5초 1분 10분 1시간
        return when (event) {
            "1초" -> 1000
            "5초" -> 5 * 1000
            "1분" -> 60 * 1000
            "10분" -> 600 * 1000
            "1시간" -> 3600 * 1000
            else -> 0
        }

    }

}