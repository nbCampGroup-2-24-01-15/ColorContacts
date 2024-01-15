package com.example.colorcontacts.contactList

import com.example.colorcontacts.User

sealed class ContactViewType{
    data class ContactUser(val user: User) : ContactViewType()
}
