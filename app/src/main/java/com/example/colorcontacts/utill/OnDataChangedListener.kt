package com.example.colorcontacts.utill

import com.example.colorcontacts.data.ColorTheme

interface OnDataChangedListener {
    //리사이클러뷰 색변경
    fun onColorChanged(color: ColorTheme)
    //리사이클러뷰 타입변경
    fun onLayoutTypeChanged(type: LayoutType)
    //리사이클러뷰 데이터의 타입변경
    fun onLayoutType(type: LayoutType)
}