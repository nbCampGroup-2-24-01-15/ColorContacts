package com.example.colorcontacts.data

import android.content.Context
import android.net.Uri
import com.example.colorcontacts.utill.LayoutType
import com.example.colorcontacts.utill.Notification
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import java.util.UUID


data class User(
    var key: String = UUID.randomUUID().toString(),
    var img: File? = null,
    var name: String="",
    var phone: String="",
    var email: String="",
    var event: String? = null,
    var info: String,
    var backgroundImg: File? = null
)

object MyData {

    var myData = User(
        key = "My",
        img = null,
        backgroundImg = null,
        name = "Me",
        phone = "010-1321-4432",
        email = "me@sparta.com",
        event = null,
        info = "안녕하세요 마이페이지 입니다",
    )

    fun Context.copyResourceToFile(resId: Int): File {
        val inputStream = resources.openRawResource(resId)
        val outputFile = File(cacheDir, "temp_resource_file")
        val outputStream = FileOutputStream(outputFile)

        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return outputFile
    }

    fun sortContacts(contacts: MutableList<User>): List<User> {
        val collator = java.text.Collator.getInstance(Locale.KOREAN)
        return contacts.sortedWith { contact1, contact2 ->
            collator.compare(contact1.name, contact2.name)
        }
    }


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

