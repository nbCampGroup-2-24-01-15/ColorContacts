package com.example.colorcontacts.contactList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.colorcontacts.User
import com.example.colorcontacts.UserList
import com.example.colorcontacts.favorite.FavoriteViewType

class ContactViewModel: ViewModel() {

    private val _list: MutableLiveData<List<ContactViewType>> = MutableLiveData()
    val list : LiveData<List<ContactViewType>> get() = _list

    private val _favoriteList: MutableLiveData<List<FavoriteViewType>> = MutableLiveData()
    val favoriteList : LiveData<List<FavoriteViewType>> get() = _favoriteList

    fun onFavorite(position: Int) {
        UserList.userList[position].favorites = UserList.userList[position].favorites != true
    }

    fun setContactList(){
        _list.value = UserList.userList.map { ContactViewType.ContactUser(it) }
    }

    fun setFavoriteList(){
        _favoriteList.value = UserList.userList.filter { it.favorites }.map { FavoriteViewType.FavoriteUser(it) }
    }
}