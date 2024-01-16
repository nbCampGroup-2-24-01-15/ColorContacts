package com.example.colorcontacts

import android.net.Uri
import java.util.UUID

sealed class Contacts {
    data class User(
        var key: String = UUID.randomUUID().toString(),
        var img: Uri?,
        var name: String,
        var phone: String,
        var email: String,
        var event: String? = null,
        var info: String?,
        var favorites: Boolean
    ): Contacts()
    object UserList{
        var userList = mutableListOf<User>()
    }

    data class My(
        var img: Uri?,
        var name: String,
        var phone: String,
        var email: String,
        var event: String? = null,
        var info: String?,
        var favorites: Boolean
    ): Contacts()
    object MyData{
        var myData = mutableListOf<My>()
    }
}
