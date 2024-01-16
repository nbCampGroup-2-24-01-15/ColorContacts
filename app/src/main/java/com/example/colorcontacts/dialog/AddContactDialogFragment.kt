package com.example.colorcontacts.dialog


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.example.colorcontacts.CheckString
import com.example.colorcontacts.User
import com.example.colorcontacts.UserList
import com.example.colorcontacts.databinding.DialogAddContactBinding

class AddContactDialogFragment : DialogFragment() {
    private val binding by lazy { DialogAddContactBinding.inflate(layoutInflater) }

    //유효성 검사 체크 변수들
    private var isChecked = false
    private var isCheckedName = false
    private var isCheckedPhoneNum = false
    private var isCheckedEmail = false

    //이미지 결과값 받기
    private lateinit var galleryResultLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // 다이얼 로그 화면 등록
        var dialog = Dialog(requireContext())
        dialog.setContentView(binding.root)

        // 콜백 리스너 등록
        setCallBackFunction()

        return dialog
    }

    private fun setCallBackFunction() {

        // Ok 버튼
        binding.btnAddContactOk.run {
            // 유효성 검사 전 확인 버튼 비활성화
            isEnabled = false
            setOnClickListener {

                // 데이터 전달
                selectedImageUri =
                    getUri(binding.ivAddContactProfileImg)
                val user = User(
                    img = selectedImageUri,
                    name = binding.etAddContactName.text.toString() ?: "Unknown",
                    phone = binding.etAddContactPhoneNumber.text.toString() ?: "No Phone",
                    email = binding.etAddContactEmail.text.toString() ?: "No Email",
                    event = null,
                    info = null,
                    favorites = false
                )
                // 데이터 를 전달
                UserList.userList.add(user)
                // 종료
                dismiss()
            }
        }

        // 뒤로가기 버튼
        binding.ivAddContactBackBtn.setOnClickListener {
            //종료
            dismiss()
        }


        // 유효성 검사 리스너 등록
        checkString()


        // 이미지 클릭시 이미지 등록
        binding.ivAddContactProfileImg.setOnClickListener {
            openGallery()
        }


        //이미지 결과값 콜백 등록
        galleryResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                selectedImageUri = data?.data!!
                binding.ivAddContactProfileImg.setImageURI(selectedImageUri)

            }
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
                if (!isCheckedName) etName.error = "한글이나 영문자만 가능합니다."
            }

            override fun afterTextChanged(s: Editable?) {
                //텍스트 바뀐후 수행
                isCheckedName = CheckString().checkName(s.toString())
                if (!isCheckedName) etName.error = "한글이나 영문자만 가능합니다."
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
                if (!isCheckedPhoneNum) etPhoneNum.error = "010-xxxx-xxxx 형태 이어야합니다."
            }

            override fun afterTextChanged(s: Editable?) {
                //텍스트 바뀐후 수행
                isCheckedPhoneNum = CheckString().checkPhoneNumber(s.toString())
                if (!isCheckedPhoneNum) etPhoneNum.error = "010-xxxx-xxxx 형태 이어야합니다."
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
                if (!isCheckedEmail) etEmail.error = "이메일 형식에 맞춰주세요."
            }

            override fun afterTextChanged(s: Editable?) {
                //텍스트 바뀐후 수행
                isCheckedEmail = CheckString().checkEmail(s.toString())
                if (!isCheckedEmail) etEmail.error = "이메일 형식에 맞춰주세요."
                isChecked = isCheckedName && isCheckedEmail && isCheckedPhoneNum
                btnOk.isEnabled = isChecked
            }

        })
    }

    /**
     *  TODO : 갤러리 불러오기
     *  setCallBackFunction() 에서 결과에 대한 처리를 등록 한다.
     */
    private fun openGallery() {
        //암시적 인텐트 이용
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResultLauncher.launch(galleryIntent)
    }

    /**
     *  TODO 해당 VIEW ID-> URI 형식에 맞게 Parse(파싱)
     */
    private fun getUri(v: View): Uri {
        val resId = v.id
        return Uri.Builder()
            .scheme("android.resource")
            .authority("com.example.colorcontacts")
            .path(resId.toString())
            .build()
    }
}