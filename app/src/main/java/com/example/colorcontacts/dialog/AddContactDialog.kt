package com.example.colorcontacts.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.example.colorcontacts.databinding.DialogAddContactBinding

class AddContactDialog(context : Context) : Dialog(context) {
    private val binding by lazy{DialogAddContactBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 화면 구성
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        // 콜백 버튼 구현
        setCallBackFunction()

        // 유효성 검사



    }
    private fun setCallBackFunction(){
        // Ok 버튼
        binding.btnAddContactOk.setOnClickListener {
            // 데이터 전달
            // 종료
            dismiss()
        }

        // 뒤로가기 버튼
        binding.ivAddContactBackBtn.setOnClickListener {
            //종료
            dismiss()
        }
    }


}