# SpinnerAdapter.md

# 1. 태그 목록 스피너 

## (1) Layout : [activity_datail_page.xml](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/res/layout/activity_detail_page.xml)

## (2) Item Layout : [item_tag_spinner.xml](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/res/layout/item_tag_spinner.xml)

즐겨찾기 태그 데이터 클래스의 경우 속성값으로 태그 이름과 태그 이미지가 있다. 

스피너의 아이템에 태그 이름과 이미지를 나타내기 위해 TextView와 ImageView를 사용하였다.    

## (2) Adapter : [SpinnerAdapter.kt](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/java/com/example/colorcontacts/ui/detail/SpinnerAdapter.kt)

### - 메소드

> getCount() 
- 리스트의 크기를 반환한다. 

> getItem(position: Int)
- 해당 위치의 아이템을 반환한다. 

> getView(position: Int, convertView: View?, parent: ViewGroup): View
- 해당 위치의 객체를 기준으로 이미지, 제목, 텍스트의 색상을 설정한다. 

> getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View
- 드롭다운 아이템의 레이아웃을 확장하며 확장된 레이아웃의 `root View`를 반환한다. 

> updateItem(newItems: List<Tag>)
- 사용자가 태그를 추가했을 때 새로운 태그 리스트로 어댑터의 리스트를 업데이트하기 위한 함수이다.
- 리스트가 변경 되었음을 어댑터에 알리고 UI를 업데이트 한다. 

## (3) Activity : [DetailPageActivity.kt](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/java/com/example/colorcontacts/ui/main/MainActivity.kt)

### - 객체
- SpinnerAdapter 객체를 늦은 초기화 형식으로 생성한다. 
```kotlin
    private lateinit var spinnerAdapter: SpinnerAdapter
```

### - 메소드
> setUpTagSpinner()
- setUpTagSpinner()에서 태그 스피너를 초기화한다..
- SpinnerAdapter의 인스턴스를 생성하고 이를 어댑터로 설정한다.
- OnItemSelectedListener항목 선택 이벤트를 처리하기 위해 스피너를 설정한다.
- 항목이 선택되면 UI를 업데이트한다. 

```kotlin
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
                // 정상적인 아이템이 선택되었을 경우 
                val tag = binding.detailSpinner.getItemAtPosition(position) as Tag
                binding.tvSelectedItem.text = "선택 된 태그 : ${tag.title}"
                selectedItem = tag
            } else {
                // hint가 선택 되었을 경우 
                selectedItem = null
                binding.tvSelectedItem.text = getString(R.string.detail_spinner_empty_item)
                binding.ivTagCancel.visibility = View.GONE
            }
            onButtonVisible() // 버튼 상태 업데이트 
        }

        override fun onNothingSelected(parent: AdapterView<*>?) = Unit
    }
}
```
>  setTagList(list: List<Tag>)
- 스피너의 태그 목록을 업데이트하는 함수이다.
- 스피너에 hint를 보이게 하기 위해 기본값을 추가하여 새로운 리스트를 생성한다.
- spinnerAdapter 사용하여 `updateItem`를 통해 업데이트하고 기본 데이터가 변경되었음을 어댑터에 알린다.

```kotlin
private fun setTagList(list: List<Tag>) {
    tagList = mutableListOf(Tag("태그", defaultTag.img))
    tagList.addAll(list)
    spinnerAdapter.updateItem(tagList)
    spinnerAdapter.notifyDataSetChanged()
}
```