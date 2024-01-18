package com.example.colorcontacts.utill

import com.example.colorcontacts.data.ColorTheme
import com.example.colorcontacts.ui.contactList.adapter.ContactViewType
import com.example.colorcontacts.ui.favorite.adapter.FavoriteViewType

interface OnSharedDataListener {
    // 즐찾
    fun onFavorite(key: String)
    // 즐찾 취소
    fun offFavorite(key: String)
    //일반 연락처 뷰타입 지정
    fun setContactList(type: LayoutType): List<ContactViewType>
    //즐겨찾기 리스트 뷰타입 지정
    fun setFavoriteList(type: LayoutType): List<FavoriteViewType>
}