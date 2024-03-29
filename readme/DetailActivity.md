Detail Activity
=============

[readme](../README.md)

# (1)Layout [activity_detail_page.xml](../app/src/main/res/layout/activity_detail_page.xml)
<img src="https://github.com/nbCampGroup-2-24-01-15/ColorContacts/assets/151485887/aa7e96b9-fa01-4dff-8a67-6063b853e1fc" width="300" height="650">


가장 위에 뒤로가기와 편집 버튼이 있는 바를 만들고
그 아래에 스크롤뷰로 구성

편집 버튼은 눌렀을 때 인식이 잘 되도록 padding을 줘서 이미지 크기보다 좀 더 큰 뷰로 클릭 범위를 넓혔다

스크롤 뷰 안에는 다시 세로 정렬 리니어 레이아웃이 있고 그 안에 표현할 정보를 순서대로 배치했다

프로필 이미지 부분을 constraint로 두고 그 안에 배경 이미지를 넣을 이미지뷰, 프로필 사진이 들어갈 카드뷰를 배치했다


그 아래로는 차례로 이름, 전화번호가 나오게 하고 editText를 넣고

전화, 메세지로 연결되는 버튼과 즐겨찾기 리스트에 추가하는 버튼을 넣었다

아래로는 차례로 이메일, 그룹(태그), 이벤트, 메모 항목이 있고 태그와 메모를 제외한 해당 항목에 내용이 없을 경우 상세 페이지에서 보이지 않도록 했다

편집 버튼을 누르기 전에는 항목을 눌러도 수정할 수 없도록 했다.


<img src="https://github.com/nbCampGroup-2-24-01-15/ColorContacts/assets/151485887/c16018c7-cb4a-471d-a4ee-e38bd32739fd" width="300" height="650">


삭제하기 버튼을 누르면 정말 삭제하시겠습니까?를 묻는 다이얼로그가 나오고 확인을 누르면 싱글턴으로 사용하는 연락처 리스트에서 제거된다.

<img src="https://github.com/nbCampGroup-2-24-01-15/ColorContacts/assets/151485887/5f13c62f-0b26-46d8-ac1a-a600cc5cbd20" width="300" height="650">


편집 버튼을 눌렀을 때는 오른쪽 상단의 편집 버튼이 체크 이미지로 바뀌고 값을 입력할 수 있도록 모든 항목이 보이고 수정할 수 있다.
편집 중에 전화나 문자를 할 수는 없도록 전화 문자 버튼은 보이지 않게 한다.


<img src="https://github.com/nbCampGroup-2-24-01-15/ColorContacts/assets/151485887/06fbc97e-7ea1-469d-99f9-f7a27e25ce23" width="300" height="650">


편집 중일 때 항목의 내용이 하나라도 바뀐 상태에서 뒤로가기를 누르면 정보가 저장되지 않고 취소된다는 토스트 메시지가 뜨고 2초 간격 내로 다시 한 번 클릭하면 연락처 리스트로 돌아간다.
다시 상세 페이지로 들어가서 확인해 보면 수정 전의 내용 그대로인 것을 확인할 수 있다.

편집을 완료하고 확인 버튼을 누르면 수정한 내용이 보이고 수정이 불가능해진 것을 확인할 수 있다.


# (2)Activity [DetailPageActivity](../app/src/main/java/com/example/colorcontacts/ui/detail/DetailPageActivity.kt)

## 선언한 변수

```kotlin

    private lateinit var getUserByIntent: User
    private lateinit var key: String

```
싱글턴으로 받아온 유저 데이터 중에서 클릭한 유저 한 명의 정보와 그 유저 데이터의 키 값을 받아올 변수를 만든다


```kotlin
    private lateinit var file: File
    private lateinit var backFile: File

    //이미지 결과값 받기
    private lateinit var backgroundGalleryResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var profileGalleryResultLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null
```
화면의 배경과 프로필 이미지를 휴대폰의 갤러리에서 받아오기 위한 변수를 만든다


```kotlin
    // Tag 관련
    private var tagList: MutableList<Tag> = mutableListOf(Tag("태그", defaultTag.img))
    private var userTag: Tag? = null
    private lateinit var spinnerAdapter: SpinnerAdapter
    private var selectedItem: Tag? = null
```
연락처의 태그를 받아오고 지정하기 위한 변수를 만든다
태그는 스피너를 사용하여 선택할 수 있다


```kotlin
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
//현재 편집중인지 아닌지 확인하는 변수
    private var isEditing = false
```
받아온 값의 수정 여부를 판단하기 위해 디폴트 값 변수와 새로 쓸 변수를 각각 나눈다
수정하기 버튼을 누를 때마다 디폴트 변수의 값이 지정된다
현재 편집 화면인지 보기 전용 화면인지에 따라 보여지는 내용과 수정 가능 여부가 변하도록 편집 중인지 확인할 수 있는 boolean변수를 만든다


