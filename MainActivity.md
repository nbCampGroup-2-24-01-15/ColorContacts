# MainActivity.md

# 1. 메인 페이지의 화면 구성

## (1) Layout : [activity_main.xml](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/res/layout/activity_main.xml)

### - TabLayout과 ViewPager2

ViewPager보다 향상 된 ViewPager2 라이브러리 사용을 권장하기에 ViewPager2를 사용하였다. 


## (2) Activity : [MainActivity.kt](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/java/com/example/colorcontacts/ui/main/MainActivity.kt)

### ViewPager Adapter 객체
- `by lazy`를 사용하여 지연 초기화한다. 
```kotlin
    private val viewPagerAdapter: ViewPagerAdapter by lazy {
        ViewPagerAdapter(this@MainActivity)
    }
```

### 메소드
> setFragment() 
1. `ViewPagerAdapter`의 `addFragment(fragment: Fragment)` 함수를 사용하여 화면에 보여질 FavoriteFragment(), ContactListFragment(), DialPadFragment()를 추가한다. 
2. ViewPager2를 초기화하고 설정하며 ViewPager2에 Adapter를 연결하고, 페이지가 변경될 때의 콜백을 등록한다. 
3. TagLayout과 ViewPager2를 연결하며 각 탭에는 icons 배열에서 가져온 아이콘이 설정된다. 이를 통해 탭을 터치하여 해당 Fragment로 이동할 수 있다.
```kotlin
    private fun setFragment() {
        // ViewPager Adapter의 리스트 초기화
        viewPagerAdapter.removeFragment()
        // (1)
        viewPagerAdapter.addFragment(FavoriteFragment())
        viewPagerAdapter.addFragment(ContactListFragment())
        viewPagerAdapter.addFragment(DialPadFragment())

        // (2)
        binding.viewPager.apply {
            adapter = viewPagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                }
            })
        }

        // (3)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setIcon(icons[position])
        }.attach()
    }
```


## (3) Adapter : [ViewPagerAdapter.kt](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/java/com/example/colorcontacts/adapter/ViewPagerAdapter.kt)  

### - FragmentStatePagerAdapter  

ViewPager에 보여질 Fragment들을 연결시키기 위해 Adapter를 구현하였다.    

Adapter 내부에 Fragment를 담을 `ArrayList<Fragment>` 를 만들고 이를 통해 Fragment를 관리하였다.  

### 메소드

- 보여질 Fragment 리스트의 개수를 반환한다. 
````kotlin
    override fun getItemCount(): Int {
        return fragments.size
    }
````

- 현재 화면에 표시될 Fragment를 생성한다. 
````kotlin
    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
````

- position에 해당하는 Fragment를 반환하며 현재 화면에 보여지는 Fragment를 찾기 위해 생성했다. 
````kotlin
    fun getFragment(position: Int): Fragment {
        return fragments[position]
    }
````
- 리스트에 Fragment를 추가한다. 
````kotlin
    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
        notifyItemInserted(fragments.size - 1)
    }
````

- 리스트를 비우며 Adapter에게 아이템이 삭제되었음을 알린다. 
````kotlin
    fun removeFragment() {
        fragments.clear()
        notifyItemRemoved(fragments.size)
    }
````

# 2. 메인페이지의 검색 기능

## (1) Layout : [activity_main.xml](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/res/layout/activity_main.xml)

### - SearchView

- SearchView의 배경에 사용할 [drawable 리소스](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/res/drawable/background_round_radius.xml) 를 설정한다. 
````kotlin
android:background="@drawable/background_round_radius
````
- SearchView를 기본적으로 아이콘으로만 표시하지 않고, 확장된 상태로 표시한다.
````kotlin
app:iconifiedByDefault="false"
````
- SearchView 내부에 표시되는 힌트를 설정한다.
````kotlin
app:queryHint="@string/main_search_hint
````

## (2) Activity : [MainActivity.kt](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/java/com/example/colorcontacts/ui/main/MainActivity.kt)

### - 메소드

> setOnQueryTextListener()
- setOnQueryTextListener 
  - SearchView에 대한 텍스트 입력 이벤트를 처리하기 위한 리스너를 설정한다.
- onQueryTextSubmit
  - 사용자가 검색 버튼을 눌렀을 때 호출되는 메서드로, 여기서는 false를 반환하여 이벤트가 소비되지 않음을 나타낸다.
- onQueryTextChange
  - 사용자가 검색어를 변경할 때 호출되는 메서드로, `updateItemCurrentFragment` 함수를 호출하여 현재 화면에 표시된 Fragment에 검색어 문자열을 전달한다. 


````kotlin
    private fun setOnQueryTextListener() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                updateItemCurrentFragment(newText)
                return false
            }
        })
    }
````
> updateItemCurrentFragment(newText: String?)
- 검색을 통해 현재 화면에 표시된 Fragment에 검색어 문자열을 전달하는 함수 
- binding.viewPager.currentItem
  - 현재 ViewPager에서 선택된 Fragment의 인덱스를 가져온다.
- viewPagerAdapter.getFragment(currentItem)
  - viewPagerAdapter에서 해당 인덱스에 해당하는 Fragment를 가져온다. 
- when 절
  - Fragment의 타입을 확인하고, updateItem 함수를 호출하여 검색어 문자열을 전달한다. 
````kotlin
    private fun updateItemCurrentFragment(newText: String?) {
        if (newText == null) {
            return
        }
        val currentItem = binding.viewPager.currentItem
        when (val currentFragment = viewPagerAdapter.getFragment(currentItem)) {
            is FavoriteFragment -> currentFragment.updateItem(newText.trim())
            is ContactListFragment -> currentFragment.updateItem(newText.trim())
        }
    }
