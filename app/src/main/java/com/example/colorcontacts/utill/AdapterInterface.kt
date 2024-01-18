package com.example.colorcontacts.utill

import com.example.colorcontacts.data.ColorTheme

interface AdapterInterface {
    fun updateColor(newColorTheme: ColorTheme)
    fun changeLayout(layoutType: LayoutType)
    fun notifyDataSetChanged()
}