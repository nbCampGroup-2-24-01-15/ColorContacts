Detail Activity
=============

[readme](README.md)

# Layout
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


# Activity

##선언한 변수

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



##기능 동작 메서드

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

```

```kotlin

```

```kotlin

```

```kotlin

```
