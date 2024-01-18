package com.example.colorcontacts.utill


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
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.colorcontacts.R
import com.example.colorcontacts.data.EventTime
import com.example.colorcontacts.data.User
import kotlin.random.Random


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
    }

    //onCreate() 에서 등록 해야할 것 들
    private lateinit var alarmManager: AlarmManager
    private lateinit var notificationManager: NotificationManager


    fun settingNotification(activity: Activity) {
        requestPermissionNotification(activity)
        setSystemService(activity)
        createChannel()
    }


    //권한 확인후 실행 해야함
    private fun setSystemService(context: Context) {
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    }

    //권한 확인 이 놈이 문제네
    private fun requestPermissionNotification(activity: Activity) {
        val status =
            ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.SCHEDULE_EXACT_ALARM
            )
        if (status == PackageManager.PERMISSION_GRANTED)
        else {
            //퍼미션 요청 다이얼로그 표시
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    android.Manifest.permission.SCHEDULE_EXACT_ALARM,
                    android.Manifest.permission.USE_EXACT_ALARM,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ),
                101
            )
        }
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

        //인텐트 값 받아오기
        val name = intent?.getStringExtra("NAME")
        val event = intent?.getStringExtra("EVENT")
        val randomCode = intent?.getIntExtra("RANDOM_CODE", 0) ?: 0

        //서비스 쪽에서 불러질 때 서비스 등록
        context?.let {context ->
            setSystemService(context)

        //알림 설정
        val builder = NotificationCompat.Builder(context, channelID)
        builder.run {
            setSmallIcon(R.drawable.ic_logo)
            setContentTitle(name + "님의 알림")
            setContentText(name + "에 설정한 시간 : ${event}이 되었습니다")
            setPriority(NotificationCompat.PRIORITY_DEFAULT)//알람 중요도
        }

        notificationManager.notify(randomCode, builder.build())
        }
    }

    // user 인스턴스 의 알람 등록
    fun setUserAlarm(user: User, context: Context) {

        //데이터 값에 맞게 파싱
        val currentTimeMillis = System.currentTimeMillis()
        val alarmTimeMillis = currentTimeMillis + parseTimeMillis(user.event.toString())
        //requestCode 생성

        val randomCode = Random(1000000).nextInt()

        //onReceive 에 불려질 인텐트 데이터
        val alarmIntent = Intent(context, Notification::class.java)
        alarmIntent.putExtra("NAME", user.name)
        alarmIntent.putExtra("EVENT", user.event)
        alarmIntent.putExtra("RANDOM_CODE", randomCode)

        //알람을 등록하기 위한 인텐트 생성
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            randomCode, //pendingIntent 를 식별하는 코드
            alarmIntent,// 알람이 트리거 될때 실행되는 intent
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            // 생성된 PendingIntent 는 불가변성 이다. 그리고 매번 업데이트를 해준다고 플래그를 전달
        )

        //매니저에게 해당시간이후에 울리도록 설정

        alarmManager.setExactAndAllowWhileIdle( // 정확한 시간에 알람이 발생하는 메소드
            AlarmManager.RTC_WAKEUP,//실시간 시계 시스템 기반으로 하는 알람을 설정
            alarmTimeMillis, // 지정된 시간에 알람이 발생
            pendingIntent
        )
    }


    //등록된 문자열에 따른 millisecond 단위로 변환
    private fun parseTimeMillis(event: String): Long {
        //5초 10초 1분 10분 1시간
        return when (event) {
            EventTime.timeArray[0] -> 5 * 1000
            EventTime.timeArray[1] -> 10 * 1000
            EventTime.timeArray[2] -> 60 * 1000
            EventTime.timeArray[3] -> 600 * 1000
            EventTime.timeArray[4] -> 3600 * 1000
            else -> 0
        }

    }

}