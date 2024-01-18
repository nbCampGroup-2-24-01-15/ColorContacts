package com.example.colorcontacts.dialog


import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.media.metrics.Event
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.colorcontacts.data.EventTime
import com.example.colorcontacts.utill.CheckString
import com.example.colorcontacts.data.NowColor
import com.example.colorcontacts.data.User
import com.example.colorcontacts.data.UserList
import com.example.colorcontacts.databinding.DialogAddContactBinding
import com.example.colorcontacts.utill.DataChangedListener
import kotlin.math.roundToInt

class AddContactDialogFragment() : DialogFragment() {
    interface DialogDismissListener {
        fun onDialogDismissed()
    }

    var dismissListener: DialogDismissListener? = null

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissListener?.onDialogDismissed()
    }

    private val binding by lazy { DialogAddContactBinding.inflate(layoutInflater) }

    private val validChk get() = CheckString()
    //유효성 검사 체크 변수들
    private var isChecked = false


    //이벤트 관련 변수
    private lateinit var selectedEvent : String

    private val editTexts get() = with(binding) {
        listOf(
            etAddContactName,
            etAddContactPhoneNumber,
            etAddContactEmail
        )
    }

    //이미지 결과값 받기
    private lateinit var galleryResultLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null
    private var selectedBackgroundImageUri: Uri? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // 다이얼 로그 화면 등록
        val dialog = Dialog(requireContext())
        dialog.setContentView(binding.root)

        // 콜백 리스너 등록
        setCallBackFunction()

        // 스피너 값 설정
        // 이벤트 spinner 값
        val spinner = binding.spinner
        val items = EventTime.timeArray
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


    private fun setAlpha(color: Int, factor: Float): Int {
        val alpha = (Color.alpha(color) * factor).roundToInt()
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color))
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
                    info = null,
                )
                // 데이터를 전달
                UserList.userList.add(user)
                UserList.userList.sortBy { it.name }


                // 알람 등록
                UserList.notification.setUserAlarm(user,requireContext())

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
                selectedImageUri = data?.data!!
                binding.ivAddContactProfileImg.setImageURI(selectedImageUri)

            }
        }


    }


    /**
     *  TODO : 유효성 검사 함수
     */
    private fun setTextChangedListener() {
        editTexts.forEach { editText ->
            editText.addTextChangedListener{
                validCheck(editText)
            }
        }
    }

    private fun validCheck(editText: EditText) {
        editText.error = with(binding) {
            when (editText) {
                etAddContactName -> validChk.checkName(editText.text.toString()) ?.let { getString(it) }
                etAddContactPhoneNumber -> validChk.checkPhoneNumber(editText.text.toString()) ?.let { getString(it) }
                else -> validChk.checkEmail(editText.text.toString()) ?.let { getString(it) }
            }
        }
        isChecked = editTexts.all { it.error == null }
        binding.btnAddContactOk.isEnabled = isChecked
        if (isChecked) binding.btnAddContactOk.background.setTint(setAlpha(NowColor.color.colorWidget, 1f))
        else binding.btnAddContactOk.background.setTint(setAlpha(NowColor.color.colorWidget, 0.5f))

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


}