```kotlin
//뒤로가기를 눌렀을 때 시간 간격 확인 변수
    private var backPressedTime: Long = 0
```
수정 중에 뒤로가기 버튼을 눌렀을 때 한 번 걸리게 하기 위해서 뒤로가기 버튼을 누른 시간을 측정하기 위한 변수를 만든다

```kotlin
// myData
    private var isMyData: Boolean = false
```
리사이클러뷰에서 마이페이지를 선택했는지 다른 연락처를 선택했는지를 구분해서 현재 페이지가 내 정보인지 다른 연락처 정보인지 알 수 있는 boolean 변수를 만든다

```kotlin
//유효성 확인용 editText값
    private val editTexts
        get() = listOf(
            binding.etDetailName,
            binding.etDetailPhoneNumber,
            binding.etDetailEmail
        )

    private var allValid = false

```
마지막으로 편집 중에 화면에 있는 텍스트 입력 값들이 각각의 형식에 맞게 쓰였는지 확인하기 위한 유효성 검사 확인 변수를 만든다



## 기능 동작 메서드

onCreate 내부에는 화면을 초기화하는 initView와 각 항목을 클릭했을 때의 동작을 수행하는 setOnClickListener들이 있다

```kotlin
    //디테일 페이지 초기 화면 구성
    private fun initView() {

        // 태그 Spinner
        setUpTagSpinner()

        key = intent.getStringExtra("user").toString()

        getUserByIntent = intent.getStringExtra("user")?.let {
            if (it != "My") UserList.findUser(it)!!
            else {
                isMyData = true
                setMyPage()
                MyData.myData
            }
        }!!

        setDefaultData(getUserByIntent)
        newData = getUserByIntent
        setProfile(getUserByIntent)

        //binding.spDetailEvent 부분 함수화
        setSpinner()

        setTextChangedListener()

        // 버튼 액션
        onButtonAction()

    }

```
initView에서는 일단 인텐트로 키 값을 받아오고 키의 값에 따라 내 정보인지 연락처 정보인지 구분해서 유저 데이터 클래스의 정보를 받아온다
key가 My로 되어 있는 내 정보일 경우 isMyData변수를 True로 바꾸고 마이페이지에 필요없는 태그와 전화 버튼, 삭제 버튼을 보이지 않게 하는 setMyPage()를 호출한다
받아온 정보를 디폴트 변수와 new 변수에 넣고 setProfile함수에서 보이는 화면을 세팅한다
setSpinner로 이벤트 스피너를 세팅하고 setTextChangerListener로 텍스트의 유효성 검사를 진행한다


```kotlin
    //이미지, 텍스트, 스피너 선택값에 대해 값이 있을 떄만 보이도록
    @SuppressLint("ResourceAsColor")
    private fun setVisibility() {
        if (isMyData) {
            binding.clDetailBtns.isVisible = false
            binding.clDetailGroup.isVisible = false
            binding.tvDetailDelete.isVisible = false
        } else {
            binding.clDetailBtns.isVisible = true
            binding.clDetailGroup.isVisible = true
            binding.tvDetailDelete.isVisible = true
        }
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
        if (TagMember.totalTags.any { it.member.contains(key) }) {
            binding.ibDetailFavorite.setImageResource(R.drawable.ic_detail_favorite_filled)
        } else {
            binding.ibDetailFavorite.setImageResource(R.drawable.ic_detail_favorite_outline)
        }
    }

```
처음 화면이 보일 때와 수정 완료 시에 보이는 화면에서 각 항목이 나타날지 아닐지를 결정하는 함수를 만든다
항목에 내용이 있을 경우에만 나타나도록 한다


```kotlin
   //editText 유효성 검사
    private fun setTextChangedListener() {
        editTexts.forEach { et ->
            et.addTextChangedListener {
                et.error = when (et) {
                    binding.etDetailName -> CheckString().checkName(et.text.toString())
                        ?.let { getString(it) }

                    binding.etDetailPhoneNumber -> CheckString().checkPhoneNumber(et.text.toString())
                        ?.let { getString(it) }

                    else -> CheckString().checkEmail(et.text.toString())
                        ?.let { getString(it) }

                }
            }
        }

        allValid = editTexts.all { it.error == null }

        binding.etDetailPhoneNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        
    }

```
디테일 페이지에 있는 editText 각각을 검사하는 함수를 만든다
CheckString은 유효성 검사를 위해 따로 만들어 둔 클래스로 각 함수에 값이 입력되었을 때 올바른 형식인지 검사해서 에러메세지를 반환한다
모든 editText들의 에러메세지가 null이면 allValid는 true가 된다.
PhoneNumberFormattingTextWatcher()를 이용해서 전화번호에 -를 자동으로 집어넣는다.


