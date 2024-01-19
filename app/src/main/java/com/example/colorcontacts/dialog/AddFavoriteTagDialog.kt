package com.example.colorcontacts.dialog

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import coil.load
import com.example.colorcontacts.FilePath.absolutelyPath
import com.example.colorcontacts.R
import com.example.colorcontacts.databinding.DialogAddFavoriteTagBinding
import java.io.File

class AddFavoriteTagDialog : DialogFragment() {
    companion object {
        const val MAX_LENGTH = 6
    }

    private var _binding: DialogAddFavoriteTagBinding? = null
    private val binding get() = _binding!!

    interface OnTagAddListener {
        fun onTagAdd(name: String, uri: Uri)
    }

    private var listener: OnTagAddListener? = null

    //이미지 결과값 받기
    private lateinit var galleryResultLauncher: ActivityResultLauncher<Intent>

    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogAddFavoriteTagBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    fun setOnTagAddListener(listener: OnTagAddListener) {
        this.listener = listener
    }

    private fun initView() {
        // 태그명 입력 칸
        binding.etTagName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) = Unit

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                onButtonEnabled()
                updateTextLength(charSequence)
                updateTextColor(charSequence)
            }

            override fun afterTextChanged(editable: Editable?) = Unit
        })

        //이미지 결과값 콜백 등록
        galleryResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                selectedImageUri = data?.data!!
//                binding.ivAddTagImage.setImageURI(selectedImageUri)
                val path = requireActivity().absolutelyPath(selectedImageUri!!)
                val file = File(path)
                binding.ivAddTagImage.load(file)
                onButtonEnabled()
            }
        }

        binding.ivAddTagImage.clipToOutline = true
        onButtonAction()
    }

    private fun onButtonAction() {
        // 추가 하기 버튼 클릭
        binding.btnAddTag.setOnClickListener {
            val tagName = binding.etTagName.text.toString()
            if (selectedImageUri != null) {
                listener?.onTagAdd(tagName, selectedImageUri!!)
            }
            dismiss()
        }

        binding.ivBack.setOnClickListener {
            dismiss()
        }

        // 이미지 클릭시 이미지 등록
        binding.ivAddTagImage.setOnClickListener {
            openGallery()
        }
    }

    private fun onButtonEnabled() {
        binding.btnAddTag.isEnabled = !(binding.etTagName.text.isBlank() ||
                selectedImageUri == null)
    }

    // 실시간 텍스트 길이 출력
    private fun updateTextLength(charSequence: CharSequence?) {
        val textLength = charSequence?.length ?: 0
        binding.tvTextLength.text = textLength.toString()
        binding.tvMaxLength.text = "/$MAX_LENGTH"
    }

    // 입력 된 텍스트 길이가 맥스일 때 색상 변경
    private fun updateTextColor(charSequence: CharSequence?) {
        val textLength = charSequence?.length ?: 0
        val isMaxLength = (textLength == MAX_LENGTH)

        binding.etTagName.setBackgroundResource(
            if (isMaxLength) R.drawable.background_radius_orange
            else R.drawable.background_radius_black
        )

        binding.tvTextLength.setTextColor(
            if (isMaxLength) Color.rgb(255, 165, 0)
            else Color.BLACK
        )
    }

    override fun onResume() {
        super.onResume()
        // DialogFragment 크기 조절
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.setCancelable(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    /**
     *  TODO : 갤러리 불러오기
     *  setCallBackFunction() 에서 결과에 대한 처리를 등록 한다.
     */
    private fun openGallery() {
        //암시적 인텐트 이용
        val pickImageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResultLauncher.launch(pickImageIntent)
    }
}