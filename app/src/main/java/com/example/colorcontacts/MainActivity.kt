package com.example.colorcontacts

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.colorcontacts.contactList.ContactListFragment
import com.example.colorcontacts.databinding.ActivityMainBinding
import com.example.colorcontacts.dialog.AddContactDialog
import com.example.colorcontacts.dialpad.DialPadFragment
import com.example.colorcontacts.favorite.FavoriteFragment
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val icons =
        listOf(
            R.drawable.ic_tablayout_favorite_all,
            R.drawable.ic_tablayout_contact_all,
            R.drawable.ic_tablayout_dialpad
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()


        //임의의 다이얼로그 버튼(디버그용)
        binding.button.setOnClickListener {
            AddContactDialog(this).show()
        }
    }

    private fun initView() {
        requestContactPermission()


        // ViewPager Adapter 생성
        val viewPagerAdapter = ViewPagerAdapter(this@MainActivity)
        viewPagerAdapter.addFragment(FavoriteFragment())
        viewPagerAdapter.addFragment(ContactListFragment())
        viewPagerAdapter.addFragment(DialPadFragment())

        // Adapter 연경
        binding.viewPager.apply {
            adapter = viewPagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
        }

        // ViewPager TabLayout 연결
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setIcon(icons[position])
        }.attach()

    }

    /**
     * TODO 연락처에서 유저 정보 가져오기
     *
     * ContactsContract를 이용
     * Manifest에서 <uses-permission android:name="android.permission.READ_CONTACTS" />를 선언
     * 사용자의 갤러리에 접근할 수 있게한다
     */
    @SuppressLint("Range")
    private fun getContacts() {
        //연락처 URI 가져오기
        val contactsUri = ContactsContract.Contacts.CONTENT_URI

        //URI 데이터 읽어 오고 데이터의 집합을 가리키는 객체 cursor선언
        val cursor = contentResolver.query(contactsUri, null, null, null, null)

        cursor?.use { cursor ->
            while (cursor.moveToNext()) {
                val id =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))

                val name = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                )

                //전화 번호랑 이메일은 여러개의 전화번호나 이메일을 가질 수 있기때문에 별도로 처리
                var phoneNumber: String? = null
                if (cursor.getInt(
                        cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                    ) > 0
                ) {
                    val phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id), null
                    )
                    phoneCursor?.use {
                        if (it.moveToFirst()) {
                            phoneNumber = it.getString(
                                it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            )
                        }
                    }
                }

                var email: String? = null
                val emailCursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                    arrayOf(id), null
                )
                emailCursor?.use {
                    if (it.moveToFirst()) {
                        email = it.getString(
                            it.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
                        )
                    }
                }

                var profileImageUri: Uri? = null
                val photoUri = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)
                )
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
                    favorites = false
                )

                //데이터 객체로 추가
                UserList.userList.add(user)
            }
        }
    }

    /**
     * TODO 접근 권한 부여
     *
     * 연락처에 접근할 권한이 없을때 사용자에게 권한을 요청하는 역할
     */
        private fun requestContactPermission() {
            //사용자가 퍼미션 허용 했는지 확인
            val status = ContextCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS")
            if (status == PackageManager.PERMISSION_GRANTED) getContacts()
            else {
                //퍼미션 요청 다이얼로그 표시
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf("android.permission.READ_CONTACTS"),
                    100
                )
            }
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

    }