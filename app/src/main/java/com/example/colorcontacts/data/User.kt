package com.example.colorcontacts.data

import android.net.Uri
import com.example.colorcontacts.Notification
import com.example.colorcontacts.utill.LayoutType
import java.util.UUID

data class User(
    var key: String = UUID.randomUUID().toString(),
    var img: Uri?,
    var name: String,
    var phone: String,
    var email: String,
    var event: String? = null,
    var info: String?,
)
object UserList{
    var userList = mutableListOf<User>()

    var layoutType = LayoutType.LINEAR

    var notification = Notification()
}