````

## (3-1) Fragment : [FavoriteFragment.kt](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/java/com/example/colorcontacts/ui/favorite/FavoriteFragment.kt)

### - 메소드
- `즐겨찾기 화면`에서 사용자가 검색어를 입력 하면 `MainActivity` 로부터 호출 되는 함수로 `SearchView`의 검색어 문자열이 전달 된다. 
````kotlin
    fun updateItem(text: String) {
        adapter.performSearch(text)
    }
````

## (4-1) RecyclerView Adapter : [FavoriteAdapter.kt](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/java/com/example/colorcontacts/ui/favorite/adapter/FavoriteAdapter.kt)
### - 인터페이스
- `Filterable`를 사용하면 어댑터가 데이터를 필터링할 수 있도록 기능을 제공하며 Filter 클래스를 사용하여 데이터를 필터링할 수 있게 된다. 
````kotlin
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable, AdapterInterface {
````
### - 객체
````kotlin
private var filteredList: List<FavoriteViewType> = mItem
````

### - 메소드
> performSearch(query: String)
- 사용자가 입력한 검색어를 받아 filter 메서드를 호출하여 검색을 수행한다. 
````kotlin
    fun performSearch(query: String) {
        filter.filter(query)
    }
````
### - 클래스
> getFilter(): Filter
- getFilter
  - Adapter에서 사용되는 Filter를 구현하는 함수로 사용자가 검색어를 입력할 때마다 호출된다.
- performFiltering
  - 백그라운드에서 필터링 작업을 수행한다.
  - charString은 사용자가 입력한 검색어를 나타내며 입력이 공백인 경우 전체 리스트를 유지하고, 그렇지 않으면 필터링 된 리스트를 생성한다.
- publishResults
  - 필터링 결과를 UI에 적용한다.
  - filteredList를 업데이트하고, RecyclerView의 데이터가 변경되었음을 알린다.
````kotlin
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString().trim()

                filteredList = if (charString.isBlank()) {
                    mItem
                } else {
                    val filterCondition: (FavoriteViewType) -> Boolean = { it ->
                        when (it) {
                            is FavoriteViewType.FavoriteUser -> {
                                it.user.name.contains(charString, true) ||
                                        it.user.phone.contains(charString, true) ||
                                        it.user.email.contains(charString, true)
                            }

                            is FavoriteViewType.FavoriteGrid -> {
                                it.user.name.contains(charString, true) ||
                                        it.user.phone.contains(charString, true) ||
                                        it.user.email.contains(charString, true)
                            }
                        }
                    }

                    mItem.filter { filterCondition(it) }
                }

                val filterResults = FilterResults().apply {
                    values = filteredList
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as List<FavoriteViewType>
                tvFavoriteList.checkListEmpty()
                notifyDataSetChanged()
            }
        }
    }
````
## (3-2) Fragment : [ContactListFragment.kt](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/java/com/example/colorcontacts/ui/contactList/ContactListFragment.kt)

- `주소록 화면`에서 사용자가 검색어를 입력 하면 `MainActivity` 로부터 호출 되는 함수로 `SearchView`의 검색어 문자열이 전달 된다. 

### - 메소드
````kotlin
    fun updateItem(text: String) {
        adapter.performSearch(text)
    }
````

## (4-1) RecyclerView Adapter : [ContactAdapter.kt](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/java/com/example/colorcontacts/ui/contactList/adapter/ContactAdapter.kt)
### - 인터페이스
- `Filterable`를 사용하면 어댑터가 데이터를 필터링할 수 있도록 기능을 제공하며 Filter 클래스를 사용하여 데이터를 필터링할 수 있게 된다. 
````kotlin
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable, AdapterInterface {
````

### - 객체
- 필터링한 결과를 저장하기 위한 리스트이다. 
````kotlin
private var filteredList: List<ContactViewType> = mItem
````
### - 메소드
> performSearch(query: String)
- 사용자가 입력한 검색어를 받아 filter 메서드를 호출하여 검색을 수행한다. 
````kotlin
fun performSearch(query: String) {
    filter.filter(query)
}
````
### - 클래스 
> getFilter(): Filter
- getFilter
  - Adapter에서 사용되는 Filter를 구현하는 함수로 사용자가 검색어를 입력할 때마다 호출된다.
- performFiltering
  - 백그라운드에서 필터링 작업을 수행한다.
  - charString은 사용자가 입력한 검색어를 나타내며 입력이 공백인 경우 전체 리스트를 유지하고, 그렇지 않으면 필터링 된 리스트를 생성한다.
- publishResults
  - 필터링 결과를 UI에 적용한다.
  - filteredList를 업데이트하고, RecyclerView의 데이터가 변경되었음을 알린다.

````kotlin
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString().trim()

                filteredList = if (charString.isBlank()) {
                    mItem
                } else {
                    val filterCondition: (ContactViewType) -> Boolean = { it ->
                        when (it) {
                            is ContactViewType.ContactUser -> {
                                it.user.name.contains(charString, true) ||
                                        it.user.phone.contains(charString, true) ||
                                        it.user.email.contains(charString, true)
                            }

                            is GridUser -> {
                                it.user.name.contains(charString, true) ||
                                        it.user.phone.contains(charString, true) ||
                                        it.user.email.contains(charString, true)
                            }
                        }
                    }

                    mItem.filter { filterCondition(it) }
                }

                val filterResults = FilterResults().apply {
                    values = filteredList
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as List<ContactViewType>
                tvContactList.checkListEmpty()
                notifyDataSetChanged()
            }
        }
    }
````