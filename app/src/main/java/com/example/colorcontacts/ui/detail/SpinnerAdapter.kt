package com.example.colorcontacts.ui.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.example.colorcontacts.R
import com.example.colorcontacts.data.Tag
import com.example.colorcontacts.databinding.ItemTagSpinnerBinding

class SpinnerAdapter(
    context: Context,
    @LayoutRes private val resId: Int,
    private var items: MutableList<Tag>
) : ArrayAdapter<Tag>(context, resId, items) {

    override fun getCount() = items.size


    override fun getItem(position: Int) = items[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding =
            ItemTagSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val model = items[position]
        try {
            binding.imgSpinner.clipToOutline = true
            binding.imgSpinner.setImageURI(model.img)
//            binding.imgSpinner.setColorFilter(ContextCompat.getColor(context, R.color.white))
            binding.txtName.text = model.title
            binding.txtName.setTextColor(ContextCompat.getColor(context, R.color.black))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding =
            ItemTagSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val model = items[position]
        try {
            binding.imgSpinner.clipToOutline = true
            binding.imgSpinner.setImageURI(model.img)
            binding.txtName.text = model.title

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }


    fun updateItem(newItems: List<Tag>) {
        items = newItems.toMutableList()

        notifyDataSetChanged()
    }

}