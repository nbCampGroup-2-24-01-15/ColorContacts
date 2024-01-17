package com.example.colorcontacts.view.contactList.adapter

import com.example.colorcontacts.data.User

sealed class ContactViewType{
    data class ContactUser(val user: User) : ContactViewType()
    data class GridUser(val user: User) : ContactViewType()
}
