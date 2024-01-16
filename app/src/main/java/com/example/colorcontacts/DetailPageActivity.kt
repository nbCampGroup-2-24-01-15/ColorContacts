package com.example.colorcontacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.colorcontacts.databinding.ActivityDetailPageBinding

class DetailPageActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetailPageBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //내 연락처인지 아닌지? 어케비교하지 왜 에뮬레이터에서는 따로 구분이 안되어잇지

        //안드로이드의 ContentProvider를 사용
        //사용자의 개인정보 보호를 위해 안드로이드 시스템은 기기의 전화번호에 대한 접근을 기본적으로 제한
        //일부 경우에서는 TelephonyManager API를 이용해 전화번호를 가져올 수 있지만,
        // 이는 통신사나 기기 설정에 따라 동작하지 않을 수 있습니다.
        // 또한, 이 기능을 사용하려면 READ_PHONE_STATE 권한을 요청해야 하며,
        // 이는 사용자에게 민감한 권한 요청으로 표시됩니다.

//        if ()

        binding.ivDetailEdit.setOnClickListener {

        }

    }
}