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
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.colorcontacts.R
import com.example.colorcontacts.data.MyData.myData
import com.example.colorcontacts.data.EventTime
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
    private var newData = User(
        img = null,
        name = "",
        phone = "",
        email = "",
        event = null,
        info = "",
        backgroundImg = null
    )

    private var backPressedTime: Long = 0
    // 이벤트 값
    private var selectedEvent: String? = null

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var isEditing = false


        initView()



        binding.ivDetailBackground.setOnClickListener {

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

        //binding.spDetailEvent 부분 함수화
        setSpinner()

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
                        //악미치겟네수정사항없어 왜 얘가 불
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
            //즐겨찾기 리스트 add
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
                setDefaultData(user)
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
                binding.etDetailEmail.setBackgroundResource(R.color.white)
                binding.spDetailEvent.isEnabled = true
                binding.etDetailMemo.isEnabled = true
                binding.clDetailBtns.isVisible = false

            } else {
                binding.ivDetailAddPhoto.isVisible = false
                setVisibility()
                //isVisible 조건 추가
                binding.ivDetailBackground.isEnabled = false
                binding.ivDetailAddProfile.isEnabled = false
                binding.etDetailName.isEnabled = false
                binding.etDetailPhoneNumber.isEnabled = false
                binding.etDetailEmail.isEnabled = false
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
                //그냥 프래그먼트를 매번 새로고침 하면 안 되
            }
            builder.setNegativeButton("아니오", null)
            builder.show()
        }

    }

    @SuppressLint("ResourceAsColor")
    private fun setVisibility() {
        //이미지, 텍스트, 스피너 선택값에 대해 값이 있을 떄만 isEditing true일 때 보이도록
        when {
            newData.name.isNotBlank() -> {
                binding.etDetailName.setTextColor(R.color.black)
            }
            newData.phone.isNotBlank() -> {
                binding.etDetailPhoneNumber.setTextColor(R.color.black)
            }
            newData.email.isNotBlank() -> {
                binding.clDetailEmail.isVisible = true
                binding.etDetailEmail.setBackgroundResource(R.color.transparent)
                binding.etDetailEmail.setTextColor(R.color.black)
            }
            newData.event != null -> {
                binding.clDetailEvent.isVisible = true
            }
            newData.info.isNotBlank() -> {
                binding.etDetailMemo.setTextColor(R.color.black)
            }

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
        Log.d("same", "img = ${defaultData.img} & ${newData.img}")
        Log.d("same", "back = ${defaultData.backgroundImg} & ${newData.backgroundImg}")
        Log.d("same", "name = ${defaultData.name} & ${binding.etDetailName.text.toString()}")
        Log.d("same", "phone = ${defaultData.phone} & ${binding.etDetailPhoneNumber.text.toString()}")
        Log.d("same", "email = ${defaultData.email} & ${binding.etDetailEmail.text.toString()}")
        Log.d("same", "event = ${defaultData.event} & ${newData.event}")
        Log.d("same", "info = ${defaultData.info} & ${binding.etDetailMemo.text.toString()}")

        return (defaultData.img == newData.img
                && defaultData.backgroundImg == newData.backgroundImg
                && defaultData.name == binding.etDetailName.text.toString()
                && defaultData.phone == binding.etDetailPhoneNumber.text.toString()
                && defaultData.email == binding.etDetailEmail.text.toString()
                && defaultData.event == newData.event
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


    // Spinner 연결 부분
    // selectedEvent 의 값을 정한다.
    private fun setSpinner() {
        val spinner = binding.spDetailEvent
        val items = EventTime.timeArray
        val adapter = EventAdapter(this, items)
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

}