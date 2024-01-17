//package com.example.colorcontacts.contactList
//
//import android.content.Context
//import android.graphics.drawable.Drawable
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.target.CustomTarget
//import com.bumptech.glide.request.transition.Transition
//import com.example.colorcontacts.R
//import com.example.colorcontacts.User
//import com.example.colorcontacts.contactList.ContactViewType.GridUser
//import com.example.colorcontacts.databinding.ItemContactGridBinding
//import com.example.colorcontacts.databinding.ItemContactListBinding
//
//class ContactAdapter (private var mItem: List<ContactViewType>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//    //그리드, 리스트 구분해주기
//    companion object {
//        private const val ITEM_VIEW_TYPE_GRID = 0
//        private const val ITEM_VIEW_TYPE_ITEM = 1
////        private const val ITEM_VIEW_TYPE_MY_LIST = 2
////        private const val ITEM_VIEW_TYPE_MY_GRID = 3
//    }
//    interface ItemClick {
//        fun onClick(view: View, position: Int)
//    }
//
//    var itemClick : ItemClick? = null
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return when (viewType){
//            ITEM_VIEW_TYPE_GRID -> { val binding = ItemContactGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//                GridViewHolder(binding)
//            }
//            else -> {
//                val binding = ItemContactListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//                ItemViewHolder(binding)
//            }
//        }
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        when (val item = mItem[position]){
//            is ContactViewType.ContactUser -> {
//                with((holder as ItemViewHolder)){
//                    img.setImageURI(item.user.img)
//                    //glide로 item의 background 이미지 적용
//                    Glide.with(itemView.context)
//                        .load(item.user.backgroundImg)
//                        .into(object : CustomTarget<Drawable>() {
//                            override fun onResourceReady(
//                                resource: Drawable,
//                                transition: Transition<in Drawable>?
//                            ) {
//                                background.background = resource
//                            }
//
//                            override fun onLoadCleared(placeholder: Drawable?) {
//                                Toast.makeText(itemView.context, "이미지 로드 실패", Toast.LENGTH_SHORT).show()
//                            }
//                        })
//                    name.text = item.user.name
//                    if (item.user.favorites) star.setImageResource(R.drawable.ic_detail_favorite_filled)
//                    else star.setImageResource(R.drawable.ic_detail_favorite_outline)
//                    star.setOnClickListener {
//                        itemClick?.onClick(it, position)
//                        notifyDataSetChanged()
//                    }
//                }
//                holder.itemView.setOnClickListener {
//                    itemClick?.onClick(it, position)
//                }
//            }
//
//            is GridUser -> {
//                with((holder as GridViewHolder)) {
//                    img.setImageURI(item.user.img)
//                    name.text = item.user.name
//                }
//            }
//            else -> {}
//        }
//    }
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//    override fun getItemCount(): Int {
//        return mItem.size
//    }
//
//    fun load(newItems: List<ContactViewType>){
//        mItem = newItems
//        notifyDataSetChanged()
//    }
//    override fun getItemViewType(position: Int): Int {
//        return when (mItem[position]) {
//            is ContactViewType.ContactUser -> ITEM_VIEW_TYPE_ITEM
//            is GridUser -> ITEM_VIEW_TYPE_GRID
////            is ContactViewType.ContactListMy -> ITEM_VIEW_TYPE_MY_LIST
////            is ContactViewType.GridMy -> ITEM_VIEW_TYPE_MY_GRID
//        }
//    }
//
//    inner class ItemViewHolder(binding: ItemContactListBinding) : RecyclerView.ViewHolder(binding.root){
//        val name = binding.tvContactName
//        val img = binding.ivContactImg
//        val background = binding.swipeItemContact
//        val star =  binding.ivContactStar
//        val swipeLayout = binding.swipeItemContact
//
//        init {
//            itemView.setOnClickListener {
//                itemClick?.onClick(it, adapterPosition)
//            }
//        }
//    }
//
//    inner class GridViewHolder(binding: ItemContactGridBinding) : RecyclerView.ViewHolder(binding.root){
//        val name = binding.tvContactName
//        val img = binding.ivContactImg
//
//        init {
//            itemView.setOnClickListener {
//                itemClick?.onClick(it, adapterPosition)
//            }
//        }
//    }
//}