package com.example.colorcontacts.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import com.example.colorcontacts.CheckString
import com.example.colorcontacts.databinding.DialogAddContactBinding

class AddContactDialog(context: Context) : Dialog(context) {
    private val binding by lazy { DialogAddContactBinding.inflate(layoutInflater) }

    //유효성 검사 체크 변수들
    private var isChecked = false
    private var isCheckedName = false
    private var isCheckedPhoneNum = false
    private var isCheckedEmail = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 화면 구성
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        // 콜백 버튼 구현
        setCallBackFunction()

        // 유효성 검사
        checkString()


    }

    private fun setCallBackFunction() {
        // Ok 버튼
        binding.btnAddContactOk.run {
            // 유효성 검사 전 확인 버튼 비활성화
            isEnabled = false
            setOnClickListener {


                // 데이터 전달


                // 종료
                dismiss()
            }
        }

        // 뒤로가기 버튼
        binding.ivAddContactBackBtn.setOnClickListener {
            //종료
            dismiss()
        }


    }

    private fun checkString() {
        // 유효성 검사
        val etName = binding.etAddContactName
        val etPhoneNum = binding.etAddContactPhoneNumber
        val etEmail = binding.etAddContactEmail

        val btnOk = binding.btnAddContactOk


        // 이름 유효성 검사
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //텍스트 바뀌기 전
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //텍스트 바뀌는 도중
                isCheckedName = CheckString().checkName(s.toString())
                if(!isCheckedName) etName.error = "한글이나 영문자만 가능합니다."
            }

            override fun afterTextChanged(s: Editable?) {
                //텍스트 바뀐후 수행
                isCheckedName = CheckString().checkName(s.toString())
                if(!isCheckedName) etName.error = "한글이나 영문자만 가능합니다."
                isChecked = isCheckedName && isCheckedEmail && isCheckedPhoneNum
                btnOk.isEnabled = isChecked
            }

        })

        //전화 번호 유효성 검사
        etPhoneNum.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //텍스트 바뀌기 전
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //텍스트 바뀌는 도중
                isCheckedPhoneNum = CheckString().checkPhoneNumber(s.toString())
                if(!isCheckedPhoneNum) etPhoneNum.error = "010-xxxx-xxxx 형태 이어야합니다."
            }

            override fun afterTextChanged(s: Editable?) {
                //텍스트 바뀐후 수행
                isCheckedPhoneNum = CheckString().checkPhoneNumber(s.toString())
                if(!isCheckedPhoneNum) etPhoneNum.error = "010-xxxx-xxxx 형태 이어야합니다."
                isChecked = isCheckedName && isCheckedEmail && isCheckedPhoneNum
                btnOk.isEnabled = isChecked
            }

        })

        //이메일 유효성 검사
        etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //텍스트 바뀌기 전
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //텍스트 바뀌는 도중
                isCheckedEmail = CheckString().checkEmail(s.toString())
                if(!isCheckedEmail) etEmail.error = "이메일 형식에 맞춰주세요."
            }

            override fun afterTextChanged(s: Editable?) {
                //텍스트 바뀐후 수행
                isCheckedEmail = CheckString().checkEmail(s.toString())
                if(!isCheckedEmail) etEmail.error = "이메일 형식에 맞춰주세요."
                isChecked = isCheckedName && isCheckedEmail && isCheckedPhoneNum
                btnOk.isEnabled = isChecked
            }

        })
    }


}