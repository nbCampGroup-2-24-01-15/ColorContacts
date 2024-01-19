package com.example.colorcontacts.ui.favorite.adapter


import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.colorcontacts.data.Tag
import com.example.colorcontacts.databinding.ItemFavoriteTypeBinding

/**
 * 즐겨 찾기 태그를 위한 Adapter
 * 가로형 RecyclerView
 */
fun ImageView.setFavoriteTag(uri: Uri) {
    clipToOutline = true
    Glide.with(context)
        .load(uri)
        .into(this)
}

class FavoriteTagAdapter(
    private var items: List<Tag>
) :
    RecyclerView.Adapter<FavoriteTagAdapter.ViewHolder>() {
    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemFavoriteTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(binding: ItemFavoriteTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageView = binding.ivFavoriteType
        private val textView = binding.tvFavoriteTitle
        fun bind(item: Tag) {
            item.img?.let { imageView.setFavoriteTag(Uri.parse(it)) }
            textView.text = item.title

            imageView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClick?.onClick(itemView, position)
                }
            }
        }
    }

    fun updateItem(newItems: List<Tag>) {
        items = newItems
        items.forEach {
            Log.d("TAG", "${it.title}, ${it.img}")
        }
        notifyDataSetChanged()
    }

}