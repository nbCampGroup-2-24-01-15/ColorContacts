package com.example.colorcontacts.data

import android.net.Uri
import com.example.colorcontacts.utill.LayoutType
import com.example.colorcontacts.utill.Notification
import java.util.UUID


data class User(
    var key: String = UUID.randomUUID().toString(),
    var img: Uri?,
    var name: String,
    var phone: String,
    var email: String,
    var event: String? = null,
    var info: String?,
    var backgroundImg: Uri?
)

object UserList{
    var myData = mutableListOf<User>()

    var userList = mutableListOf<User>()

    var layoutType = LayoutType.LINEAR

    var notification = Notification()



    fun findUser(key:String) : User? {
        return userList.find { it.key == key }
    }
}

/**
 *  이벤트에 대한 알림의 시간을 저장
 */
object EventTime{
    val timeArray = arrayOf("5초","10초","1분","10분","1시간")
}

