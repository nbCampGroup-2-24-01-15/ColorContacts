package com.example.colorcontacts.data

import android.net.Uri
import com.example.colorcontacts.utill.LayoutType
import com.example.colorcontacts.utill.Notification
import java.io.File
import java.util.UUID


data class User(
    var key: String = UUID.randomUUID().toString(),
    var img: File?,
    var name: String="",
    var phone: String="",
    var email: String="",
    var event: String? = null,
    var info: String,
    var backgroundImg: File?
)

object MyData {

    var myData = User(
        img =null,
        backgroundImg = null,
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

    fun getURLForResource(resId: Int): String {
        val packageName = javaClass.`package`?.name
        return Uri.parse("android.resource://$packageName/$resId").toString() }
}

/**
 *  이벤트에 대한 알림의 시간을 저장
 */
object EventTime{
    val timeArray = arrayOf("X","10초","1분","10분","1시간")
}

