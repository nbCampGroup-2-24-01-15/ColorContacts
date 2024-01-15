package com.example.colorcontacts.contactList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.colorcontacts.User
import com.example.colorcontacts.UserList

class ContactViewModel: ViewModel() {
    private val _contactList: MutableLiveData<MutableList<User>> = MutableLiveData()
    val contactList: LiveData<MutableList<User>> get() = _contactList

    private val _list: MutableLiveData<List<ContactViewType>> = MutableLiveData()
    val list : LiveData<List<ContactViewType>> get() = _list
    fun setContact(){
        _contactList.value = UserList.userList
    }
    fun onFavorite(position: Int) {
        UserList.userList[position].favorites = UserList.userList[position].favorites != true
    }

    fun setContactList(){
        _list.value = UserList.userList.map { ContactViewType.ContactUser(it) }
    }
}