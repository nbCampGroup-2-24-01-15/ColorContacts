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
import com.example.colorcontacts.R
import com.example.colorcontacts.data.ColorTheme
import com.example.colorcontacts.data.TagMember
import com.example.colorcontacts.databinding.ItemContactGridBinding
import com.example.colorcontacts.databinding.ItemContactListBinding
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
        Log.d("ContactAdapter", "onBindViewHolder - Position: $position")
        when (val item = filteredList[position]) {
            is ContactViewType.ContactUser -> {
                with((holder as ItemViewHolder)) {
                    if (item.user.img != null) img.load(item.user.img)
                    name.text = item.user.name
                    name.setTextColor(mColor.colorFont)
                    val favorite = TagMember.memberChk(item.user.key)
                    if (favorite != null) favorite.img?.let { star.load(it) }
                    else star.setImageResource(R.drawable.ic_detail_favorite_outline)
                    favoritgo.setOnClickListener {
                        itemClick?.onClick(it, position, item.user.key)
                        notifyDataSetChanged()
                    }
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

            is GridUser -> {
                with((holder as GridViewHolder)) {
                    img.load(item.user.img)
                    name.text = item.user.name
                    name.setTextColor(mColor.colorFont)
                    layout.setBackgroundColor(mColor.colorLinear)
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

        init {
            Log.d("ContactAdapter", "Item clicked - Position: $adapterPosition")
            itemView.setOnClickListener {
                itemClick?.onClick(it, adapterPosition, "")
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