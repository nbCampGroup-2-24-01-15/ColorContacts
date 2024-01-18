package com.example.colorcontacts.ui.favorite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.colorcontacts.R
import com.example.colorcontacts.data.ColorTheme
import com.example.colorcontacts.data.TagMember
import com.example.colorcontacts.databinding.ItemContactGridBinding
import com.example.colorcontacts.databinding.ItemContactListBinding
import com.example.colorcontacts.utill.AdapterInterface
import com.example.colorcontacts.utill.LayoutType
import com.example.colorcontacts.utill.SharedDataListener

class FavoriteAdapter(private var mItem: List<FavoriteViewType>, private var mColor: ColorTheme) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable, AdapterInterface {
    private var filteredList: List<FavoriteViewType> = mItem

    companion object {
        private const val ITEM_VIEW_TYPE_GRID = 0
        private const val ITEM_VIEW_TYPE_ITEM = 1
    }

    interface ItemClick {
        fun onClick(view: View, position: Int, key: String)
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
        when (val item = filteredList[position]) {
            is FavoriteViewType.FavoriteUser -> {
                with(holder as ItemViewHolder) {
                    img.setImageURI(item.user.img)
                    name.text = item.user.name
                    name.setTextColor(mColor.colorFont)
                    val favorite = TagMember.memberChk(item.user.key)
                    if (favorite != null) star.setImageURI(favorite.img)
                    else star.setImageResource(R.drawable.ic_detail_favorite_outline)
                    star.setOnClickListener {
                        itemClick?.onClick(it, position, item.user.key)
                        notifyDataSetChanged()
                    }
                    swipeLayout.background.setTint(mColor.colorLinear)
                    back.setBackgroundColor(mColor.colorWidget)
                    backCall.setColorFilter(mColor.colorFont)
                    backFont.setTextColor(mColor.colorFont)
                }
                holder.itemView.setOnClickListener {
                    itemClick?.onClick(it, position, "")
                }
            }
            is FavoriteViewType.FavoriteGrid -> {
                with(holder as GridViewHolder) {
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
        return filteredList.size
    }

    fun load(newItem: List<FavoriteViewType>) {
        mItem = newItem
        filteredList = mItem
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (filteredList[position]) {
            is FavoriteViewType.FavoriteUser -> ITEM_VIEW_TYPE_ITEM
            is FavoriteViewType.FavoriteGrid -> ITEM_VIEW_TYPE_GRID
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
                itemClick?.onClick(it, adapterPosition,"")
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
                        it is FavoriteViewType.FavoriteUser &&
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
                filteredList = results?.values as List<FavoriteViewType>
                notifyDataSetChanged()
            }
        }
    }

    // Fragment에서 호출
    fun performSearch(query: String) {
        filter.filter(query)
    }

    override fun updateColor(newColorTheme: ColorTheme) {
        mColor = newColorTheme
        notifyDataSetChanged()
    }

    override fun changeLayout(layoutType: LayoutType) {
        mItem = SharedDataListener().setFavoriteList(layoutType)
        filteredList = mItem
        notifyDataSetChanged()
    }

}