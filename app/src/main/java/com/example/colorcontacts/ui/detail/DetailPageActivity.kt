package com.example.colorcontacts.ui.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.colorcontacts.R
import com.example.colorcontacts.data.EventTime
import com.example.colorcontacts.data.MyData.myData
import com.example.colorcontacts.data.User
import com.example.colorcontacts.data.UserList
import com.example.colorcontacts.databinding.ActivityDetailPageBinding
import com.google.android.material.snackbar.Snackbar

private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

@Suppress("DEPRECATION")
class DetailPageActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetailPageBinding.inflate(layoutInflater)
    }

    lateinit var user: User
    private lateinit var key: String


    //이미지 결과값 받기
    private lateinit var backgroundGalleryResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var profileGalleryResultLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null

    private val contents
        get() = listOf(
            binding.etDetailName,
            binding.etDetailPhoneNumber,
            binding.clDetailEmail,
            binding.etDetailEmail,
            binding.clDetailEvent,
            binding.clDetailGroup,
            binding.etDetailMemo
        )

    private lateinit var defaultData: User
    private lateinit var newData: User

    private var backPressedTime: Long = 0

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var isEditing = false


        initView()



        binding.ivDetailBackground.setOnClickListener {
//            val key = intent.getStringExtra("user")
            Log.d(
                "data",
                "before = ${UserList.userList.find { it.key == key }?.backgroundImg.toString()}"
            )

            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            backgroundGalleryResultLauncher.launch(galleryIntent)


        }

        binding.ivDetailAddProfile.setOnClickListener {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            profileGalleryResultLauncher.launch(galleryIntent)


        }

        backgroundGalleryResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                selectedImageUri = data?.data!!
                binding.ivDetailBackground.setImageURI(selectedImageUri)
                newData.backgroundImg = selectedImageUri
            }
        }

        profileGalleryResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                selectedImageUri = data?.data!!
                binding.ivDetailAddProfile.setImageURI(selectedImageUri)
                newData.img = selectedImageUri

            }
        }

