package com.example.colorcontacts.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.colorcontacts.R
import com.example.colorcontacts.data.UserList
import com.example.colorcontacts.adapter.ViewPagerAdapter
import com.example.colorcontacts.data.ColorTheme
import com.example.colorcontacts.data.NowColor
import com.example.colorcontacts.ui.contactList.ContactListFragment
import com.example.colorcontacts.databinding.ActivityMainBinding
import com.example.colorcontacts.dialog.AddContactDialogFragment
import com.example.colorcontacts.test.TestActivity
import com.example.colorcontacts.utill.LayoutType
import com.example.colorcontacts.ui.dialpad.DialPadFragment
import com.example.colorcontacts.ui.favorite.FavoriteFragment
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
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
        UserList.notification.settingNotification(this)
        initView()

    }

    private fun initView() {
        setFragment()
        setOnQueryTextListener()
        setLayoutBtn()
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

        setDialog()
    }

    fun setDialog() {
        //플로팅 버튼(주소록 추가 다이얼로그)
        val currentItem = binding.viewPager.currentItem
//        val currentFragment = supportFragmentManager.fragments[currentItem]
        binding.btnAddContactDialog.setOnClickListener {
            AddContactDialogFragment().dismissListener =
                object : AddContactDialogFragment.DialogDismissListener {
                    override fun onDialogDismissed() {
//                    currentFragment.onResume()
                    }
                }
            AddContactDialogFragment().show(supportFragmentManager, "AddContactDialogFragment")
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


    /**
     * TODO 접근 권한 부여
     *
     * 연락처에 접근할 권한이 없을때 사용자에게 권한을 요청하는 역할
     */


    //상단 버튼
    private fun setLayoutBtn() {
        binding.ivMainLayout.setOnClickListener {
            onLayoutbtn()
        }

        binding.ivMainEdit.setOnClickListener {
            showColorSelection()
        }

        binding.testBtn.setOnClickListener {
            startActivity(Intent(this, TestActivity::class.java))
        }

    }

    private fun onLayoutbtn() {
        if (UserList.layoutType == LayoutType.LINEAR) {
            UserList.layoutType = LayoutType.GRID
            binding.ivMainLayout.setImageResource(R.drawable.ic_fragment_linear)
        } else {
            UserList.layoutType = LayoutType.LINEAR
            binding.ivMainLayout.setImageResource(R.drawable.ic_fragment_grid)
        }
        updateLayoutInCurrentFragment(UserList.layoutType)
    }

    private fun updateLayoutInCurrentFragment(layoutType: LayoutType) {
        val currentItem = binding.viewPager.currentItem
        when (val currentFragment = supportFragmentManager.fragments[currentItem]) {
            is ContactListFragment -> {
                currentFragment.dataChangedListener.onLayoutTypeChanged(layoutType)
                currentFragment.dataChangedListener.onLayoutType(layoutType)
            } // DataChangedListener
            is FavoriteFragment -> {
                currentFragment.dataChangedListener.onLayoutTypeChanged(layoutType)
                currentFragment.dataChangedListener.onLayoutType(layoutType)
            }
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
                setColor(colorType, color)
            }
            .show()
    }

    //선택한 값을 현재 객체로 저장, 라이브데이터를 갱신해 옵저빙하는 뷰들의 색을 갱신해줌
    private fun setColor(colorType: String, color: Int) {
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
        //싱글턴 데이터가 변경되는 구간
        setColors()
    }

    @SuppressLint("ResourceType")
    private fun setColors() {
        //View 색 변경
        val color = NowColor.color

        with(binding) {
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

        updateColorInCurrentFragment(NowColor.color)
    }

    private fun updateColorInCurrentFragment(color: ColorTheme) {
        val currentItem = binding.viewPager.currentItem
        when (val currentFragment = supportFragmentManager.fragments[currentItem]) {
            is ContactListFragment -> currentFragment.dataChangedListener.onColorChanged(color) // DataChangedListener
            is FavoriteFragment -> currentFragment.dataChangedListener.onColorChanged(color)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}

