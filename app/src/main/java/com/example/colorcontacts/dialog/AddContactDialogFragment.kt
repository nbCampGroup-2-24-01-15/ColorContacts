package com.example.colorcontacts.dialog


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.DialogFragment
import com.example.colorcontacts.CheckString
import com.example.colorcontacts.databinding.DialogAddContactBinding

class AddContactDialogFragment(): DialogFragment() {
    private val binding by lazy { DialogAddContactBinding.inflate(layoutInflater) }

    //유효성 검사 체크 변수들
    private var isChecked = false
    private var isCheckedName = false
    private var isCheckedPhoneNum = false
    private var isCheckedEmail = false


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var dialog = Dialog(requireContext())
        dialog.setContentView(binding.root)

        // 콜백 버튼 구현
        setCallBackFunction()

        // 유효성 검사 리스너 등록
        checkString()

        return dialog
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

        // 이미지 클릭
        binding.ivAddContactProfileImg.setOnClickListener {
            openGallery()
        }


    }


    /**
     *  TODO : 유효성 검사 함수
     */
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

    /**
     *  TODO : 갤러리 불러오기
     */
    private fun openGallery(){

        //암시적 인텐트 이용
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

    }
}