//        galleryResultLauncher = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val data: Intent? = result.data
//                selectedImageUri = data?.data!!
//                binding.ivDetailAddProfile.setImageURI(selectedImageUri)
//
//            }
//        }


        binding.spDetailEvent.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, EventTime.timeArray)

        binding.ivDetailBack.setOnClickListener {
            if (isEditing) {
                if (isSame()) {
                    finish()
                } else {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - backPressedTime < 2000) {
                        UserList.userList.find { it.key == key }?.img = defaultData.img
                        UserList.userList.find { it.key == key }?.backgroundImg = defaultData.backgroundImg
                        UserList.userList.find { it.key == key }?.name = defaultData.name
                        UserList.userList.find { it.key == key }?.phone = defaultData.phone
                        UserList.userList.find { it.key == key }?.email = defaultData.email
                        UserList.userList.find { it.key == key }?.event = defaultData.event
                        UserList.userList.find { it.key == key }?.info = defaultData.info
                        finish()
                    } else {
                        Toast.makeText(this, "뒤로가기 버튼 다시 누르면 수정사항은 반영되지 않고 돌아감", Toast.LENGTH_SHORT).show()
                        backPressedTime = currentTime
                    }
                }
                //그냥 몇초안에 바로 뒤로가기 눌렀을 때 바로 취소되게
                //변경사항 없을 때는 안 나오게
                //디테일 페이지에 들어갔을 때의 초기 값이랑 변경된 값 비교
                //변경된 값을 유저 데이터리스트에 넣기...를 어케하지
            } else {
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
                UserList.userList.find { it.key == key }?.img = newData.img
                UserList.userList.find { it.key == key }?.backgroundImg = newData.backgroundImg
                UserList.userList.find { it.key == key }?.name = binding.etDetailName.text.toString()
                UserList.userList.find { it.key == key }?.phone = binding.etDetailPhoneNumber.text.toString()
                UserList.userList.find { it.key == key }?.email = binding.etDetailEmail.text.toString()
                UserList.userList.find { it.key == key }?.event = binding.spDetailEvent.toString()
                UserList.userList.find { it.key == key }?.info = binding.etDetailMemo.text.toString()
                UserList.userList.find { it.key == key }?.let { it1 -> setDefaultData(it1) }
                false
            } else {
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
                binding.etDetailMemo.isEnabled = true
                binding.clDetailBtns.isVisible = false

            } else {
                //if (binding.ivDetailAddProfile)
                binding.ivDetailAddPhoto.isVisible = false
                binding.clDetailEmail.isVisible = false
                binding.clDetailGroup.isVisible = false
                binding.clDetailEvent.isVisible = false
                //isVisible 조건 추가
                binding.ivDetailBackground.isEnabled = false
                binding.ivDetailAddProfile.isEnabled = false
                binding.etDetailName.isEnabled = false
                binding.etDetailName.setTextColor(R.color.text_color)
                binding.etDetailPhoneNumber.isEnabled = false
                binding.etDetailPhoneNumber.setTextColor(R.color.text_color)
                binding.etDetailEmail.isEnabled = false
                binding.etDetailEmail.setTextColor(R.color.text_color)
                binding.spDetailEvent.isEnabled = false
                binding.etDetailMemo.isEnabled = false
                binding.clDetailBtns.isVisible = true
            }
        }

        binding.tvDetailDelete.setOnClickListener {
            var builder = AlertDialog.Builder(this)
            builder.setTitle("연락처 삭제")
            builder.setMessage("정말 삭제하시겠습니까?")
            builder.setPositiveButton("네") {_, _ ->
                UserList.userList.remove(user)
                finish()
                //프래그먼트에 어떻게 알리지...? notify 안되는데
                //그냥 프래그먼트를 매번 새로고침 하면 안 되나
            }
            builder.setNegativeButton("아니오", null)
            builder.show()
        }

    }

    private fun setVisibility() {
        //이미지, 텍스트, 스피너 선택값에 대해 값이 있을 떄만 isEditing true일 때 보이도록
        contents.forEach { content ->
//            if (content.isEnabled)
        }
    }

    private fun initView() {
        key = intent.getStringExtra("user").toString()
        val type = intent.getStringExtra("TYPE")
        if (type == "mypage") {
            setDefaultData(myData)
            setProfile(myData)
        } else {
            user = intent.getStringExtra("user")?.let { UserList.findUser(it) }!!
            setDefaultData(user)
            setProfile(user)
        }
        newData = User(
            user.key,
            user.img,
            user.name,
            user.phone,
            user.email,
            user.event,
            user.info,
            user.backgroundImg
        )
    }

    private fun setDefaultData(user: User) {
        defaultData = User(
            user.key,
            user.img,
            user.name,
            user.phone,
            user.email,
            user.event,
            user.info,
            user.backgroundImg
        )
    }

    private fun isSame(): Boolean {
        //기존 데이터랑 현재 데이터랑 비교해서 다 같으면 true
        //false일 때만 뒤로가기 막기
        return (defaultData.img == newData.img
                && defaultData.backgroundImg == newData.backgroundImg
                && defaultData.name == binding.etDetailName.text.toString()
                && defaultData.phone == binding.etDetailPhoneNumber.text.toString()
                && defaultData.email == binding.etDetailEmail.text.toString()
                && defaultData.event == binding.spDetailEvent.toString()
                && defaultData.info == binding.etDetailMemo.text.toString())
    }

    @SuppressLint("ResourceAsColor")
    private fun setProfile(user: User) {
        with(binding) {
            ivDetailBackground.setImageURI(user.backgroundImg)
            ivDetailAddProfile.setImageURI(user.img)
            etDetailName.setText(user.name)
            etDetailPhoneNumber.setText(user.phone)
            etDetailEmail.setText(user.email)
            etDetailMemo.setText(user.info)
        }
        when {
            user.email.isNotBlank() -> {
                binding.clDetailEmail.isVisible = true
            }

            user.event.isNullOrBlank().not() -> {
                binding.clDetailEvent.isVisible = true
            }
        }
        binding.ivDetailAddPhoto.isVisible = false
        binding.clDetailGroup.isVisible = false
        //isVisible 조건 추가
        binding.ivDetailBackground.isEnabled = false
        binding.ivDetailAddProfile.isEnabled = false
        binding.etDetailName.isEnabled = false
        binding.etDetailName.setTextColor(R.color.text_color)
        binding.etDetailPhoneNumber.isEnabled = false
        binding.etDetailPhoneNumber.setTextColor(R.color.text_color)
        binding.etDetailEmail.isEnabled = false
        binding.etDetailEmail.setTextColor(R.color.text_color)
        binding.spDetailEvent.isEnabled = false
        binding.etDetailMemo.isEnabled = false
        binding.etDetailMemo.setTextColor(R.color.text_color)
        binding.clDetailBtns.isVisible = true
    }

    //저거를 함수로 어떻게 따로 못 빼나 뺄 수 있을 것 같은데


}