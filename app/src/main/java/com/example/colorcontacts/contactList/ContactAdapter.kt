package com.example.colorcontacts.contactList

import android.app.Notification
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.colorcontacts.R
import com.example.colorcontacts.databinding.ItemContactListBinding

class ContactAdapter (private var mItem: List<ContactViewType>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val ITEM_VIEW_TYPE_ITEM = 1
    }
    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
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
                    if (item.user.favorites) star.setImageResource(R.drawable.baseline_star_24)
                    else star.setImageResource(R.drawable.baseline_star_outline_24)
                    star.setOnClickListener {
                        itemClick?.onClick(it, position)
                        notifyDataSetChanged()
                    }
                }
                holder.itemView.setOnClickListener {
                    itemClick?.onClick(it, position)
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

    fun load(){
        notifyDataSetChanged()
    }
    override fun getItemViewType(position: Int): Int {
        return when (mItem[position]) {
            is ContactViewType.ContactUser -> ITEM_VIEW_TYPE_ITEM
        }
    }

    inner class ItemViewHolder(binding: ItemContactListBinding) : RecyclerView.ViewHolder(binding.root){
        val name = binding.tvContactName
        val img = binding.ivContactImg
        val star =  binding.ivContactStar

        init {
            itemView.setOnClickListener {
                itemClick?.onClick(it, adapterPosition)
            }
        }

    }

}