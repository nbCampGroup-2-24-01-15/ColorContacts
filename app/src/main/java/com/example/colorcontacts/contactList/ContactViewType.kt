package com.example.colorcontacts.contactList

import com.example.colorcontacts.Contacts
//import com.example.colorcontacts.User

sealed class ContactViewType{
    data class ContactUser(val user: Contacts.User) : ContactViewType()
    data class ContactMy(val my: Contacts.My) : ContactViewType()
}
