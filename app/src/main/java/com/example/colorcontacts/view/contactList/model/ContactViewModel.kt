package com.example.colorcontacts.view.contactList.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.colorcontacts.data.UserList
import com.example.colorcontacts.utill.LayoutType
import com.example.colorcontacts.view.contactList.adapter.ContactViewType

class ContactViewModel: ViewModel() {

    private val _list: MutableLiveData<List<ContactViewType>> = MutableLiveData()
    val list: LiveData<List<ContactViewType>> get() = _list

    //레이아웃 타입을 구별하고 실드클래스에 넣어줌
    fun setContactList(type: LayoutType) {
        if (type == LayoutType.LINEAR) _list.value = UserList.userList.map { ContactViewType.ContactUser(it) }
        else _list.value = UserList.userList.map { ContactViewType.GridUser(it) }
    }
}