Splash 인트로페이지
=
[Readme로 돌아가기](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/README.md)

# Layout

![image](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/assets/116724657/1f788526-2de6-43fd-9fa8-1d1c3a43ef33)

애니메이션을 구성하는 요소로

타이틀 + 타이틀을 꾸며줄 Spinkit

뒤에 배경으로 넣을 스카이라인이랑

남성과 여성, 그리고 스와이프를 표현할 바로 구성되어있고

사용자한테서 연락처를 받고 읽는동안 화면을 채워줄 프로그래스바가 있다

<details>
<summary> 코드보기 </summary>

```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.motion.widget.MotionLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/intro_motion"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/white"
    app:layoutDescription="@xml/activity_intro_scene">

    <ImageView
        android:id="@+id/iv_intro_skyline"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:src="@drawable/img_intro_skyline"
        android:alpha="0"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.8"
        android:contentDescription="TODO" />

    <ProgressBar
        android:id="@+id/pb_intro_loading"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/tv_intro_title"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:fontFamily="@font/hakgyoansimwoojur"
        android:text="Color\n Contatcs"
        android:textColor="@color/black"
        android:textSize="56sp"
        android:textStyle="bold"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.2" />

    <com.github.ybq.android.spinkit.SpinKitView
        android:id="@+id/pg_intro_textred"
        style="@style/SpinKitView.Large.FadingCircle"
        android:layout_width="20sp"
        android:layout_height="20sp"
        app:SpinKit_Color="@color/red"
        android:alpha="0"
        app:layout_constraintBottom_toBottomOf="@+id/tv_intro_title"
        app:layout_constraintEnd_toEndOf="@+id/tv_intro_title"
        app:layout_constraintHorizontal_bias="0.121"
        app:layout_constraintStart_toStartOf="@+id/tv_intro_title"
        app:layout_constraintTop_toTopOf="@+id/tv_intro_title"
        app:layout_constraintVertical_bias="0.26" />

    <com.github.ybq.android.spinkit.SpinKitView
        android:id="@+id/pg_intro_textyellow"
        android:layout_width="20sp"
        android:layout_height="20sp"
        style="@style/SpinKitView.Large.FadingCircle"
        app:SpinKit_Color="@color/yellow_700"
        android:rotation="70"
        android:alpha="0"
        app:layout_constraintBottom_toBottomOf="@+id/tv_intro_title"
        app:layout_constraintEnd_toEndOf="@+id/tv_intro_title"
        app:layout_constraintHorizontal_bias="0.545"
        app:layout_constraintStart_toStartOf="@+id/tv_intro_title"
        app:layout_constraintTop_toTopOf="@+id/tv_intro_title"
        app:layout_constraintVertical_bias="0.14" />

    <com.github.ybq.android.spinkit.SpinKitView
        android:id="@+id/pg_intro_textyblue"
        android:layout_width="20sp"
        android:layout_height="20sp"
        style="@style/SpinKitView.Large.FadingCircle"
        app:SpinKit_Color="@color/blue_600"
        android:rotation="120"
        android:alpha="0"
        app:layout_constraintBottom_toBottomOf="@+id/tv_intro_title"
        app:layout_constraintEnd_toEndOf="@+id/tv_intro_title"
        app:layout_constraintHorizontal_bias="1.028"
        app:layout_constraintStart_toStartOf="@+id/tv_intro_title"
        app:layout_constraintTop_toTopOf="@+id/tv_intro_title"
        app:layout_constraintVertical_bias="0.78" />

    <ImageView
        android:id="@+id/iv_intro_actionbar2"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:scaleX="-1"
        android:src="@drawable/img_intro_actionbar"
        app:layout_constraintBottom_toTopOf="@id/iv_intro_actionbar1"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintWidth_percent="1.2" />

    <LinearLayout
        android:id="@+id/linear1"
        android:layout_width="0dp"
        android:layout_height="1dp"
        android:orientation="horizontal"
        app:layout_constraintBottom_toBottomOf="@id/iv_intro_actionbar2"
        app:layout_constraintStart_toStartOf="@id/iv_intro_actionbar2"
        app:layout_constraintTop_toTopOf="@id/iv_intro_actionbar2" />

    <ImageView
        android:id="@+id/iv_intro_male"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_marginStart="30dp"
        android:src="@drawable/img_intro_male"
        app:layout_constraintBottom_toTopOf="@+id/linear1"
        app:layout_constraintHeight_percent="0.27"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintWidth_percent="0.2"
        android:contentDescription="TODO" />

    <ImageView
        android:id="@+id/iv_intro_actionbar1"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:src="@drawable/img_intro_actionbar"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintWidth_percent="1.2" />

    <LinearLayout
        android:id="@+id/linear2"
        android:layout_width="0dp"
        android:layout_height="1dp"
        android:layout_marginTop="14dp"
        android:orientation="horizontal"
        app:layout_constraintBottom_toBottomOf="@id/iv_intro_actionbar1"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="@id/iv_intro_actionbar1" />

    <ImageView
        android:id="@+id/iv_intro_female"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_marginEnd="30dp"
        android:src="@drawable/img_intro_female"
        app:layout_constraintBottom_toTopOf="@+id/linear2"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHeight_percent="0.30"
        app:layout_constraintWidth_percent="0.2" />

    <TextView
        android:id="@+id/tv_intro_touch"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="화면을 터치해 주세요"
        android:textSize="24sp"
        android:textStyle="bold"
        android:alpha="0.6"
        android:layout_centerInParent="true"
        app:layout_constraintVertical_bias="0.85"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"/>
</androidx.constraintlayout.motion.widget.MotionLayout>
```
</details>

