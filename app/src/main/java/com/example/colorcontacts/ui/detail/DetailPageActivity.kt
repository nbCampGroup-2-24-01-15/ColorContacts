package com.example.colorcontacts.ui.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.colorcontacts.R
import com.example.colorcontacts.data.User
import com.example.colorcontacts.data.UserList
import com.example.colorcontacts.databinding.ActivityDetailPageBinding

private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

@Suppress("DEPRECATION")
class DetailPageActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetailPageBinding.inflate(layoutInflater)
    }

    lateinit var user: User

    //이미지 결과값 받기
    private lateinit var galleryResultLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var isEditing = false

        val hasBackground = false

        initView()


//        if (hasBackground) {
//            binding.ivDetailAddPhoto.isVisible = false
//        } else {
//            binding.ivDetailAddPhoto.isVisible = true
//        }


        binding.ivDetailBackground.setOnClickListener {

//            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            galleryIntent.type = "image/*"

//            if (Build.VERSION.SDK_INT >= 31) {
//                val pick =
//                    registerForActivityResult(ActivityResultContracts.PickVisualMedia) { uri ->
//                        if (uri != null) {
//                            Log.d("photoPicker", uri)
//                        } else {
//                            Log.d("photoPicker", "no image selected")
//                        }
//                    }
//
//                pick.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//
//            }

//            val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//                if (uri != null) {
//                    binding.ivDetailBackground.setImageURI(uri)
//                } else {
//                    Log.d("photoPicker", "no image selected")
//                }
//            }
//
//            getImage.launch("image/*")


            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryResultLauncher.launch(galleryIntent)


//            when {
//                ContextCompat.checkSelfPermission(
//                    this,
//                    android.Manifest.permission.READ_EXTERNAL_STORAGE
//                ) == PackageManager.PERMISSION_GRANTED -> {
//
//                }
//
//                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
//                    showPermissionAlertDialog()
//                }
//
//                else -> {
//                    requestPermissions(
//                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
//                        PERMISSION_CODE
//                    )
//                }
//            }
        }

//        binding.ivDetailAddProfile.setImageResource()

        galleryResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                selectedImageUri = data?.data!!
                binding.ivDetailBackground.setImageURI(selectedImageUri)

            }
        }

        binding.spDetailEvent.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOf("1분", "5분", "10분"))

        binding.ivDetailBack.setOnClickListener {
            if (isEditing) {
                Toast.makeText(this, "수정서항 저장되지않음 취소?", Toast.LENGTH_SHORT).show()
                //dialog?
            }else {
                finish()
            }
        }


        binding.ibDetailPhone.setOnClickListener {
            //call
        }

        binding.ibDetailMessage.setOnClickListener {
            //message
        }

        binding.ibDetailFavorite.setOnClickListener {
            //즐겨찾기 리스트
        }

        binding.ivDetailGroupCircle.setOnClickListener {
            //태그?
        }



        binding.ivDetailEdit.setOnClickListener {
            isEditing = if (isEditing) {
                binding.ivDetailEdit.setImageResource(R.drawable.ic_detail_edit)
                false
            }else {
                binding.ivDetailEdit.setImageResource(R.drawable.ic_detail_edit_done)
                true
            }

            if (isEditing) {
                binding.ivDetailAddPhoto.isVisible = true
                binding.clDetailEmail.isVisible = true
                binding.clDetailGroup.isVisible = true
                binding.clDetailEvent.isVisible = true
                binding.ivDetailBackground.isEnabled = true
                binding.ivDetailAddProfile.isEnabled = true
                binding.etDetailName.isEnabled = true
                binding.etDetailPhoneNumber.isEnabled = true
                binding.etDetailEmail.isEnabled = true
                binding.spDetailEvent.isEnabled = true
                binding.clDetailBtns.isVisible = false

            } else {
                binding.ivDetailAddPhoto.isVisible = false
                binding.clDetailEmail.isVisible = false
                binding.clDetailGroup.isVisible = false
                binding.clDetailEvent.isVisible = false
                binding.ivDetailBackground.isEnabled = false
                binding.ivDetailAddProfile.isEnabled = false
                binding.etDetailName.isEnabled = false
                binding.etDetailName.setTextColor(R.color.text_color)
                binding.etDetailPhoneNumber.isEnabled = false
                binding.etDetailPhoneNumber.setTextColor(R.color.text_color)
                binding.etDetailEmail.isEnabled = false
                binding.etDetailEmail.setTextColor(R.color.text_color)
                binding.spDetailEvent.isEnabled = false
                binding.clDetailBtns.isVisible = true
            }
        }

    }

    private fun initView() {
        user = intent.getStringExtra("user")?.let { UserList.findUser(it) }!!
        setProfile(user)
    }

    private fun setProfile(user: User){
        with(binding){
            ivDetailBackground.setImageURI(user.backgroundImg)
            ivDetailAddProfile.setImageURI(user.img)
            etDetailName.setText(user.name)
            etDetailPhoneNumber.setText(user.phone)
            etDetailMemo.setText(user.info)
        }
    }

//    private fun showPermissionAlertDialog() {
//        AlertDialog.Builder(this)
//            .setTitle("권한 승인")
//            .setMessage("사진을 불러오려면 권한 허용 필요")
//            .setPositiveButton("허용") { _, _ ->
//                requestPermissions(
//                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
//                    PERMISSION_CODE
//                )
//            }
//            .setNegativeButton("거부") { _, _ -> }
//            .create()
//            .show()
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            PERMISSION_CODE -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == packageManager.PERMISSION_GRANTED)
//            }
//        }
//    }


}