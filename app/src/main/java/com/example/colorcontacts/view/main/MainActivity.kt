package com.example.colorcontacts.view.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.colorcontacts.Notification
import com.example.colorcontacts.R
import com.example.colorcontacts.data.User
import com.example.colorcontacts.data.UserList
import com.example.colorcontacts.adapter.ViewPagerAdapter
import com.example.colorcontacts.data.NowColor
import com.example.colorcontacts.view.contactList.ContactListFragment
import com.example.colorcontacts.databinding.ActivityMainBinding
import com.example.colorcontacts.dialog.AddContactDialogFragment
import com.example.colorcontacts.test.TestActivity
import com.example.colorcontacts.utill.LayoutType
import com.example.colorcontacts.utill.SharedViewModel
import com.example.colorcontacts.view.dialpad.DialPadFragment
import com.example.colorcontacts.view.favorite.FavoriteFragment
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: SharedViewModel by lazy {
        ViewModelProvider(this)[SharedViewModel::class.java]
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

        UserList.notification.settingNotification(this)

        initView()

    }
    private fun initView() {
        requestContactPermission()
        getContacts()
        setFragment()
        setOnQueryTextListener()
        setLayoutBtn()
        setObserve()
    }

    private fun setFragment() {
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
                    binding.toolBar.visibility = if (position == 2) View.GONE else View.VISIBLE
                }
            })
        }

        // ViewPager TabLayout 연결
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setIcon(icons[position])
        }.attach()

        //플로팅 버튼(주소록 추가 다이얼로그)
        binding.btnAddContactDialog.setOnClickListener {
            AddContactDialogFragment().show(supportFragmentManager,"AddContactDialogFragment")
        }
    }

    /**
     * TODO 검색 기능
     */
    private fun setOnQueryTextListener() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                updateItemCurrentFragment(newText)
                return false
            }
        })
    }

    private fun updateItemCurrentFragment(newText: String?) {
        if (newText == null) {
            return
        }
        val currentItem = binding.viewPager.currentItem
        when (val currentFragment = supportFragmentManager.fragments[currentItem]) {
            is FavoriteFragment -> currentFragment.updateItem(newText.trim())
            is ContactListFragment -> currentFragment.updateItem(newText.trim())
        }
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
        UserList.userList = mutableListOf()
        //연락처 URI 가져오기
        UserList.userList = mutableListOf()
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
                    info = null
                )

                //데이터 객체로 추가
                UserList.userList.add(user)
            }
        }
        UserList.userList.sortBy { it.name }
        setFragment()
    }

    /**
     * TODO 접근 권한 부여
     *
     * 연락처에 접근할 권한이 없을때 사용자에게 권한을 요청하는 역할
     */
    private fun requestContactPermission() {
        //연락처 퍼미션, 사용자가 퍼미션 허용 했는지 확인
        val status =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
        if (status == PackageManager.PERMISSION_GRANTED)
        else {
            //퍼미션 요청 다이얼로그 표시
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                100
            )
            binding.pbMainLoading.isVisible = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        binding.pbMainLoading.visibility = View.GONE
        when (requestCode) {
            100 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 연락처 권한이 허용된 경우, 전화 권한 요청
                    requestCallPermission()
                } else {
                    // 연락처 권한이 거부된 경우, 앱 종료
                    finish()
                }
            }

            55 -> {
                if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) finish()
                else {
                    getContacts()
                    setFragment()
                }
            }
        }
    }

    //상단 버튼
    @SuppressLint("ResourceType")
    private fun setLayoutBtn() {
        binding.ivMainLayout.setOnClickListener {
            viewModel.getLayoutType()
        }

        binding.ivMainEdit.setOnClickListener {
            showColorSelection()
        }

        binding.testBtn.setOnClickListener {
            startActivity(Intent(this, TestActivity::class.java))
        }
    }

    /**
     * TODO 테마색 변경 다이얼로그
     *
     * 화면 상단의 edit아이콘을 눌렀을때 작동
     * 다이얼로그가 뜨며 바꿀 뷰의 색(위젯, 폰트컬러등)을 컬러피커를 이용해 선택하고
     * 뷰를 갱신한다
     */
    private fun showColorSelection() {
        val colorType = arrayOf(
            getString(R.string.main_color_widget),
            getString(R.string.main_color_search),
            getString(R.string.main_color_icon),
            getString(R.string.main_color_font),
            getString(R.string.main_color_basic),
            getString(R.string.main_color_select),
            getString(R.string.main_color_list),
            getString(R.string.main_color_header),
            getString(R.string.main_color_back)
        )
        //색을 변경할 항목을 선택하고 컬러피커 다이얼로그 호출
        AlertDialog.Builder(this).setTitle(R.string.main_dialog_title)
            .setItems(colorType) { _, position ->
                showColorPicker(colorType[position])
            }.show()
    }

    /**
     * TOOD 컬러피커 다이얼로그
     *
     * 스펙트럼표로 색을 고를수 있음
     * 타입에 맞춰 객체로 저장
     *
     * module이랑 project Setting gradle설정 해야함
     */
    private fun showColorPicker(colorType: String) {
        var nowColor = when (colorType) {
            getString(R.string.main_color_widget) -> NowColor.color.colorWidget
            getString(R.string.main_color_search) -> NowColor.color.colorSearch
            getString(R.string.main_color_icon) -> NowColor.color.colorIcon
            getString(R.string.main_color_font) -> NowColor.color.colorFont
            getString(R.string.main_color_basic) -> NowColor.color.colorBasic
            getString(R.string.main_color_select) -> NowColor.color.colorSelect
            getString(R.string.main_color_list) -> NowColor.color.colorLinear
            getString(R.string.main_color_header) -> NowColor.color.colorHeader
            else -> NowColor.color.colorBackground
        }
        ColorPickerDialog.Builder(this).setTitle(colorType)
            .setColorShape(ColorShape.SQAURE)   // Default ColorShape.CIRCLE
            .setDefaultColor(nowColor)     // Pass Default Color
            .setColorListener { color, _ ->
                setColor(colorType,color)
            }
            .show()
    }

    //선택한 값을 현재 객체로 저장, 라이브데이터를 갱신해 옵저빙하는 뷰들의 색을 갱신해줌
    private fun setColor(colorType: String, color: Int){
        when (colorType) {
            getString(R.string.main_color_widget) -> NowColor.color.colorWidget = color
            getString(R.string.main_color_search) -> NowColor.color.colorSearch = color
            getString(R.string.main_color_icon) -> NowColor.color.colorIcon = color
            getString(R.string.main_color_font) -> NowColor.color.colorFont = color
            getString(R.string.main_color_basic) -> NowColor.color.colorBasic = color
            getString(R.string.main_color_select) -> NowColor.color.colorSelect = color
            getString(R.string.main_color_list) -> NowColor.color.colorLinear = color
            getString(R.string.main_color_header) -> NowColor.color.colorHeader = color
            else -> NowColor.color.colorBackground = color
        }
        viewModel.setColor()
    }

    @SuppressLint("ResourceType")
    private fun setObserve() {
        viewModel.layoutType.observe(this) { type ->
            if (type == LayoutType.GRID) binding.ivMainLayout.setImageResource(R.drawable.ic_fragment_linear)
            else binding.ivMainLayout.setImageResource(R.drawable.ic_fragment_grid)
        }

        //View 색 변경
        viewModel.color.observe(this) {color ->
            with(binding){
                tabLayout.background.setTint(color.colorWidget)
                searchView.background.setTint(color.colorSearch)
                ivMainLayout.setColorFilter(color.colorIcon)
                ivMainEdit.setColorFilter(color.colorIcon)
                csMainHeader.background.setTint(color.colorHeader)
                csMainBackground.setBackgroundColor(color.colorBackground)
                btnAddContactDialog.background.setTint(color.colorWidget)
                btnAddContactDialog.setTextColor(color.colorBasic)
            }
            window.statusBarColor = color.colorWidget
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun requestCallPermission() {
        val callPermission =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
        if (callPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CALL_PHONE),
                55
            )
            binding.pbMainLoading.isVisible = true
        }
    }
}