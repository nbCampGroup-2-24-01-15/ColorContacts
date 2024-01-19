package com.example.colorcontacts.ui.favorite.adapter

import android.content.Context
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
import com.example.colorcontacts.utill.AdapterInterface
import com.example.colorcontacts.utill.LayoutType
import com.example.colorcontacts.utill.SharedDataListener

class FavoriteAdapter(
    private val context: Context,
    private var mItem: List<FavoriteViewType>,
    private var mColor: ColorTheme,
    private val tvFavoriteList: TextView
) :
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
                    if (favorite != null) star.load(favorite.img)
                    else star.load(R.drawable.ic_detail_favorite_outline)

                    Log.d("FavoriteTagAdapter", "1:   ${favorite?.title}, ${favorite?.img}")

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
        tvFavoriteList.checkListEmpty()
        return filteredList.size
    }

    fun load(newItem: List<FavoriteViewType>) {
        mItem = newItem
        filteredList = mItem
        tvFavoriteList.checkListEmpty()
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

    private fun TextView.checkListEmpty() {
        this.visibility = if (filteredList.isEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

}