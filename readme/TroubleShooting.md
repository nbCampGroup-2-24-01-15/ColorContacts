
# 트러블 슈팅
## 이상오

### 연락처 추가
------------
#### 1. 이미지 갤러리를 부르고 결과를 받기위해 ActivityResultLauncher를 구성했는데  startActivityForResult가 불러지지가 않음

->  해결 : 상속받은 클래스를 dialog에서 dialogFragment 으로 변경
### DialogFragment 는 Activity에 종속되어있어서 기존 결과값받기 처리가 유용함

반면, dialog 는 종속되지 않아 별개의 생명주기로 인해 더 복잡해짐

------------------
#### 2. Singtone 클래스에 데이터를 업데이트 한후, 해당 뷰에 업로드가 되지않음
->  해결 : DataUpdateListener 인터페이스를 정의하고, 

MainActivity, 해당 프래그먼트(ContactsListFragment)에 상속하여 

기능(onDataUpdate())을 재정의 하여 처리함

- AddContactDialogFragment() : 인터페이스 인스턴스 생성후 확인 버튼을 누르면 메소드 실행

- mainActivity : Viewpager2 에 보이는 프래그먼트를 찾아서 메소드 실행

- ContactsListFragment : setList()을 실행
--------------

### Event 시간에 맞춰 알림 표시

#### 1. class 로 설계하여 구현하는데 context로 permission 등록이 안됨

->  해결 : permission 등록하는 함수를 인자값을 Activitiy로 바꿈

---------------
#### 2. pending Intent를 등록하고, onRecivce를 받아서 처리하는데 기존의 값이 남아있음
-> 해결 : pending Intent 의 인자 flags 값을 바꿔줌
```kotlin
PendingIntent.FLAG_IMMUTABLE ->
PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
```
----------------

