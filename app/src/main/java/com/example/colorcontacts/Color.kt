package com.example.colorcontacts

import android.graphics.Color

data class ColorTheme(

    var colorWidget: Int = Color.parseColor("#a2d2ff"),

    var colorSearch: Int = Color.parseColor("#bde0f2"),

    var colorIcon: Int = Color.parseColor("#07B2FF"),

    var colorFont: Int = Color.parseColor("#000000"),

    var colorBasic: Int = Color.parseColor("e9e9e9"),

    var colorSelect: Int = Color.parseColor("#11264f"),

    var colorList: Int = Color.parseColor("#ffffff")
)
object NowColor{
    var color:ColorTheme = ColorTheme()
}