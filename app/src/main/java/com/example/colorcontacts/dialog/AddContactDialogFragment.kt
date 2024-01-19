package com.example.colorcontacts.dialog


import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.example.colorcontacts.data.EventTime
import com.example.colorcontacts.data.NowColor
import com.example.colorcontacts.data.User
import com.example.colorcontacts.data.UserList
import com.example.colorcontacts.databinding.DialogAddContactBinding
import com.example.colorcontacts.utill.CheckString
import kotlin.math.roundToInt


interface DataUpdateListener {
    fun onDataUpdate()
}

class AddContactDialogFragment() : DialogFragment() {

    private val binding by lazy { DialogAddContactBinding.inflate(layoutInflater) }

    private val validChk get() = CheckString()

    //유효성 검사 체크 변수들
    private var isChecked = false

    private val editTexts
        get() = with(binding) {
            listOf(
                etAddContactName,
                etAddContactPhoneNumber,
                etAddContactEmail
            )
        }

    //이벤트 관련 변수
    private var selectedEvent: String? = null


    //이미지 결과값 받기
    private lateinit var galleryResultLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: String? = null
    private var selectedBackgroundImageUri: String? = null

    //데이터 업데이트 인터페이스
    private var dateUpdateListener: DataUpdateListener? = null

    fun setDataUpdateListener(listener : DataUpdateListener){
        this.dateUpdateListener = listener
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // 다이얼 로그 화면 등록
        val dialog = Dialog(requireContext())
        dialog.setContentView(binding.root)

        // 콜백 리스너 등록
        setCallBackFunction()

        // 스피너 값 설정
        setSpinner()
        return dialog
    }

    // 이벤트 spinner 값
    private fun setSpinner() {

        val spinner = binding.spAddContactEvent
        val items = EventTime.timeArray
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinner.adapter = adapter
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                selectedEvent = parent?.getItemAtPosition(position).toString()
                if (selectedEvent == EventTime.timeArray[0]) selectedEvent = null
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }.also { spinner.onItemSelectedListener = it }
    }


    private fun setCallBackFunction() {
        binding.btnAddContactOk.background.setTint(setAlpha(NowColor.color.colorWidget, 0.5f))

        // Ok 버튼
        binding.btnAddContactOk.run {
            setOnClickListener {

                // 데이터 전달
                val user = User(
                    img = selectedImageUri,
                    backgroundImg = selectedBackgroundImageUri,
                    name = binding.etAddContactName.text.toString(),
                    phone = binding.etAddContactPhoneNumber.text.toString(),
                    email = binding.etAddContactEmail.text.toString(),
                    event = selectedEvent,
                    info = ""
                )
                // 데이터를 전달
                UserList.userList.add(user)
                UserList.userList.sortBy { it.name }

                dateUpdateListener?.onDataUpdate()

                // 알람 등록
                if (selectedEvent != null)
                    UserList.notification.setUserAlarm(user, requireContext())

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
        setTextChangedListener()


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
                selectedImageUri = data?.data.toString()
                binding.ivAddContactProfileImg.setImageURI(Uri.parse(selectedImageUri))

            }
        }


    }


    /**
     *  TODO : 유효성 검사 함수
     */


    // 텍스트가 변경될때 실행
    private fun setTextChangedListener() {

        //editTexts 유효성 검사
        editTexts.forEach { editText ->
            editText.addTextChangedListener {
                validCheck(editText)
            }
        }

        // phoneNumber 입력시 자동으로 하이픈 추가
        editTexts[1].addTextChangedListener(PhoneNumberFormattingTextWatcher())

    }


    //vaildCheck = CheckString
    private fun validCheck(editText: EditText) {
        editText.error = with(binding) {
            when (editText) {
                etAddContactName -> validChk.checkName(editText.text.toString())
                    ?.let { getString(it) }

                etAddContactPhoneNumber -> validChk.checkPhoneNumber(editText.text.toString())
                    ?.let { getString(it) }

                etAddContactEmail -> validChk.checkEmail(editText.text.toString())
                    ?.let { getString(it) }

                else -> ""
            }
        }
        // 에러 값이 없고 동시에 문자열 이 비어 있지 않으면 true
        isChecked = editTexts.all { it.error == null && it.text.toString() != "" }
        binding.btnAddContactOk.isEnabled = isChecked

        // 체크 상태에 따른 버튼 Alpha값 변경

        if (isChecked) binding.btnAddContactOk.background.setTint(
            setAlpha(
                NowColor.color.colorWidget,
                1f
            )
        )
        else binding.btnAddContactOk.background.setTint(setAlpha(NowColor.color.colorWidget, 0.5f))

    }

    private fun setAlpha(color: Int, factor: Float): Int {
        val alpha = (Color.alpha(color) * factor).roundToInt()
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color))
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

