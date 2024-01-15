package com.example.colorcontacts

import android.net.Uri
import java.util.UUID

data class User(
    var key: String = UUID.randomUUID().toString(),
    var img: Uri?,
    var name: String,
    var phone: String,
    var email: String,
    var event: String? = null,
    var info: String?,
    var favorites: Boolean
)
object UserList{
    var userList = mutableListOf<User>()
}