# AddFavoriteTagDailog.md

# 1. 즐겨찾기 태그 추가 기능 

## (1) Layout : [dialog_add_favorite_tag.xml](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/res/layout/activity_main.xml)

## (2) Activity : [AddFavoriteTagDailog.kt](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/java/com/example/colorcontacts/ui/main/MainActivity.kt)

- ### 인터페이스
```kotlin
    interface OnTagAddListener {
        fun onTagAdd(name: String, uri: Uri)
    }
```
- ### 메소드
> setOnTagAddListener(listener: OnTagAddListener)
- 태그 추가를 위한 리스너를 설정한다. 
```kotlin
    private var listener: OnTagAddListener? = null

    fun setOnTagAddListener(listener: OnTagAddListener) {
        this.listener = listener
    }
```
> initView()
- addTextChangedListener 
  - EditText에 `TextChangedListener`를 등록하여 텍스트 필드의 변경 사항을 수신한다. 
```kotlin
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
                binding.ivAddTagImage.setImageURI(selectedImageUri)
                onButtonEnabled()
            }
        }

        binding.ivAddTagImage.clipToOutline = true
        onButtonAction()
    }
```
> onButtonAction()
- btnAddTag
  - 태그명과 이미지가 빈 값이 아닐 경우 태그 리스트에 데이터를 추가 하기 위해 리스너를 통해 데이터를 전달한다. 
```kotlin
    private fun onButtonAction() {
        binding.btnAddTag.setOnClickListener {
            val tagName = binding.etTagName.text.toString()
            if (selectedImageUri != null) {
                listener?.onTagAdd(tagName, selectedImageUri!!)
            }
            dismiss()
        }
    }
```
> updateTextLength(charSequence: CharSequence?)
- 입력한 태그명의 길이에 따라 텍스트 길이를 TextView에 출력한다. 
```kotlin
   private fun updateTextLength(charSequence: CharSequence?) {
        val textLength = charSequence?.length ?: 0
        binding.tvTextLength.text = textLength.toString()
        binding.tvMaxLength.text = "/$MAX_LENGTH"
    }
```
> updateTextColor(charSequence: CharSequence?)
- 입력된 태그명의 길이에 따라 태그명 필드의 배경색과 텍스트 색상을 변경한다.
- 길이가 최대 `MAX_LENGTH`에 해당되면 배경색이 주황색으로 변경된다. 
```kotlin
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
```
> onResume()
- DialogFragment 내용에 맞게 크기를 조정한다.  
```kotlin
   override fun onResume() {
        super.onResume()
        // DialogFragment 크기 조정
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.setCancelable(true)
    }
```
>  openGallery()
- 암시적 인텐트를 실행하여 갤러리에서 이미지를 선택하도록 한다. 
```kotlin
    private fun openGallery() {
        //암시적 인텐트 이용
        val pickImageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResultLauncher.launch(pickImageIntent)
    }
```

# 즐겨찾기 태그 추가 기능 사용하기 
## (2) Activity : [FavoriteFragment.kt](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/java/com/example/colorcontacts/ui/main/MainActivity.kt)

### - 인터페이스 등록
```kotlin
class FavoriteFragment : Fragment(), AddFavoriteTagDialog.OnTagAddListener {
```
### - 메소드 
> onButtonAction()
- AddFavoriteTagDialog() 생성 및 표시 
```kotlin
    private fun onButtonAction() {
        binding.ivAddTag.setOnClickListener {
            // 생성
            val dialog = AddFavoriteTagDialog()
            dialog.setOnTagAddListener(this@FavoriteFragment)
            dialog.show(requireActivity().supportFragmentManager, DIALOG_TAG)
        }
    }
```
> onTagAdd(name: String, uri: Uri)
- 사용자가 `AddFavoriteTagDailog`에서 `등록하기` 버튼을 클릭할 경우 이 메소드가 호출된다. 
- 전달 받은 데이터로 태그 목록에 태그 데이터를 추가하고 변경 된 리스트로 어댑터의 리스트를 업데이트한다. 
```kotlin
    override fun onTagAdd(name: String, uri: Uri) {
        TagMember.addNewTag(Tag(name, uri))
        tagAdapter?.updateItem(totalTags)
    }
```