xml은 이렇게 짜여있는데 애니메이션을 수행하기 위해
모션 레이아웃을 사용했다 

타이틀을 꾸며줄 spinkit은 조금만 위치가 틀어져도 모양이 뭉게질 수가 있어서

bias로 위치 조절하고 title의 상하좌우에 연결해서 다양한 화면 구성에서도 모양이 뭉게지지 않게 했다

![image](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/assets/116724657/cf071377-d572-4242-a2f1-593d05e4e190)

애니메이션은 이런 식으로 디자인 툴을 사용했는데

수행해야할 동작은 처음실행했을때 딱한번만 움직이면 되므로 트랜지션을 하나만 만들어 줬고

KeyAttribute로 상태값, 투명도를 조절

KeyPostion으로 움직임을 조절해서 구성했다.


# IntroActivity
<details>
<summary>IntroActivity.kt</summary>

```kotlin
package com.example.colorcontacts.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.colorcontacts.R
import com.example.colorcontacts.data.User
import com.example.colorcontacts.data.UserList
import com.example.colorcontacts.databinding.ActivityIntroBinding
import com.example.colorcontacts.ui.main.MainActivity

@Suppress("DEPRECATION")
class IntroActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityIntroBinding.inflate(layoutInflater)
    }
    private var contactsLoaded = false

    private val blick by lazy {
        AnimationUtils.loadAnimation(this, R.anim.blink)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        supportActionBar?.hide()

        requestContactPermission()

        binding.introMotion.setOnClickListener {
            binding.introMotion.transitionToEnd()
            startActivity(Intent(this@IntroActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun goMain() {
        binding.introMotion.setTransitionDuration(4000)
        binding.introMotion.transitionToEnd()

        binding.introMotion.setOnClickListener {
            startActivity(Intent(this@IntroActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun startMotion() {
        binding.introMotion.apply {
            transitionToStart()
            binding.tvIntroTouch.startAnimation(blick)
            Handler(Looper.getMainLooper()).postDelayed({
                goMain()
            }, 0)
        }
    }

    private fun requestContactPermission() {
        val status =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
        if (status == PackageManager.PERMISSION_GRANTED) {
            loadContacts()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                100
            )
        }


    }

    private fun requestCallPermission() {
        val callPermission =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
        if (callPermission == PackageManager.PERMISSION_GRANTED) {
            if (contactsLoaded) {
                startMotion()
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CALL_PHONE),
                55
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadContacts()
                } else {
                    finish()
                }
            }

            55 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (contactsLoaded) startMotion()
                } else {
                    finish()
                }

            }
        }
    }

    private fun loadContacts() {
        Thread {
            getContacts()
            runOnUiThread {
                contactsLoaded = true
                requestCallPermission()
            }
        }.start()
    }


    @SuppressLint("Range")
    private fun getContacts() {
        UserList.userList = mutableListOf()
        val contactsUri = ContactsContract.Contacts.CONTENT_URI
        val cursor = contentResolver.query(contactsUri, null, null, null, null)

        cursor?.use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                var phoneNumber: String? = null
                var email: String? = null
                var profileImageUri: Uri? = null

                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    phoneCursor?.use {
                        if (it.moveToFirst()) {
                            phoneNumber =
                                it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        }
                    }
                }

                val emailCursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", arrayOf(id), null
                )
                emailCursor?.use {
                    if (it.moveToFirst()) {
                        email =
                            it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
                    }
                }

                val photoUri =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))
                if (photoUri != null) {
                    profileImageUri = Uri.parse(photoUri)
                }

                val user = User(
                    img = profileImageUri,
                    name = name ?: "Unknown",
                    phone = phoneNumber ?: "No Phone",
                    email = email ?: "No Email",
                    event = null,
                    info = null,
                    backgroundImg = null
                )
                UserList.userList.add(user)
            }
        }
        UserList.userList.sortBy { it.name }
    }
}

```

</details>

인트로화면에서 수행하는 동작은 3가지가 있다

1. 사용자한테 연락처, 전화 등 권한 요청하기
2. 연락처 권한이 수락됬을때 데이터 불러오고 객체에 저장하기
3. 모션 레이아웃 애니메이션 수행하고 화면 터치시 메인페이지로 전환하기

