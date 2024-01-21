[ContactListFragment](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/java/com/example/colorcontacts/ui/contactList/ContactListFragment.kt)
=
[readme](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/README.md)

# [Layout](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/res/layout/fragment_contact_list.xml)

![image](https://github.com/mond-al/recyclerview-fastscroller/assets/116724657/20e085a9-99bf-4c84-b1fd-e7f8bb7df1d3)

레이아웃은 간단하게 리사이클러뷰와

스크롤을 해줄 핸들로 구성되어 있다.

리사이클러뷰에 들어갈 아이템으론 본인을 나타낼 self랑

![image](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/assets/116724657/71c84740-0b61-4e60-a84b-30907e04aee6)

ㄱㄴㄷㄹ등 헤더를 장식해줄 header

![image](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/assets/116724657/c491d766-c918-40c9-89f4-d563c94b088b)

그리고 연락처 목록을 나타낼 list 아이템이 있다

![image](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/assets/116724657/bd09e02b-ed7e-4a09-bd0b-b03d8173ae24)


# [Fragment](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/java/com/example/colorcontacts/ui/contactList/ContactListFragment.kt)

<details>
<summary> ContactListFragment.kt </summary>

```kotlin
package com.example.colorcontacts.ui.contactList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.al.mond.fastscroller.FastScroller
import com.example.colorcontacts.R
import com.example.colorcontacts.data.NowColor
import com.example.colorcontacts.data.TagMember
import com.example.colorcontacts.data.UserList
import com.example.colorcontacts.databinding.FragmentContactListBinding
import com.example.colorcontacts.dialog.DataUpdateListener
import com.example.colorcontacts.ui.contactList.adapter.ContactAdapter
import com.example.colorcontacts.ui.contactList.adapter.ContactItemHelper
import com.example.colorcontacts.ui.detail.DetailPageActivity
import com.example.colorcontacts.utill.DataChangedListener
import com.example.colorcontacts.utill.RecyclerViewBindingWrapper
import com.example.colorcontacts.utill.SharedDataListener


class ContactListFragment : Fragment(), DataUpdateListener {

    private val bindingWrapper by lazy {
        RecyclerViewBindingWrapper(binding)
    }
    val dataChangedListener by lazy {
        DataChangedListener(adapter, bindingWrapper)
    }
    private val adapter by lazy {
        ContactAdapter(emptyList(), NowColor.color, binding.tvContactList)
    }

    val sharedDataListener = SharedDataListener()
    private val binding by lazy {
        FragmentContactListBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcContactList.adapter = adapter
        Log.d("ContactListFragment", "Adapter initialized with empty list")
        dataChangedListener

        init()
    }


    private fun init() {
        val loadedData = sharedDataListener.setContactList(UserList.layoutType)
        adapter.load(loadedData ?: emptyList())
        binding.rcContactList.layoutManager = LinearLayoutManager(context)

        setList()
        Log.d("ContactListFragment", "Loaded data: $loadedData")
    }

    /**
     * TODO 연락처 리스트 세팅
     *
     * enum class로 layout타입을 나눠서 구별
     * 현재 타입에 맞춰 sealed class의 ContactUser와 GridUser타입으로 어댑터에 넣어줌
     * sealed class(ContactViewType)의 데이터 클래스에 따라 레이아웃을 어댑터에서 설정함
     */
    private fun setList() {
        dataChangedListener.onColorChanged(NowColor.color)
        dataChangedListener.onLayoutTypeChanged(UserList.layoutType)
        val data = sharedDataListener.setContactList(UserList.layoutType)
        //현재 보고 있는 레이아웃 타입 설정, 버튼도 그에 맞춰 변경 -> 컬러랑 레이아웃타입은 메인페이지에서 버튼 누를때 변경!

        data.let { adapter.load(it) }
        Log.d("ContactListFragment", "Data loaded into adapter, size: ${adapter.itemCount}")
        adapter.itemClick = object : ContactAdapter.ItemClick {
            override fun onClick(view: View, position: Int, key: String) {
                if (view.id == R.id.ll_item_star) {
                    Log.d(
                        "ContactListFragment",
                        "Item clicked - view:$view, Position: $position, Key: $key"
                    )
                    if (TagMember.totalTags.any { it.member.contains(key) }) sharedDataListener.offFavorite(
                        key
                    )
                    else sharedDataListener.onFavorite(key)
                } else {
                    val intent = Intent(view.context, DetailPageActivity::class.java)
                    intent.putExtra("user", key)
                    intent.putExtra("TYPE", "others")
                    startActivity(intent)
                }
            }
        }

        //스와이프 통화
        val itemTouchHelper = ItemTouchHelper(ContactItemHelper(requireContext()))
        itemTouchHelper.attachToRecyclerView(binding.rcContactList)
        FastScroller(binding.handleView).bind(binding.rcContactList)
    }


    override fun onResume() {
        super.onResume()
        setList()
//        setMyPageTab()
    }

    /**
     * TODO Fragment RecyclerView 검색
     */
    fun updateItem(text: String) {
        adapter.performSearch(text)
    }

    /**
     * TODO : DataUpdate 될때 화면을 재구성
     */
    override fun onDataUpdate() {
        setList()
    }


}
```
</details>

ContactListFragment 에선 리사이클러뷰로 연락처의 목록을 띄워 주는 역활을한다

리사이클러뷰는 MyProfile,header,Linear,Grid타입으로 나뉘는데
<details>
<summary>ContactViewType</summary>

```kotlin
sealed class ContactViewType {
    data class ContactUser(val user: User) : ContactViewType()
    data class GridUser(val user: User) : ContactViewType()
    data class Header(val title: String) : ContactViewType()
    data class MyProfile(val user: User) : ContactViewType()
}
```

</details>

리니어타입에 프로필 헤더 등을 추가하고 Grid는 연락처를 그리드형식으로 모아준다

ItemTouchHelper를 통해서 스와이프할시 통화가 가능하다

또한 연락처의 리스트가 많을때 원하는 부분까지 스크롤을 편하게 하기위해

fast Scroll bar를 화면 좌측에 구성해서 빠르게 리스트를 넘길 수 있다.

## [ContactAdapter](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/java/com/example/colorcontacts/ui/contactList/adapter/ContactAdapter.kt)


<details>
<summary>ContactAdapter.kt</summary>

```kotlin
package com.example.colorcontacts.ui.contactList.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
//import coil.load
import com.example.colorcontacts.R
import com.example.colorcontacts.data.ColorTheme
import com.example.colorcontacts.data.TagMember
import com.example.colorcontacts.databinding.ItemContactGridBinding
import com.example.colorcontacts.databinding.ItemContactListBinding
import com.example.colorcontacts.databinding.ItemContactSelfBinding
import com.example.colorcontacts.databinding.ItemContactsHeaderBinding
import com.example.colorcontacts.ui.contactList.adapter.ContactViewType.GridUser
import com.example.colorcontacts.utill.AdapterInterface
import com.example.colorcontacts.utill.LayoutType
import com.example.colorcontacts.utill.SharedDataListener

@Suppress("DEPRECATION")
class ContactAdapter(
    private var mItem: List<ContactViewType>,
    private var mColor: ColorTheme,
    private val tvContactList: TextView
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable, AdapterInterface {
    /**
     * Recyclerview 검색 기능
     * 검색을 위한 리스트 추가
     */
    private var filteredList: List<ContactViewType> = mItem


    //그리드, 리스트 구분해주기
    companion object {
        private const val ITEM_VIEW_TYPE_GRID = 0
        private const val ITEM_VIEW_TYPE_ITEM = 1
        private const val ITEM_VIEW_TYPE_HEAD = 2
        private const val Item_VIEW_TYPE_SELF = 3
    }

    interface ItemClick {
        fun onClick(view: View, position: Int, key: String)
    }

    interface ItemLongClick{
        fun onLongClick(view: View, position: Int, key: String)
    }
    var itemClick: ItemClick? = null
    var itemLongClick: ItemLongClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_GRID -> {
                val binding = ItemContactGridBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                GridViewHolder(binding)
            }
            ITEM_VIEW_TYPE_HEAD -> {
                val binding = ItemContactsHeaderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                HeadHolder(binding)
            }
            Item_VIEW_TYPE_SELF -> {
                val binding = ItemContactSelfBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                SelfHolder(binding)
            }
            else -> {
                val binding = ItemContactListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ItemViewHolder(binding)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("ContactAdapter", "onBindViewHolder - Position: $position")
        when (val item = filteredList[position]) {
            is ContactViewType.ContactUser -> {
                with((holder as ItemViewHolder)) {
                    if (item.user.img != null) img.load(item.user.img)
                    else img.setImageResource(R.drawable.img_user_profile)
                    name.text = item.user.name
                    name.setTextColor(mColor.colorFont)
                    val favorite = TagMember.memberChk(item.user.key)
                    if (favorite != null) {
                        if (favorite.img == null){
                            star.setImageResource(R.drawable.ic_detail_favorite_filled)
                        }
                        favorite.img?.let { star.load(it) }
                    }
                    else star.setImageResource(R.drawable.ic_detail_favorite_outline)
                    favoritgo.setOnClickListener {
                        itemClick?.onClick(it, position, item.user.key)
                        notifyDataSetChanged()
                    }
                    if (item.user.backgroundImg != null) backImg.load(item.user.backgroundImg)
                    else backImg.setImageResource(R.drawable.fill_vector)
                    swipeLayout.background.setTint(mColor.colorLinear)
                    back.setBackgroundColor(mColor.colorWidget)
                    backCall.setColorFilter(mColor.colorFont)
                    backFont.setTextColor(mColor.colorFont)
                    detailgo.setOnClickListener {
                        itemClick?.onClick(it, position, item.user.key)
                        notifyDataSetChanged()
                    }
                }
            }
            is ContactViewType.Header -> {
                with((holder as HeadHolder)) {
                    head.text = item.title
                    head.setTextColor(mColor.colorFont)
                }
            }

            is GridUser -> {
                with((holder as GridViewHolder)) {
                    if (item.user.img != null) {
                        img.load(item.user.img)
                    }else img.setImageResource(R.drawable.img_user_profile)
                    name.text = item.user.name
                    name.setTextColor(mColor.colorFont)
                    layout.setBackgroundColor(mColor.colorLinear)
                }
            }

            is ContactViewType.MyProfile -> {
                with((holder as SelfHolder)) {
                    if (item.user.img != null) img.load(item.user.img)
                    else img.setImageResource(R.drawable.img_user_profile)
                    back.setBackgroundColor(mColor.colorLinear)
                    name.setTextColor(mColor.colorFont)
                    back.setOnClickListener {
                        itemClick?.onClick(it,position,item.user.key)
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun onClickUser(holder: ItemViewHolder, position: Int, key:String){
        holder.itemView.setOnLongClickListener {
            itemLongClick?.onLongClick(it, position,key)
            true
        }
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return filteredList.size
        tvContactList.checkListEmpty()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun load(newItems: List<ContactViewType>) {
        Log.d("ContactAdapter", "Loading new items")
        mItem = newItems
        filteredList = mItem
        tvContactList.checkListEmpty()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun updateColor(newColorTheme: ColorTheme) {
        mColor = newColorTheme
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun changeLayout(layoutType: LayoutType) {
        Log.d("ContactAdapter", "Changing layout to $layoutType")
        mItem = SharedDataListener().setContactList(layoutType)
        filteredList = mItem
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
//        return when (mItem[position]) {
        return when (filteredList[position]) {
            is ContactViewType.ContactUser -> ITEM_VIEW_TYPE_ITEM
            is GridUser -> ITEM_VIEW_TYPE_GRID
            is ContactViewType.Header -> ITEM_VIEW_TYPE_HEAD
            is ContactViewType.MyProfile -> Item_VIEW_TYPE_SELF
        }
    }

    inner class ItemViewHolder(binding: ItemContactListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name = binding.tvContactName
        val img = binding.ivContactImg
        val star = binding.ivContactStar
        val swipeLayout = binding.swipeItemContact
        val back = binding.itemListBack
        val backCall = binding.ivBackCall
        val backFont = binding.tvBackCall
        val detailgo = binding.llDetailGo
        val favoritgo = binding.llItemStar
        val backImg = binding.backImg

        init {
            detailgo.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = filteredList[position] as ContactViewType.ContactUser
                    itemClick?.onClick(it, position, item.user.key)
                    Log.d("ContactAdapter", "Item clicked - view:$it, Position: $position, Key: ${item.user.key}")
                }
            }
        }
    }

    inner class GridViewHolder(binding: ItemContactGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name = binding.tvContactName
        val img = binding.ivContactImg
        val layout = binding.itemLayout

        init {
            itemView.setOnClickListener {
                itemClick?.onClick(it, adapterPosition, "")
            }
        }
    }

    inner class HeadHolder(binding: ItemContactsHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {
        val head = binding.tvContactHeader
    }

    inner class SelfHolder(binding: ItemContactSelfBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.tvContactName
        val img = binding.ivContactImg
        val layout = binding.swipeItemContact
        val back = binding.backImg
    }

    /**
     * 검색 기능
     */

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

                            is ContactViewType.Header -> false
                            is ContactViewType.MyProfile -> false
                        }
                    }

                    mItem.filter { filterCondition(it) }
                }

                val filterResults = FilterResults().apply {
                    values = filteredList
                }
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as List<ContactViewType>
                tvContactList.checkListEmpty()
                notifyDataSetChanged()
            }
        }
    }

    // Fragment에서 호출
    fun performSearch(query: String) {
        filter.filter(query)
    }

    private fun TextView.checkListEmpty() {
        this.visibility = if (filteredList.isEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}
```

</details>

타입이 총 4가지로 나뉘는데

```kotlin
companion object {
        private const val ITEM_VIEW_TYPE_GRID = 0
        private const val ITEM_VIEW_TYPE_ITEM = 1
        private const val ITEM_VIEW_TYPE_HEAD = 2
        private const val Item_VIEW_TYPE_SELF = 3
    }
```
그리드, 리니어(아이템), 헤더, 자신 이렇게 나뉜다

특히 신경썻던 부분은 리니어타입 (CotactViewType.ContactUser)부분인데 

```kotlin
when (val item = filteredList[position]) {
            is ContactViewType.ContactUser -> {
                with((holder as ItemViewHolder)) {
                    if (item.user.img != null) img.load(item.user.img)
                    else img.setImageResource(R.drawable.img_user_profile)
                    name.text = item.user.name
                    name.setTextColor(mColor.colorFont)
                    val favorite = TagMember.memberChk(item.user.key)
                    if (favorite != null) {
                        if (favorite.img == null){
                            star.setImageResource(R.drawable.ic_detail_favorite_filled)
                        }
                        favorite.img?.let { star.load(it) }
                    }
                    else star.setImageResource(R.drawable.ic_detail_favorite_outline)
                    favoritgo.setOnClickListener {
                        itemClick?.onClick(it, position, item.user.key)
                        notifyDataSetChanged()
                    }
                    if (item.user.backgroundImg != null) backImg.load(item.user.backgroundImg)
                    else backImg.setImageResource(R.drawable.fill_vector)
                    swipeLayout.background.setTint(mColor.colorLinear)
                    back.setBackgroundColor(mColor.colorWidget)
                    backCall.setColorFilter(mColor.colorFont)
                    backFont.setTextColor(mColor.colorFont)
                    detailgo.setOnClickListener {
                        itemClick?.onClick(it, position, item.user.key)
                        notifyDataSetChanged()
                    }
                }
            }
```
유저에 저장되어있는 이미지들은 File타입으로 coil.load를 써서 이미지로 받았고

drawable에 있는 이미지는 File타입으로 저장이안되서 null이면 디폴트 이미지를 세팅하도록했다.

뿐만아니라 각 레이아웃 별로 현재 설정한 컬러를 넣어줘서 색깔을 리스트가 갱신될때 마다 바꿀 수 있게 구현했다.

함수들에는 notifyDataSetChanged()를 넣어줘서 함수를 다른 곳에서 쓸때마다 갱신되게 만들었다.


## [ContactItemHelper](https://github.com/nbCampGroup-2-24-01-15/ColorContacts/blob/dev/app/src/main/java/com/example/colorcontacts/ui/contactList/adapter/ContactItemHelper.kt)

<details>
<summary>ContactItemHelper.kt</summary>

```kotlin
package com.example.colorcontacts.ui.contactList.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.net.Uri
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.colorcontacts.data.UserList

/**
 * TODO 스와이프시 통화 기능
 *
 * @param requireContext 프래그먼트에서 받아옴
 *
 * 왼쪽에서 슬라이드 하면서 리사이클러 뷰를 밀면
 * 통화가 가능함
 *
 * 통화 권한을 등록해야함으로 Mainfest에 <uses-permission android:name="android.permission.CALL_PHONE" />를 입력
 */
class ContactItemHelper(val context: Context): ItemTouchHelper.Callback() {

    //드래그 또는 스와이프를 할 수 있는데, 상 하 좌 우로 선택가능, 드래그롤 통해 순서변경도 가능
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipeFqwelags = ItemTouchHelper.RIGHT
        return makeMovementFlags(0, swipeFqwelags)
        //오른쪽으로만 슬라이드 가능하게 설정
    }

    //드래그시 호출
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // 드래그 기능은 사용하지 않으므로 false 반환
        return false
    }

    //스와이프 동작 활성화
    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }
    //롱터치는 비활성화한다
    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    /**
     * TODO 스와이프 설정
     *
     * @param viewHolder ContactAdapter의 ItemViewHolder
     * @param direction 스와이프의 동작 방향
     * 오른쪽으로 스와이프할시 전화 걸기 인텐트 생성
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val personViewHolder = viewHolder as ContactAdapter.ItemViewHolder
        val phoneNumber = UserList.userList.find { it.name == personViewHolder.name.text}?.phone

        if (direction == ItemTouchHelper.RIGHT){
            val callUriSwiped = Uri.parse("tel:$phoneNumber ")
            val callIntent = Intent(Intent.ACTION_CALL, callUriSwiped)
            context.startActivity(callIntent)
        }
    }

    //레이아웃 뒤에 숨겨진 뷰들을 냅두고 겉에 있는 레이아웃만 이동시킨다
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            val view = (viewHolder as ContactAdapter.ItemViewHolder).swipeLayout //리사이클러뷰의 레이아웃
            getDefaultUIUtil().onDraw(c, recyclerView, view, dX, dY, actionState, isCurrentlyActive)
        }//스와이프 동작에 따른 UI 변화를 적용
    }
}
```

</details>
getMovementFlags를 makeMovementFlags(0, swipeFqwelags)로 설정해 오른쪽으로만 스와이프 가능하게 변경

onSwiped에서 스와이프 이벤트를 설정해주는데 direction이 ItemTouchHelper.RIGHT일때 Uri로 폰번호에 연결하고 전화를 호출한다

뒤에 숨어 있는 레이아웃도 같이 이동하지 않게 설정해줘야 하는데

onChildDraw를 이용한다.

스와이프 액션을 감지했을때 리사이클러뷰와 연결하고 위치를 고정시켰다
