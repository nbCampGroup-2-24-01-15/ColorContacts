package com.example.colorcontacts.contactList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.colorcontacts.R
import com.example.colorcontacts.contactList.ContactViewType.GridUser
import com.example.colorcontacts.databinding.ItemContactGridBinding
import com.example.colorcontacts.databinding.ItemContactListBinding

class ContactAdapter (private var mItem: List<ContactViewType>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //그리드, 리스트 구분해주기
    companion object {
        private const val ITEM_VIEW_TYPE_GRID = 0
        private const val ITEM_VIEW_TYPE_ITEM = 1
    }
    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            ITEM_VIEW_TYPE_GRID -> { val binding = ItemContactGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GridViewHolder(binding)
            }
            else -> {
                val binding = ItemContactListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ItemViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = mItem[position]){
            is ContactViewType.ContactUser -> {
                with((holder as ItemViewHolder)){
                    img.setImageURI(item.user.img)
                    name.text = item.user.name
                    if (item.user.favorites) star.setImageResource(R.drawable.ic_star_24)
                    else star.setImageResource(R.drawable.ic_star_outline_24)
                    star.setOnClickListener {
                        itemClick?.onClick(it, position)
                        notifyDataSetChanged()
                    }
                }
                holder.itemView.setOnClickListener {
                    itemClick?.onClick(it, position)
                }
            }

            is GridUser -> {
                with((holder as GridViewHolder)) {
                    img.setImageURI(item.user.img)
                    name.text = item.user.name
                }
            }
        }
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getItemCount(): Int {
        return mItem.size
    }

    fun load(newItems: List<ContactViewType>){
        mItem = newItems
        notifyDataSetChanged()
    }
    override fun getItemViewType(position: Int): Int {
        return when (mItem[position]) {
            is ContactViewType.ContactUser -> ITEM_VIEW_TYPE_ITEM
            is GridUser -> ITEM_VIEW_TYPE_GRID
        }
    }

    inner class ItemViewHolder(binding: ItemContactListBinding) : RecyclerView.ViewHolder(binding.root){
        val name = binding.tvContactName
        val img = binding.ivContactImg
        val star =  binding.ivContactStar
        val swipeLayout = binding.swipeItemContact

        init {
            itemView.setOnClickListener {
                itemClick?.onClick(it, adapterPosition)
            }
        }
    }

    inner class GridViewHolder(binding: ItemContactGridBinding) : RecyclerView.ViewHolder(binding.root){
        val name = binding.tvContactName
        val img = binding.ivContactImg

        init {
            itemView.setOnClickListener {
                itemClick?.onClick(it, adapterPosition)
            }
        }
    }
}