## Permission 요청

<details>
<summary> AndroidManifest.xml </summary>

```xml
    <uses-permission android:name="android.permission.READ_CONTACTS" />
    <uses-permission android:name="android.permission.CALL_PHONE" />
```
</details>


<details>
<summary> 권한 요청 코드 </summary>

```kotlin

    private fun requestContactPermission() {
        val status =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
        if (status == PackageManager.PERMISSION_GRANTED) {
            loadContacts()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                100
            )
            binding.pbIntroLoading.isVisible = true
        }


    }

    private fun requestCallPermission() {
        val callPermission =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
        if (callPermission == PackageManager.PERMISSION_GRANTED) {
            if (contactsLoaded) {
                startMotion()
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CALL_PHONE),
                55
            )
        }
    }

```
</details>

매니페스트에서 설정을 해주고

권한을 요청하는 함수 requestContactPermission(), requestCallPermission()를 만들었다

현재 상태를 나타내는 함수를 만들었고

연락처가 허용되어있다면 데이터를 로드 그렇지 않다면 requestPermissions를 호출해 사용자에게 권한을 요청하게 했다

```kotlin
override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadContacts()
                } else {
                    finish()
                }
            }

            55 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (contactsLoaded) startMotion()
                } else {
                    finish()
                }

            }
        }
    }
```
퍼미션을 호출한뒤 결과를 받고

연락처가 허용되면 연락처 데이터를 불러오고

전화까지 허용되면 모션 레이아웃을 실행하면서 메인액티비티로 갈 수 있게한다

## 연락처 데이터 불러오기
<details>
<summary> getContacts </summary>

```kotlin
    private fun getContacts() {
        UserList.userList = mutableListOf()
        val contactsUri = ContactsContract.Contacts.CONTENT_URI
        val cursor = contentResolver.query(contactsUri, null, null, null, null)

        cursor?.use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                var phoneNumber: String? = null
                var email: String? = null
                var profileImageUri: Uri? = null

                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    phoneCursor?.use {
                        if (it.moveToFirst()) {
                            phoneNumber =
                                it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        }
                    }
                }

                val emailCursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", arrayOf(id), null
                )
                emailCursor?.use {
                    if (it.moveToFirst()) {
                        email =
                            it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
                    }
                }

                val photoUri =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))
                if (photoUri != null) {
                    profileImageUri = Uri.parse(photoUri)
                }

                val user = User(
                    img = profileImageUri,
                    name = name ?: "Unknown",
                    phone = phoneNumber ?: "No Phone",
                    email = email ?: "No Email",
                    event = null,
                    info = null,
                    backgroundImg = null
                )
                UserList.userList.add(user)
            }
        }
        UserList.userList = MyData.sortContacts(UserList.userList).toMutableList()
    }
```
</details>
contactUri를 이용해서 기기의 연락처 데이터를 가져왔다

일단 UserList.userList는 연락처 인원들의 데이터를 가지고 있는 싱글턴 객체인데

mutableListOf<User>타입으로 이루어져 있다

ContactsContract.Contacts.CONTENT_URI를 사용해서 연락처 데이터의 URI를 가져오고

contentResolver.query()로 URI를 넣어주고, 모든 열, 필터링X, 정렬부분은 따로 처리를 해주기때문에 Null로 설정

데이터베이스에서 읽어온 데이터는 Cursor 객체에 저장되는데 

Cursor객체는 데이터베이스의 각행에 순차적으로 접근할 수 있게 해준다

순회하면서 각 연락처의 ID, 이름, 전화번호, 이메일, 프로필 이미지 URI를 읽어 온다

읽어온 값들을 User객체로 생성하고 전체 정보를 가지고 있는 UserList.UserList에 추가한다

그런다음 연락처를 정렬해준다.

## Motion Layout

<details>
<summary> IntroActivity.kt </summary>

```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        supportActionBar?.hide()

        requestContactPermission()

        binding.introMotion.setOnClickListener {
            binding.introMotion.transitionToEnd()
            startActivity(Intent(this@IntroActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun goMain() {
        binding.introMotion.setTransitionDuration(4000)
        binding.introMotion.transitionToEnd()

        binding.introMotion.setOnClickListener {
            startActivity(Intent(this@IntroActivity, MainActivity::class.java))
            finish()
        }
    }
```
</details>



모션레이아웃에서 우여곡절이 많았는데

transitionToStart를 해도 실행이 안되고 end때 점프하면서 확 전환하게 되는 문제가 있었다

코드를 일일히 찾아 봤는데

transitionToEnd전에 setTransitionDuration으로 길이를 설정해주고 실행하니 잘 됬다

인트로 이미지가 나오는 동안 답답함을 느낄 수 있다 생각해

그냥 화면을 터치하면 바로 넘어가게끔 만들었다.