```kotlin
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
```
디폴트 값과 편집 중에 변한 값을 비교하는 함수를 만든다


```kotlin
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

```
setSpinner에서는 이벤트 스피너를 세팅하고 X가 선택되어 있을 때는 newData의 event값을 null로 설정한다


```kotlin
    /**
     * 상세 화면 태그 추가
     */
    override fun onTagAdd(name: String, uriad: File) {
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
        TagMember.addNewTag(Tag(name, uriad))
        setTagList(TagMember.totalTags)
    }

    private fun setTagList(list: List<Tag>) {
        tagList = mutableListOf(Tag("태그", defaultTag.img))
        tagList.addAll(list)
        spinnerAdapter.updateItem(tagList)
        spinnerAdapter.notifyDataSetChanged()
    }

    private fun getTagIndex(title: String?, uriad: File?): Int {
        return tagList.indexOfFirst { tag -> tag.title == title && tag.img == uriad }
    }

    /**
     * 회원에 대한 태그 정보 가져오기
     */
    private fun setUserTagOnSpinner() {

        userTag = TagMember.getFindTag(newData.key)

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

```
태그 정보를 가져오고 설정하는 함수를 만든다

```kotlin
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
                id: Long,
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

```
태그를 지정하는 스피너를 세팅하는 함수를 만든다


```kotlin

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
                TagMember.removeMember(newData.key)
            }

            userTag == null && selectedItem != null -> {
                TagMember.addMember(selectedItem!!, newData.key)
            }

            selectedItem != null -> {
                updateMemberTag(newData.key, selectedItem!!)
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

```
추가 버튼을 눌렀을 때 사용자 정의 태그를 추가하는 다이얼로그 창을 띄운다




```kotlin

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
                data?.data?.let { uri ->
                    selectedImageUri = uri
                    binding.ivDetailBackground.setImageURI(uri)
                    val path = this.absolutelyPath(selectedImageUri!!)
                    file = File(path)
                    newData.backgroundImg = file
                }
            }
        }

        profileGalleryResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let { uri ->
                    selectedImageUri = uri
                    val path = this.absolutelyPath(selectedImageUri!!)
                    backFile = File(path)
                    binding.ivDetailAddProfile.load(file)
                    newData.img = backFile
                }
            }
        }
```
편집 화면에서 프로필 배경을 눌렀을 때와 프로필 사진 이미지를 눌렀을 때 갤러리로 연결해서 사진을 가져오는 기능


```kotlin

        //뒤로가기 버튼 눌렀을 때 상태에 따라 동작
        binding.ivDetailBack.setOnClickListener {
            if (isEditing) { // 페이지를 수정 중일때
                //편집 버튼을 눌렀을 때의 값과 현재 값이 같은지 확인
                if (isSame()) {// 페이지가 같다면 액티비티 종료
                    //같으면 수정사항이 없으니까 그냥 뒤로가기
                    //finish말고 수정 전 디테일 페이지로 가는 게 더 자연스러울 것 같은데 그것까지는 못 했다
                    finish()
                } else {
                    //내용이 다르면 수정한 게 있는 거니까 취소할지 일단 물어보고 확실히 취소하는 거면 편집 눌렀을 때 넣어 둔 디폴트 값으로 다시 바꾸고 닫기
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - backPressedTime < 2000) {
                        val data = UserList.userList.find { it.key == key }
                        if (data != null) defaultData = data
                        finish()
                    } else {
                        Toast.makeText(this, "저장하지 않고 돌아가려면 버튼을 한 번 더 누르세요", Toast.LENGTH_SHORT)
                            .show()
                        backPressedTime = currentTime
                    }
                }
            } else {
                finish()
            }
        }

```
뒤로가기 버튼 클릭 시의 동작
편집 화면이 아니라면 바로 뒤로가기 실행이 되고, 편집 화면에서는 바뀐 정보가 없을 때 뒤로가기가 바로 실행된다
편집 중에 바뀐 정보가 있다면 뒤로가기를 눌렀을 때 경고 토스트 메세지가 뜨고 2초 이내에 바로 한 번 더 클릭하면 뒤로가기가 되면서 바꾼 내용은 저장되지 않고 편집 전 정보로 되돌아간다


