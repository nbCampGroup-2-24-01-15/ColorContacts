package com.example.colorcontacts.ui.contactList.adapter

import com.example.colorcontacts.data.User

sealed class ContactViewType {
    data class ContactUser(val user: User) : ContactViewType()
    data class GridUser(val user: User) : ContactViewType()
    data class Header(val title: String) : ContactViewType()
    data class MyProfile(val user: User) : ContactViewType()
}
