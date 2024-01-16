package com.example.colorcontacts.data

import android.graphics.Color

data class ColorTheme(
    var colorWidget: Int = Color.parseColor("#a2d2ff"),
    var colorSearch: Int = Color.parseColor("#bde0f2"),
    var colorIcon: Int = Color.parseColor("#07B2FF"),
    var colorFont: Int = Color.parseColor("#000000"),
    var colorBasic: Int = Color.parseColor("#e9e9e9"),
    var colorSelect: Int = Color.parseColor("#11264f"),
    var colorLinear: Int = Color.parseColor("#ffffff"),
    var colorHeader: Int = Color.parseColor("#ffffff"),
    var colorBackground: Int = Color.parseColor("#D3D3D3")
)

object NowColor {
    var color: ColorTheme = ColorTheme()
}