```kotlin
        //전화 버튼 눌렀을 때 해당 번호로 전화 연결
        binding.ibDetailPhone.setOnClickListener {
            val callUri = Uri.parse("tel:${getUserByIntent.phone} ")
            val callIntent = Intent(Intent.ACTION_CALL, callUri)
            startActivity(callIntent)

        }

        //메세지 버튼 눌렀을 때 해당 번호로 문자 창 연결
        binding.ibDetailMessage.setOnClickListener {
            val messageUri = Uri.parse("smsto:${getUserByIntent.phone}")
            val messageIntent = Intent(Intent.ACTION_SENDTO, messageUri)
            startActivity(messageIntent)
        }

        //별 버튼 눌렀을 때 즐겨찾기 기본 목록에 추가하고 색 바뀌기
        binding.ibDetailFavorite.setOnClickListener {
            if (TagMember.totalTags.any { it.member.contains(key) }) {
                SharedDataListener().offFavorite(key)
                binding.ibDetailFavorite.setImageResource(R.drawable.ic_detail_favorite_outline)
            } else {
                SharedDataListener().onFavorite(key)
                binding.ibDetailFavorite.setImageResource(R.drawable.ic_detail_favorite_filled)
            }
        }
```
번호 아래 있는 각 버튼의 기능
전화 버튼을 누르면 인텐트로 해당 번호에 통화 연결이 된다
메세지 버튼을 누르면 기기의 기본 문자 앱으로 연결되고
별 버튼을 누르면 기본 태그로 즐겨찾기 리스트에 추가가 된다


```kotlin
        //편집 버튼 눌렀을 때
        binding.ivDetailEdit.setOnClickListener {
            setTextChangedListener()
            setNewDataText()
            //현재 편집중이 아니면 버튼 눌렀을 때 버튼은 확인 이미지로 바뀌고 편집중 true로 바꾸기
            //편집중이면 버튼을 눌렀을 때 버튼 편집 이미지로 바뀌고 현재 새로 바뀐 데이터를 싱글턴 데이터 리스트에 각각 반영하고 편집 종료 false로 바꾸기
            isEditing = if (isEditing) {// 수정하는 부분
                if (!isMyData) {
                    if (allValid) {
                        binding.ivDetailEdit.setImageResource(R.drawable.ic_detail_edit)
                        UserList.userList.find { it.key == key }?.let { newData }
                        UserList.userList.find { it.key == key }?.let { it1 ->
                            defaultData

                            // 알람 등록
                            if (selectedEvent != null)
                                UserList.notification.setUserAlarm(it1, this)
                        }
                        // 태그 추가
                        updateUserTag()
                        false

                    } else {
                        Toast.makeText(this, "유효하지 않은 값이 존재합니다", Toast.LENGTH_SHORT).show()
                        true
                    }

                } else {
                    if (allValid) {
                        binding.ivDetailEdit.setImageResource(R.drawable.ic_detail_edit)
                        myData = newData
                        defaultData = myData
                        false
                    } else {
                        Toast.makeText(this, "유효하지 않은 값이 존재합니다", Toast.LENGTH_SHORT).show()
                        true
                    }
                }

            } else {
                binding.ivDetailEdit.setImageResource(R.drawable.ic_detail_edit_done)
                defaultData = getUserByIntent
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

                binding.detailSpinner.isEnabled = true
                binding.ivDetailGroupAdd.visibility = View.VISIBLE

                //mydata 상태 일때는 이벤트 처리 비 활성화
                if (isMyData) binding.clDetailEvent.visibility = View.GONE

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
                binding.detailSpinner.isEnabled = false
                binding.ivDetailGroupAdd.visibility = View.GONE
                binding.ivTagCancel.visibility = View.GONE
            }
        }


```
편집 버튼을 눌렀을 때의 동작
setNewDataText로 버튼을 누른 순간의 텍스트값들을 newData에 집어넣고
setTextChangedListener로 유효성 검사 결과를 다시 받는다
유효성 검사를 통과했는지 한 번 더 검사하고
내 정보와 나머지 연락처를 구분해서 각각 mtData와 해당 key값의 데이터 리스트에 집어넣는다



```kotlin
        //삭제 버튼을 눌렀을 때 다이얼로그 뜨면서 삭제 여부 다시 물어보고
        //네를 눌렀을 때 유저 정보 리스트에서 해당 유저정보가 삭제되고 finish로 연락처 목록 돌아가기
        binding.tvDetailDelete.setOnClickListener {
            var builder = AlertDialog.Builder(this)
            builder.setTitle("연락처 삭제")
            builder.setMessage("정말 삭제하시겠습니까?")
            builder.setPositiveButton("네") { _, _ ->
                UserList.userList.remove(getUserByIntent)
                finish()
            }
            builder.setNegativeButton("아니오", null)
            builder.show()
        }

```
삭제 버튼 클릭 시 다이얼로그로 한 번 더 확인한다


