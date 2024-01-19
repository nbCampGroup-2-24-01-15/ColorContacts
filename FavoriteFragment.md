# FavoriteFragment.md

# 1. 즐겨찾기 화면의 태그 리스트

## (1) Layout : [fragment_favorite.xml](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/res/layout/activity_main.xml)

즐겨찾기 화면의 태그 리스트의 경우 수평 방향으로 아이템을 배치하기 위해 `android:orientation="horizontal"` 를 선언하여 가로형 리스트로 만들었다. 

## (2) Activity : [FavoriteFragment.kt](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/java/com/example/colorcontacts/ui/main/MainActivity.kt)

- ### 객체
- 즐겨찾기 태그 항목에서 태그를 클릭했을 때 태그에 해당하는 아이템 목록을 보여주기 위해 생성하였으며 초기에는 비어 있는 리스트로 초기화했다. 
```kotlin
private var filteredFavoriteList: List<FavoriteViewType> = emptyList()

filteredFavoriteList
```

- ### 메소드
> setFavoriteTypeAdapter()
- 태그에 따라 즐겨찾기 목록을 필터링하여 표시하기 위한 함수이다. 
- val items = totalTags
  - 전체 태그 목록을 가져온다.
- tagAdapter = FavoriteTagAdapter(requireContext(), items)
 - 태그 목록을 표시하기 위한 어댑터를 초기화한다.
- binding.favoriteRecyclerView.adapter = tagAdapter
  - RecyclerView에 태그 어댑터를 설정한다.
- filteredFavoriteList = filteredTag(loadedData, items[position].img)
  - 클릭 된 태그에 따라 필터링된 목록을 가져온다.
- adapter.load(filteredFavoriteList)
  - 즐겨찾기 목록 어댑터에 필터링된 목록을 load 한다.
```kotlin
    private fun setFavoriteTypeAdapter() {
        val items = totalTags
        tagAdapter = FavoriteTagAdapter(requireContext(), items)
        binding.favoriteRecyclerView.adapter = tagAdapter
        tagAdapter?.itemClick = object : FavoriteTagAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                filteredFavoriteList = filteredTag(loadedData, items[position].img)
                adapter.load(filteredFavoriteList)
            }
        }
    }
```

## (3) RecyclerView Adapter : [FavoriteTagAdapter.kt](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/res/layout/activity_main.xml)
- 즐겨찾기 태그 목록을 표시하기 위한 RecyclerViewd에서 사용 할 어댑터이다. 
- fun updateItem(newItems: List<Tag>) 
  - 어댑터의 데이터를 업데이트하고, 새로운 아이템 목록을 받아와 변경 된 데이터로 RecyclerView를 업데이트한다.

```kotlin
class FavoriteTagAdapter(
    private val context: Context,
    private var items: List<Tag>
) :
    RecyclerView.Adapter<FavoriteTagAdapter.ViewHolder>() {
    // 아이템이 클릭될 때의 콜백 인터페이스를 정의
    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    // 아이템 클릭 이벤트를 처리할 리스너 변수
    var itemClick: ItemClick? = null

    // ViewHolder를 생성
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemFavoriteTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // 각 아이템의 데이터를 ViewHolder에 바인딩
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    // RecyclerView에 표시할 아이템의 총 개수를 반환
    override fun getItemCount(): Int {
        return items.size
    }

    // 각 아이템의 뷰를 담당하는 클래스
    inner class ViewHolder(binding: ItemFavoriteTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageView = binding.ivFavoriteType
        private val textView = binding.tvFavoriteTitle
        fun bind(item: Tag) {
            imageView.load(item.img)
            textView.text = item.title
            imageView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClick?.onClick(itemView, position)
                }
            }
        }
    }

    // 어댑터의 데이터를 업데이트 
    fun updateItem(newItems: List<Tag>) {
        items = newItems
        notifyDataSetChanged()
    }
    
}
```