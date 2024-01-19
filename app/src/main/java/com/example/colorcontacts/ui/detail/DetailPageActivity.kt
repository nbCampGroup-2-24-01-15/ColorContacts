package com.example.colorcontacts.ui.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
import com.example.colorcontacts.data.Tag
import com.example.colorcontacts.data.TagMember
import com.example.colorcontacts.data.TagMember.defaultTag
import com.example.colorcontacts.data.TagMember.updateMemberTag
import com.example.colorcontacts.data.User
import com.example.colorcontacts.data.UserList
import com.example.colorcontacts.databinding.ActivityDetailPageBinding
import com.example.colorcontacts.dialog.AddFavoriteTagDialog
import com.example.colorcontacts.ui.favorite.FavoriteFragment

private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

@Suppress("DEPRECATION")
class DetailPageActivity : AppCompatActivity(), AddFavoriteTagDialog.OnTagAddListener {

    private val binding by lazy {
        ActivityDetailPageBinding.inflate(layoutInflater)
    }

    lateinit var user: User
    private lateinit var key: String


    //이미지 결과값 받기
    private lateinit var backgroundGalleryResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var profileGalleryResultLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: String? = null

    // add
    private var tagList: MutableList<Tag> = mutableListOf(Tag("태그", defaultTag.img))
    private var userTag: Tag? = null
    private lateinit var spinnerAdapter: SpinnerAdapter
    private var selectedItem: Tag? = null

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
                data?.data?.let {uri ->
                    selectedImageUri = uri.toString()
                    binding.ivDetailBackground.setImageURI(uri)
                    newData.backgroundImg = selectedImageUri
                }
            }
        }

        profileGalleryResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let {uri ->
                    selectedImageUri = uri.toString()
                    binding.ivDetailAddProfile.setImageURI(uri)
                    newData.img = selectedImageUri
                }
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
                        val data = UserList.userList.find { it.key == key }
                        if (data != null) defaultData = data
                        finish()
                    } else {
                        //악미치겟네수정사항없어 왜 얘가 불
                        Toast.makeText(this, "뒤로가기 버튼 다시 누르면 수정사항은 반영되지 않고 돌아감", Toast.LENGTH_SHORT)
                            .show()
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


        binding.ivDetailEdit.setOnClickListener {
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
                UserList.userList.find { it.key == key }?.event = binding.spDetailEvent.toString()
                UserList.userList.find { it.key == key }?.info =
                    binding.etDetailMemo.text.toString()
                UserList.userList.find { it.key == key }?.let { it1 -> setDefaultData(it1) }


                // 태그 추가
                updateUserTag()

                false
            } else {
                binding.ivDetailEdit.setImageResource(R.drawable.ic_detail_edit_done)
                setDefaultData(user)
                true
            }

            if (isEditing) {
                binding.ivDetailAddPhoto.isVisible = true
                binding.clDetailEmail.isVisible = true
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

                binding.detailSpinner.isEnabled = true
                binding.ivDetailGroupAdd.visibility = View.VISIBLE

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

                binding.detailSpinner.isEnabled = false
                binding.ivDetailGroupAdd.visibility = View.GONE
                binding.ivTagCancel.visibility = View.GONE
            }
        }

        binding.tvDetailDelete.setOnClickListener {
            var builder = AlertDialog.Builder(this)
            builder.setTitle("연락처 삭제")
            builder.setMessage("정말 삭제하시겠습니까?")
            builder.setPositiveButton("네") { _, _ ->
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
        if (newData.name.isNotBlank()) {
            binding.etDetailName.setTextColor(R.color.black)
        }
        if (newData.phone.isNotBlank()) {
            binding.etDetailPhoneNumber.setTextColor(R.color.black)
        }
        if (newData.email.isNotBlank()) {
            binding.clDetailEmail.isVisible = true
            binding.etDetailEmail.setBackgroundResource(R.color.transparent)
            binding.etDetailEmail.setTextColor(R.color.black)
        } else {
            binding.clDetailEmail.isVisible = false
        }
        binding.clDetailEvent.isVisible = newData.event != null
        if (newData.info.isNotBlank()) {
            binding.etDetailMemo.setTextColor(R.color.black)
        }


    }

    private fun initView() {
        // 태그 Spinner
        setUpTagSpinner()

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
        newData = user

        // 버튼 액션
        onButtonAction()
        // spinner 비활성화
        binding.detailSpinner.isEnabled = false
    }

    private fun setDefaultData(user: User) {
        defaultData = user
    }

    private fun isSame(): Boolean {
        //기존 데이터랑 현재 데이터랑 비교해서 다 같으면 true
        //false일 때만 뒤로가기 막기
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

    @SuppressLint("ResourceAsColor")
    private fun setProfile(user: User) {
        with(binding) {
            user.backgroundImg?.let {
                ivDetailBackground.setImageURI(Uri.parse(it))
            }
            user.img?.let {
                ivDetailAddProfile.setImageURI(Uri.parse(it))
            }
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

        // 회원에 대한 태그 정보 가져오기
        setUserTagOnSpinner()
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

    /**
     * 상세 화면 태그 추가
     */
    override fun onTagAdd(name: String, uri: Uri) {
//        val path = this.absolutelyPath(uri)
//        if (path == null) {
//            Log.d("TAG", "path is null")
//        }
//        val file = File(path!!)
//        Log.d("TAG", "$path")
        /**
         * TODO
         * 여기서 에러 발생
         */
        TagMember.addNewTag(Tag(name, uri))
        setTagList(TagMember.totalTags)
    }

    private fun setTagList(list: List<Tag>) {
        tagList = mutableListOf(Tag("태그", defaultTag.img))
        tagList.addAll(list)
        spinnerAdapter.updateItem(tagList)
        spinnerAdapter.notifyDataSetChanged()
    }

    private fun getTagIndex(title: String?, uri: Uri?): Int {
        return tagList.indexOfFirst { tag -> tag.title == title && tag.img == uri }
    }

    /**
     * 회원에 대한 태그 정보 가져오기
     */
    private fun setUserTagOnSpinner() {
        userTag = TagMember.getFindTag(user.key)

        val selectedIndex = if (userTag == null) {
            0
        } else {
            getTagIndex(userTag!!.title, userTag!!.img).coerceAtLeast(0)
        }

        binding.detailSpinner.setSelection(selectedIndex)
    }

    private fun onButtonVisible() {
        binding.ivTagCancel.visibility = if (selectedItem == null) View.GONE else View.VISIBLE
    }

    /**
     * 수정 화면이 아닐 경우 스피너가 비활성화 되어야 함
     * 수정 화면일 경우만 편집이 가능
     * 스피너, 더하기 버튼, 엑스 버튼 상태 관리
     * 스피너 세팅
     */
    private fun setUpTagSpinner() {
        tagList.addAll(TagMember.totalTags)
        spinnerAdapter = SpinnerAdapter(this@DetailPageActivity, R.layout.item_tag_spinner, tagList)
        binding.detailSpinner.adapter = spinnerAdapter

        binding.detailSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    val tag = binding.detailSpinner.getItemAtPosition(position) as Tag
                    binding.tvSelectedItem.text = "선택 된 태그 : ${tag.title}"
                    selectedItem = tag
                } else {
                    selectedItem = null
                    binding.tvSelectedItem.text = getString(R.string.detail_spinner_empty_item)
                    binding.ivTagCancel.visibility = View.GONE
                }
                onButtonVisible()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
    }

    private fun onButtonAction() {
        // 태그 추가 버튼
        binding.ivDetailGroupAdd.setOnClickListener {
            showAddTagDialog()
        }

        // 태그 삭제 버튼
        binding.ivTagCancel.setOnClickListener {
            clearSelectedTag()
        }
    }

    /**
     * 선택된 태그가 있는지 확인 후 추가
     * 원래 있었는데 없어질 경우 태그 목록에서 멤버 삭제
     */
    private fun updateUserTag() {
        when {
            userTag != null && selectedItem == null -> {
                TagMember.removeMember(user.key)
            }

            userTag == null && selectedItem != null -> {
                TagMember.addMember(selectedItem!!, user.key)
            }

            selectedItem != null -> {
                updateMemberTag(user.key, selectedItem!!)
            }
        }
    }

    private fun setTagEnabled(enabled: Boolean) {
        binding.detailSpinner.isEnabled = enabled
        binding.ivDetailGroupAdd.isEnabled = enabled
        binding.ivTagCancel.isEnabled = enabled
    }

    private fun showAddTagDialog() {
        val dialog = AddFavoriteTagDialog()
        dialog.setOnTagAddListener(this@DetailPageActivity)
        dialog.show(supportFragmentManager, FavoriteFragment.DIALOG_TAG)
    }

    private fun clearSelectedTag() {
        selectedItem = null
        binding.tvSelectedItem.text = getString(R.string.detail_spinner_empty_item)
        onButtonVisible()
    }
}