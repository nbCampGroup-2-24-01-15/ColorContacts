package com.example.colorcontacts.contactList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.colorcontacts.LayoutType
import com.example.colorcontacts.UserList
import com.example.colorcontacts.favorite.FavoriteViewType

class ContactViewModel: ViewModel() {

    private val _list: MutableLiveData<List<ContactViewType>> = MutableLiveData()
    val list : LiveData<List<ContactViewType>> get() = _list

    private val _favoriteList: MutableLiveData<List<FavoriteViewType>> = MutableLiveData()
    val favoriteList : LiveData<List<FavoriteViewType>> get() = _favoriteList

    private val _layoutType: MutableLiveData<LayoutType> = MutableLiveData(LayoutType.LINEAR)
    val layoutType : LiveData<LayoutType> get() = _layoutType

    //즐겨찾기 표시
    fun onFavorite(position: Int) {
        UserList.userList[position].favorites = UserList.userList[position].favorites != true
    }

    //레이아웃 타입을 구별하고 실드클래스에 넣어줌
    fun setContactList(type: LayoutType){
        if (type == LayoutType.LINEAR) _list.value = UserList.userList.map { ContactViewType.ContactUser(it) }
        else _list.value = UserList.userList.map { ContactViewType.GridUser(it) }
    }

    //즐겨찾기 목록에 있는 유저를 레이아웃타입을 구별하고 실드클래스에 넣어줌
    fun setFavoriteList(type: LayoutType){
        if (type == LayoutType.LINEAR) _favoriteList.value = UserList.userList.filter { it.favorites }.map { FavoriteViewType.FavoriteUser(it) }
        else _favoriteList.value = UserList.userList.filter { it.favorites }.map { FavoriteViewType.FavoriteGrid(it) }
    }

    //레이아웃 변경 버튼을 눌렀을때 갱신
    fun getLayoutType() {
        UserList.layoutType = if (_layoutType.value == LayoutType.LINEAR) LayoutType.GRID else LayoutType.LINEAR
        _layoutType.value = UserList.layoutType
    }

    //프래그먼트창이 켜질때 레이아웃타입 갱신
    fun setLayoutType() {
        _layoutType.value = UserList.layoutType
    }
}