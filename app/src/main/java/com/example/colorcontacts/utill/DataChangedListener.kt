package com.example.colorcontacts.utill

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colorcontacts.data.ColorTheme

class DataChangedListener(private val adapter: AdapterInterface?, private val binding: RecyclerViewBindingWrapper) : OnDataChangedListener {
    override fun onColorChanged(color: ColorTheme) {
        adapter?.updateColor(color)
    }

    override fun onLayoutTypeChanged(type: LayoutType) {
        val context = binding.recyclerView.context
        when (type) {
            LayoutType.GRID -> {
                val gridManager = GridLayoutManager(context, 4)
                gridManager.spanSizeLookup = object :SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (adapter?.getItemViewType(position)){
                            2 -> 4
                            3 -> 4
                            else -> 1
                        }
                    }

                }
                binding.recyclerView.layoutManager = gridManager
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