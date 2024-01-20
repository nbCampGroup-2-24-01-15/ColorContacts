# AddContactDialog
ContactList에 보여질 연락처를 추가하는 다이얼로그
- DialogFragment 를 상속하여 구현
# 레이아웃 구성
<img width="186" alt="layout" src="https://github.com/nbCampGroup-2-24-01-15/ColorContacts/assets/148442711/dd6a4d5c-fd15-498e-a403-06fa0d7adf59">


|이름 |타입        |설명
|----|-----------|--
|뒤로가기|ImageView|해당 다이얼로그를 종료
|프로필|ImageView| 이미지 클릭시,갤러리 내의 사진을 등록
|이름|EditText| 유저 의 이름
|번호|EditText| 유저 의 휴대폰 번호
|이메일|EditText| 유저 의 이메일
|이벤트|Spinner| 알림 설정
|확인|Button| 해당 정보를 입력되었을때 활성화, 데이터 업데이트 후 다이얼로그 종료

## 프로필 사진 지정
![프로필 이미지](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/assets/148442711/54e2b117-970a-45fa-940d-6fa902440972)



default 일 경우, 위의 그림처럼 기본 형태의 이미지 파일이 들어가고

유저가 원하는 이미지를 핸드폰 내에 사진을 불어들어 저장할 수 있게 하였다.

## 유효성 검사
![유효성 검사](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/assets/148442711/4d0f9164-a7ed-4eb4-84eb-458a1c922d5e)

해당 유저 정보(이름,번호,이메일)에 대한 문자열들을 유효성 검사를 실시하여 사용자의 경험을 향상 시킴
## 알림 이벤트 발생 지정
<img width="224" alt="이벤트 스피너" src="https://github.com/nbCampGroup-2-24-01-15/ColorContacts/assets/148442711/dd1e540f-dbfd-4075-95c9-93a06950e956">

알림 이벤트를 각 현재시각 이후에 지정할 수 있다.

## 뒤로가기 버튼 / 완료 버튼
- 뒤로가기 버튼
    - 해당 버튼 클릭시,다이얼로그는 종료된다.

- 완료 버튼

  - 유효성 검사가 완료되지 않으면 활성화 되지 않는다.이후 검사가 완료되면 활성화
  - ![버튼 활성화](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/assets/148442711/395f577a-82d8-415d-8ce0-ca5c923dc5ca)

  - 버튼을 클릭시 저장된 유저를 데이터 업데이트 하고, ContactsList의 Fragment 가 업데이트 된다. 
  - ![데이터 업데이트](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/assets/148442711/d29ae3cf-6a7b-4e87-8c77-bc7b9f7ff649)


# 코드 설명

## 프로필 사진 지정
### OpenGallery()
```kotlin
private fun openGallery() {
        //암시적 인텐트 이용
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResultLauncher.launch(galleryIntent)
    }
```
```kotlin

        //이미지 결과값 콜백 등록
        galleryResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                selectedImageUri = data?.data
                val path = requireActivity().absolutelyPath(selectedImageUri!!)
                file = File(path)
                binding.ivAddContactProfileImg.load(selectedImageUri)

            }
        }
```
### 갤러리(암시적 인텐트), registerForActivityResult 를 이용하여 데이터의 결과를 받는다.


## 유효성 검사
### Class CheckString
- 유효성 검사 클래스
- 정규표현식(Regex) 을 이용하여 error 값의 문자열을 반환한다.
```kotlin
class CheckString {
    //1. 이름 유효성 검사
    fun checkName(str : String): Int? {
        val regex = Regex("^[가-힣a-zA-Z]+$")
        return if (regex.matches(str)) null
        else R.string.add_contact_dialog_name_error
        
        ...
    }
```

### setTextChangedListener()
### editText 유효성을 검사하여 콜백함수를 등록한다.
 - 텍스트를 입력할때 검사하는 방식
 - 폰 번호 같은 경우에는 자동으로 하이픈이 추가되도록 구현하였다.
 ```kotlin
    // phoneNumber 입력시 자동으로 하이픈 추가
    editTexts[1].addTextChangedListener(PhoneNumberFormattingTextWatcher())
 ```

### validCheck(editText: EditText)
- 유효성을 검사하여 error값을 등록
- 버튼의 활성화 비활성화 설정
    -  에러값이 없고 동시에 문자열이 비어있지 않으면 활성화
    -  버튼의 alpha 값을 주어서 시각적으로 효과있게 처리하였다.
    ```kotlin
    setAlpha(NowColor.color.colorWidget,1f) // 활성화 경우
    setAlpha(NowColor.color.colorWidget,0.5f) // 비활성화 경우
    ```
#### setAlpha(color: Int, factor: Float): Int 
- argb 값을 리턴하는 함수

## 알람 이벤트 발생 지정
### object EventTIme
- 이벤트에 대한 알람의 시간을 저장
### setSpinner()
- EventTime.timeArray 값을 spinner 위젯에 연결
- 선택한 spinner의 값을 selectedEvent 에 저장
## 완료 버튼
### User 인스턴스를 생성하여 기록한 정보들을 저장하여 User.UserList 에 데이터 업데이트
```kotlin
// 데이터 전달
val user = User(
img = file,
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
```


### interface DateUpdateListener 

### 데이터 업데이트 리스너 실행
- 데이터 업데이트에 따른 MainActivity,ContactListFragment 에서 기능들을 수행하기위해 인터페이스 실행


### 알람을 등록
[Notification.kt](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/Notification.md) 클래스내의 메소드를 이용
- setUserAlarm(user,requireContext())
    - 새로생성된 user 에 대한 알람을 등록


이후 해당 다이얼로그 종료
