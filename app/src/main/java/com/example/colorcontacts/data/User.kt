package com.example.colorcontacts.data

import com.example.colorcontacts.R
import com.example.colorcontacts.utill.LayoutType
import com.example.colorcontacts.utill.Notification
import java.util.UUID


data class User(
    var key: String = UUID.randomUUID().toString(),
    var img: String? = "android.resource://com.example.colorcontacts/"+ R.drawable.img_user_profile,
    var name: String="",
    var phone: String="",
    var email: String="",
    var event: String? = null,
    var info: String,
    var backgroundImg: String? = "android.resource://com.example.colorcontacts/"+ R.drawable.fill_vector
)

object MyData {

    var myData = User(
        img = "android.resource://com.example.colorcontacts/"+ R.drawable.img_user_profile,
        backgroundImg = "android.resource://com.example.colorcontacts/"+ R.drawable.fill_vector,
        name = "",
        phone = "",
        email = "",
        event = null,
        info = "",
    )



}

object UserList{

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
    val timeArray = arrayOf("X","10초","1분","10분","1시간")
}

