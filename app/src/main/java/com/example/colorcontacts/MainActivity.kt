package com.example.colorcontacts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.colorcontacts.contactList.ContactListFragment
import com.example.colorcontacts.databinding.ActivityMainBinding
import com.example.colorcontacts.dialog.AddContactDialog
import com.example.colorcontacts.dialpad.DialPadFragment
import com.example.colorcontacts.favorite.FavoriteFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val icons =
        listOf(
            R.drawable.ic_tablayout_favorite,
            R.drawable.ic_tablayout_user,
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
                    // TODO Tab에 텍스트 추가
                }
            })
        }

        // ViewPager TabLayout 연결
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setIcon(icons[position])
        }.attach()

        // Selected Listener
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // TODO Tab에 텍스트 추가
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

            override fun onTabReselected(tab: TabLayout.Tab?) = Unit

        })

    }
}