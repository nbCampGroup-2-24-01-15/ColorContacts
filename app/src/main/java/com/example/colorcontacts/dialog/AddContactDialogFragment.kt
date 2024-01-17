package com.example.colorcontacts.dialog


import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.colorcontacts.R
import com.example.colorcontacts.data.User
import com.example.colorcontacts.data.UserList
import com.example.colorcontacts.databinding.DialogAddContactBinding
import com.example.colorcontacts.utill.CheckString
import com.example.colorcontacts.utill.SharedViewModel

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

    //이벤트 관련 변수
    private lateinit var selectedEvent : String
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // 다이얼 로그 화면 등록
        var dialog = Dialog(requireContext())
        dialog.setContentView(binding.root)

        // 콜백 리스너 등록
        setCallBackFunction()

        // 이벤트 spinner 값
        val spinner = binding.spinner
        val items = arrayOf("1초","5초","1분","10분","1시간")
        val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinner.adapter= adapter
        object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                selectedEvent = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }.also { spinner.onItemSelectedListener = it }

        return dialog
    }

    private fun setCallBackFunction() {

        // Ok 버튼
        binding.btnAddContactOk.run {
            // 유효성 검사 전 확인 버튼 비활성화
            isEnabled = false
            setOnClickListener {

                // 데이터 전달
                val user = User(
                    img = selectedImageUri,
                    name = binding.etAddContactName.text.toString(),
                    phone = binding.etAddContactPhoneNumber.text.toString(),
                    email = binding.etAddContactEmail.text.toString(),
                    event = selectedEvent,
                    info = null,
                )
                // 데이터를 전달
                UserList.userList.add(user)

                // 뷰모델을 연결하여 UI를 업데이트(뷰모델의 라이브 데이터를 갱신)
                val viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
                viewModel.setLayoutType()

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
                if (!isCheckedName) etName.error =
                    getString(R.string.add_contact_dialog_name_error)
            }

            override fun afterTextChanged(s: Editable?) {
                //텍스트 바뀐후 수행
                isCheckedName = CheckString().checkName(s.toString())
                if (!isCheckedName) etName.error =
                    getString(R.string.add_contact_dialog_name_error)
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
                if (!isCheckedPhoneNum) etPhoneNum.error =
                    getString(R.string.add_contact_dialog_phone_number_error)
            }

            override fun afterTextChanged(s: Editable?) {
                //텍스트 바뀐후 수행
                isCheckedPhoneNum = CheckString().checkPhoneNumber(s.toString())
                if (!isCheckedPhoneNum) etPhoneNum.error =
                    getString(R.string.add_contact_dialog_phone_number_error)
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
                if (!isCheckedEmail) etEmail.error =
                    getString(R.string.add_contact_dialog_email_error)
            }

            override fun afterTextChanged(s: Editable?) {
                //텍스트 바뀐후 수행
                isCheckedEmail = CheckString().checkEmail(s.toString())
                if (!isCheckedEmail) etEmail.error =
                    getString(R.string.add_contact_dialog_email_error)
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


}