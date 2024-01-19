# Notification

### 알람 이벤트를 등록하여 지정된 시간에 알림(Notification) 을 보여주는 기능
![알림](https://github.com/Ohleesang/TIL/assets/148442711/13e60366-9498-4b30-a395-71bcdcf40ce3)



# 설계
```kotlin
class Notification : BroadcastReceiver()

fun settingNotification(activity: Activity)
private fun setSystemService(context: Context)
private fun requestPermissionNotification(activity: Activity)
private fun createChannel()

override fun onRecive(context: Context?,intent: Intent?)

fun setUserAlarm(user:User,context: Context)
private fun parseTimeMillis(event: String): Long

```
### 1. 권한 확인 후 처리
### 2. Notifitcation 채널 등록 (최초 한번)
### 3. 알람 등록
### 4. 알람 등록시 동작 정의
### 5. notification을 생성하고 표시

#### 알람(Alarm) 을 등록하고, 알림(Notification)을 동작하게 한다!
그전에 권한을 설정해야하고, 최초로 채널을 등록하고 그 한 채널에서 여러번의 알림을 동작할 수 있게 구현하였다.

### 알람의 경우, 현재시간 이후 몇초(몇분) 으로 구성해두었다.
```kotlin
object EventTime{
    val timeArray = arrayOf("X","10초","1분","10분","1시간")
}
```

# 사용 

### fun settingNotification(activity: Activity)
- 권한 확인을 하고, 최초로 채널을 등록하는 메소드
- MainActivity 실행시, 최초 한번 실행되면 된다.
### fun setUserAlarm(user:User,context:Context)
- 해당 유저의 알람을 등록하고 싶을경우
- 유저가 생성되거나 유저의 정보가 변경되었을때, 이때 알람을 줘야할때 사용하면된다.

# 구현

## 1. 권한 확인 후 처리

```kotlin
private lateinit var alarmManager: AlarmManager
private lateinit var notificationManager: NotificationManager

fun settingNotification(activity: Activity) {
        requestPermissionNotification(activity)
        setSystemService(activity)

        ...

    }
```
A. AlarmManager , NotificationManger 를 사용하기위해 권한이 필요하다.

B. 권한의 경우 Activity 내에서 직접 처리해야하므로 Activity 인자값을 받아들인다

## 2. Notifitcation 채널 등록
```kotlin
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
```
한 채널 에서 여러 알림을 처리하므로 최초 한번 채널을 등록한다.

## 3. 알람 등록
```kotlin
fun setUserAlarm(user: User, context: Context)
```
### 해당 User 의 Event 값이 있다면 이 함수를 불러들어 알람을 등록하는 함수

#### 2개의 Intent 를 정의하고 알람 서비스에 전달한다.
```kotlin
    
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
```
#### 일정 시간 이후 알람이 울리도록 등록하였다.
```kotlin
//매니저에게 해당시간이후에 울리도록 설정

alarmManager.setExactAndAllowWhileIdle( // 정확한 시간에 알람이 발생하는 메소드
    AlarmManager.RTC_WAKEUP,//실시간 시계 시스템 기반으로 하는 알람을 설정
    alarmTimeMillis, // 지정된 시간에 알람이 발생
    pendingIntent
)
```

## 4. 알람 등록시 동작 정의

```kotlin
override fun onReceive(context: Context?, intent: Intent?)
``` 
### 알람(Alarm)이 울리면 발생하는 메소드
- 이때, 해당 intent를 받아 알림(Notifitcation)을 등록한다.

## 5. notification을 생성하고 표시

A. 서비스 쪽에서 불러질때, 서비스를 등록한다.
```kotlin
//서비스 쪽에서 불러질 때 서비스 등록
        context?.let {context ->
            setSystemService(context)
```
B. 이후 알림을 설정하여 고유한값의 알림을 생성한다.
```kotlin
    //알림 설정
    val builder = NotificationCompat.Builder(context, channelID)
        builder.run {
            setSmallIcon(R.drawable.ic_logo)
            setContentTitle(name + "님의 알림")
            setContentText(name + "에 설정한 시간 : ${event}이 되었습니다")
            setPriority(NotificationCompat.PRIORITY_DEFAULT)//알람 중요도
        }

    notificationManager.notify(randomCode, builder.build())
```
