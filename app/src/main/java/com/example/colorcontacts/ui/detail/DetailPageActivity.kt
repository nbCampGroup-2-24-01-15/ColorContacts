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
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.colorcontacts.R
import com.example.colorcontacts.data.MyData.myData
import com.example.colorcontacts.data.EventTime
import com.example.colorcontacts.data.User
import com.example.colorcontacts.data.UserList
import com.example.colorcontacts.databinding.ActivityDetailPageBinding
import com.example.colorcontacts.utill.AdapterInterface
import com.google.android.material.snackbar.Snackbar

@Suppress("DEPRECATION")
class DetailPageActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetailPageBinding.inflate(layoutInflater)
    }

    //디테일 페이지에서 표시할 유저 정보와 키 값 변수
    lateinit var user: User
    private lateinit var key: String

    //이미지 결과값 받기
    private lateinit var backgroundGalleryResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var profileGalleryResultLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null


    //수정 전의 기존 값과 수정 완료를 눌렀을 때 바뀌어있는 값
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

    //뒤로가기를 눌렀을 때 시간 간격 확인 변수
    private var backPressedTime: Long = 0

    // 이벤트 값
    private var selectedEvent: String? = null

    //현재 편집중인지 아닌지 확인하는 변수
    private var isEditing = false

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        initView()



        //배경 부분 눌렀을 때 배경 이미지 갤러리에서 가져오기
        binding.ivDetailBackground.setOnClickListener {

            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            backgroundGalleryResultLauncher.launch(galleryIntent)


        }

        //프로필 눌렀을 때 이미지 가져오기
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



        //뒤로가기 버튼 눌렀을 때 상태에 따라 동작
        binding.ivDetailBack.setOnClickListener {
            if (isEditing) {
                //편집 버튼을 눌렀을 때의 값과 현재 값이 같은지 확인
                if (isSame()) {
                    //같으면 수정사항이 없으니까 그냥 뒤로가기
                    //finish말고 수정 전 디테일 페이지로 가는 게 더 자연스러울 것 같은데 그것까지는 못 했다
                    finish()
                } else {
                    //내용이 다르면 수정한 게 있는 거니까 취소할지 일단 물어보고 확실히 취소하는 거면 편집 눌렀을 때 넣어 둔 디폴트 값으로 다시 바꾸고 닫기
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - backPressedTime < 2000) {
                        UserList.userList.find { it.key == key }?.img = defaultData.img
                        UserList.userList.find { it.key == key }?.backgroundImg =
                            defaultData.backgroundImg
                        UserList.userList.find { it.key == key }?.name = defaultData.name
                        UserList.userList.find { it.key == key }?.phone = defaultData.phone
                        UserList.userList.find { it.key == key }?.email = defaultData.email
                        UserList.userList.find { it.key == key }?.event = defaultData.event
                        UserList.userList.find { it.key == key }?.info = defaultData.info
                        finish()
                    } else {
                        Toast.makeText(this, "저장하지 않고 돌아가려면 버튼을 한 번 더 누르세요", Toast.LENGTH_SHORT)
                            .show()
                        backPressedTime = currentTime
                    }
                }
            } else {
//                AdapterInterface.notifyDataSetChanged()
                finish()
            }
        }


        //전화 버튼 눌렀을 때 해당 번호로 전화 연결
        binding.ibDetailPhone.setOnClickListener {
            val callUri = Uri.parse("tel:${user.phone} ")
            val callIntent = Intent(Intent.ACTION_CALL, callUri)
            startActivity(callIntent)

        }

        //메세지 버튼 눌렀을 때 해당 번호로 문자 창 연결
        binding.ibDetailMessage.setOnClickListener {
            val messageUri = Uri.parse("smsto:${user.phone}")
            val messageIntent = Intent(Intent.ACTION_SENDTO, messageUri)
            startActivity(messageIntent)
        }

        //별 버튼 눌렀을 때 즐겨찾기 기본 목록에 추가하고 색 바뀌기?
        binding.ibDetailFavorite.setOnClickListener {
            //즐겨찾기 리스트 add
        }

        //태그 추가 버튼 눌렀을 때 태그 추가
        binding.ivDetailGroupCircle.setOnClickListener {
            //태그?
        }


        //편집 버튼 눌렀을 때
        binding.ivDetailEdit.setOnClickListener {
            //현재 편집중이 아니면 버튼 눌렀을 때 버튼은 확인 이미지로 바뀌고 편집중 true로 바꾸기
            //편집중이면 버튼을 눌렀을 때 버튼 편집 이미지로 바뀌고 현재 새로 바뀐 데이터를 싱글턴 데이터 리스트에 각각 반영하고 편집 종료 false로 바꾸기
            isEditing = if (isEditing) {
                binding.ivDetailEdit.setImageResource(R.drawable.ic_detail_edit)
                UserList.userList.find { it.key == key }?.img = newData.img
                UserList.userList.find { it.key == key }?.backgroundImg = newData.backgroundImg
                UserList.userList.find { it.key == key }?.name =
                    binding.etDetailName.text.toString()
                UserList.userList.find { it.key == key }?.phone =
                    binding.etDetailPhoneNumber.text.toString()
                UserList.userList.find { it.key == key }?.email =
                    binding.etDetailEmail.text.toString()
                UserList.userList.find { it.key == key }?.event = newData.event
                UserList.userList.find { it.key == key }?.info =
                    binding.etDetailMemo.text.toString()
                UserList.userList.find { it.key == key }?.let { it1 -> setDefaultData(it1) }
                false
            } else {
                binding.ivDetailEdit.setImageResource(R.drawable.ic_detail_edit_done)
                setDefaultData(user)
                true
            }

            //그리고 편집 시작했을때/끝났을 때 화면에 보이는 값과 수정가능한 값 세팅
            //편집중이면 배경 사진 선택하기 아이콘 및 모든 항목이 보이고 수정가능해야 하고 전화 버튼 안 보이고
            //편집이 끝났으면 사진선택아이콘은 안 보이고 전화 버튼은 보이고 항목들은 다 수정가능하고
            //항목에 값이 있는 것만 보여야 하고
            if (isEditing) {
                binding.ivDetailAddPhoto.isVisible = true
                binding.clDetailEmail.isVisible = true
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
                binding.ivDetailAddPhoto.isVisible = false
                binding.clDetailBtns.isVisible = true
                binding.ivDetailBackground.isEnabled = false
                binding.ivDetailAddProfile.isEnabled = false
                binding.etDetailName.isEnabled = false
                binding.etDetailPhoneNumber.isEnabled = false
                binding.etDetailEmail.isEnabled = false
                binding.spDetailEvent.isEnabled = false
                binding.etDetailMemo.isEnabled = false
                setVisibility()
            }
        }

        //삭제 버튼을 눌렀을 때 다이얼로그 뜨면서 삭제 여부 다시 물어보고
        //네를 눌렀을 때 유저 정보 리스트에서 해당 유저정보가 삭제되고 finish로 연락처 목록 돌아가기
        binding.tvDetailDelete.setOnClickListener {
            var builder = AlertDialog.Builder(this)
            builder.setTitle("연락처 삭제")
            builder.setMessage("정말 삭제하시겠습니까?")
            builder.setPositiveButton("네") { _, _ ->
                UserList.userList.remove(user)
                finish()
                //프래그먼트에 어떻게 알리지...? notify 안되는데
                //그냥 프래그먼트를 매번 새로고침 하면 안 되나
                //인터페이스로? 어떻게 하지 방법 찾아봐야겠다
            }
            builder.setNegativeButton("아니오", null)
            builder.show()
        }

    }

    //디테일 페이지 초기 화면 구성
    private fun initView() {
        key = intent.getStringExtra("user").toString()
        val type = intent.getStringExtra("TYPE")
        //인텐트로 타입 따로 받아서 마이페이지 화면 따로 구성?을 하고 싶었는데 뭐가 잘 안 되어서 자꾸 에러가 난다 그냥 빼버릴까...
        if (type == "mypage") {
            setDefaultData(myData)
            setProfile(myData)
            setSpinner()
        } else {
            user = intent.getStringExtra("user")?.let { UserList.findUser(it) }!!
            setDefaultData(user)
            setProfile(user)
            //binding.spDetailEvent 부분 함수화
            setSpinner()
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

    //수정하기 전 디폴트 값 세팅하는 함수?
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


    //받아온 데이터로 화면에 정보 배치
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

        //화면 구성할 때 값이 있는지 없는지 확인해서 없으면 안 보이게 하고
        //보여지는 화면이니까 일단 수정 불가능하게 기본값 설정
        binding.ivDetailAddPhoto.isVisible = false
        setVisibility()
        binding.ivDetailBackground.isEnabled = false
        binding.ivDetailAddProfile.isEnabled = false
        binding.etDetailName.isEnabled = false
        binding.etDetailPhoneNumber.isEnabled = false
        binding.etDetailEmail.isEnabled = false
        binding.spDetailEvent.isEnabled = false
        binding.etDetailMemo.isEnabled = false
        binding.clDetailBtns.isVisible = true
    }


    //이미지, 텍스트, 스피너 선택값에 대해 값이 있을 떄만 보이도록
    @SuppressLint("ResourceAsColor")
    private fun setVisibility() {
        if (binding.etDetailName.text.isNotBlank()) {
            binding.etDetailName.setTextColor(R.color.black)
        }
        if (binding.etDetailPhoneNumber.text.isNotBlank()) {
            binding.etDetailPhoneNumber.setTextColor(R.color.black)
        }
        if (binding.etDetailEmail.text.isNotBlank()) {
            binding.clDetailEmail.isVisible = true
            binding.etDetailEmail.setTextColor(R.color.black)
        } else {
            binding.clDetailEmail.isVisible = false
        }
        binding.clDetailEvent.isVisible = newData.event != null
        if (binding.etDetailMemo.text.isNotBlank()) {
            binding.etDetailMemo.setTextColor(R.color.black)
        }
    }

    //기존 데이터랑 현재 데이터랑 비교해서 다 같으면 true
    //false일 때만 뒤로가기 막기
    private fun isSame(): Boolean {
        //각 데이터 값 확인용 로그
        Log.d("same", "img = ${defaultData.img} & ${newData.img}")
        Log.d("same", "back = ${defaultData.backgroundImg} & ${newData.backgroundImg}")
        Log.d("same", "name = ${defaultData.name} & ${binding.etDetailName.text.toString()}")
        Log.d(
            "same",
            "phone = ${defaultData.phone} & ${binding.etDetailPhoneNumber.text.toString()}"
        )
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
                newData.event = selectedEvent
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }.also { spinner.onItemSelectedListener = it }
    }

}


