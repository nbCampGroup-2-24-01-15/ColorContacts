package com.example.colorcontacts.utill

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colorcontacts.data.ColorTheme

class DataChangedListener(private val adapter: AdapterInterface?, private val binding: RecyclerViewBindingWrapper) : OnDataChangedListener {
    override fun onColorChanged(color: ColorTheme) {
        adapter?.updateColor(color)
    }

    override fun onLayoutTypeChanged(type: LayoutType) {
        val context = binding.recyclerView.context // Get context from the RecyclerView
        when (type) {
            LayoutType.GRID -> {
                binding.recyclerView.layoutManager = GridLayoutManager(context, 4)
            }
            else -> {
                binding.recyclerView.layoutManager = LinearLayoutManager(context)
            }
        }
    }

    override fun onLayoutType(type: LayoutType) {
        adapter?.changeLayout(type)
    }
}