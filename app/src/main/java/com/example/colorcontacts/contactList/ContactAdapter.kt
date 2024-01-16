package com.example.colorcontacts.contactList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.colorcontacts.ColorTheme
import com.example.colorcontacts.R
import com.example.colorcontacts.contactList.ContactViewType.GridUser
import com.example.colorcontacts.databinding.ItemContactGridBinding
import com.example.colorcontacts.databinding.ItemContactListBinding

class ContactAdapter(private var mItem: List<ContactViewType>, private var mColor: ColorTheme) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    /**
     * TODO Recyclerview 검색 기능
     * 검색을 위한 리스트 추가
     */
    private var filteredList: List<ContactViewType> = mItem


    //그리드, 리스트 구분해주기
    companion object {
        private const val ITEM_VIEW_TYPE_GRID = 0
        private const val ITEM_VIEW_TYPE_ITEM = 1
    }

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = filteredList[position]) { // TODO mItem -> filteredList
            is ContactViewType.ContactUser -> {
                with((holder as ItemViewHolder)) {
                    img.setImageURI(item.user.img)
                    name.text = item.user.name
                    name.setTextColor(mColor.colorFont)
                    if (item.user.favorites) star.setImageResource(R.drawable.ic_detail_favorite_filled)
                    else star.setImageResource(R.drawable.ic_detail_favorite_outline)
                    star.setOnClickListener {
                        itemClick?.onClick(it, position)
                        notifyDataSetChanged()
                    }
                    swipeLayout.background.setTint(mColor.colorLinear)
                    back.setBackgroundColor(mColor.colorWidget)
                    backCall.setColorFilter(mColor.colorFont)
                    backFont.setTextColor(mColor.colorFont)
                }
                holder.itemView.setOnClickListener {
                    itemClick?.onClick(it, position)
                }
            }

            is GridUser -> {
                with((holder as GridViewHolder)) {
                    img.setImageURI(item.user.img)
                    name.text = item.user.name
                    name.setTextColor(mColor.colorFont)
                    layout.setBackgroundColor(mColor.colorLinear)
                }
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return filteredList.size // TODO mItem -> filteredList
//        return mItem.size
    }

    fun load(newItems: List<ContactViewType>) {
        mItem = newItems
        filteredList = mItem
        notifyDataSetChanged()
    }
    fun updateColor(newColorTheme: ColorTheme) {
        mColor = newColorTheme
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
//        return when (mItem[position]) {
        return when (filteredList[position]) { // TODO mItem -> filteredList
            is ContactViewType.ContactUser -> ITEM_VIEW_TYPE_ITEM
            is GridUser -> ITEM_VIEW_TYPE_GRID
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

        init {
            itemView.setOnClickListener {
                itemClick?.onClick(it, adapterPosition)
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
                itemClick?.onClick(it, adapterPosition)
            }
        }
    }

    /**
     * TODO 검색 기능
     */

    override fun getFilter(): Filter {
        return object : Filter() {
            // 입력 받은 문자열에 대한 처리
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                filteredList = if (charString.isBlank()) { // 공백, 아무런 값이 입력 되지 않았을 때는 원본 리스트
                    mItem
                } else {
                    val filteredList = mItem.filter { // 이름, 전화 번호, 이메일 검색
                        it is ContactViewType.ContactUser &&
                                (it.user.name.contains(charString, true) ||
                                        it.user.phone.contains(charString, true) ||
                                        it.user.email.contains(charString, true))
                    }
                    filteredList // 검색 된 값으로 필터링 된 리스트
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            // 처리에 대한 결과물
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as List<ContactViewType>
                notifyDataSetChanged()
            }
        }
    }

    // Fragment에서 호출
    fun performSearch(query: String) {
        filter.filter(query)
    }
}