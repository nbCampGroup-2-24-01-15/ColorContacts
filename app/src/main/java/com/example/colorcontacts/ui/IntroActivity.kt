package com.example.colorcontacts.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.colorcontacts.R
import com.example.colorcontacts.data.MyData
import com.example.colorcontacts.data.User
import com.example.colorcontacts.data.UserList
import com.example.colorcontacts.databinding.ActivityIntroBinding
import com.example.colorcontacts.ui.main.MainActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Suppress("DEPRECATION")
class IntroActivity : AppCompatActivity() {
    companion object {
        private val callPermission = android.Manifest.permission.CALL_PHONE
        private val mediaPermission = android.Manifest.permission.READ_EXTERNAL_STORAGE
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        private val imagePermission = android.Manifest.permission.READ_MEDIA_IMAGES
    }

    private val binding by lazy {
        ActivityIntroBinding.inflate(layoutInflater)
    }
    private var contactsLoaded = false

    private val blick by lazy {
        AnimationUtils.loadAnimation(this, R.anim.blink)
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        supportActionBar?.hide()

        requestContactPermission()

        binding.introMotion.setOnClickListener {
            binding.introMotion.transitionToEnd()
            startActivity(Intent(this@IntroActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun goMain() {
        binding.introMotion.setTransitionDuration(4000)
        binding.introMotion.transitionToEnd()

        binding.introMotion.setOnClickListener {
            startActivity(Intent(this@IntroActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun startMotion() {
        binding.introMotion.apply {
            transitionToStart()
            binding.tvIntroTouch.startAnimation(blick)
            Handler(Looper.getMainLooper()).postDelayed({
                goMain()
            }, 0)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestContactPermission() {
        val status =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
        if (status == PackageManager.PERMISSION_GRANTED) {
            loadContacts()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_CONTACTS,android.Manifest.permission.WRITE_CONTACTS),
                100
            )
        }
    }

    private fun requestCallPermission() {
        val callPermission =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)

        if (callPermission == PackageManager.PERMISSION_GRANTED
        ) {
            if (contactsLoaded) {
                startMotion()
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.CALL_PHONE
                ),
                55
            )
        }
    }

    private fun checkPermissionsAndStartMotion(permissions: Array<String>, requestCode: Int) {
        val permissionResults = permissions.map {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }

        if (permissionResults.all { it }) {
            if (contactsLoaded) {
                startMotion()
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, requestCode)
        }
    }

    private fun requestCallAndStoragePermission() {
        checkPermissionsAndStartMotion(arrayOf(callPermission, mediaPermission), 55)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestTiramisuPermission() {
        checkPermissionsAndStartMotion(arrayOf(callPermission, imagePermission), 200)
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadContacts()
                } else {
                    finish()
                }
            }

            55 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (contactsLoaded) startMotion()
                } else {
                    finish()
                }
            }

            200 -> {
                // API 33 이상 권한 요청
                var allPermissionsGranted = true
                for (result in grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        allPermissionsGranted = false
                        break
                    }
                }

                if (allPermissionsGranted && contactsLoaded) {
                    startMotion()
                } else {
                    finish()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun loadContacts() {
        Thread {
            getContacts()
            runOnUiThread {
                contactsLoaded = true
//                requestCallPermission()
                // API 버전 확인 후 권한 요청
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestTiramisuPermission()
                } else {
                    requestCallAndStoragePermission()
                }
            }
        }.start()
    }


    @SuppressLint("Range")
    private fun getContacts() {
        UserList.userList = mutableListOf()
        val contactsUri = ContactsContract.Contacts.CONTENT_URI
        val cursor = contentResolver.query(contactsUri, null, null, null, null)

        cursor?.use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                var phoneNumber: String? = null
                var email: String? = null
                var profileImageUri: Uri? = null

                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    phoneCursor?.use {
                        if (it.moveToFirst()) {
                            phoneNumber =
                                it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        }
                    }
                }

                val emailCursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", arrayOf(id), null
                )
                emailCursor?.use {
                    if (it.moveToFirst()) {
                        email =
                            it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
                    }
                }

                val photoUri =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))
                val proimg = if (photoUri != null) {
                    savImageToFile(Uri.parse(photoUri))
                } else null
                val user = User(
                    img = proimg,
                    name = name ?: "Unknown",
                    phone = phoneNumber ?: "No Phone",
                    email = email ?: "",
                    event = null,
                    info = "",
                    backgroundImg = null
                )
                UserList.userList.add(user)
            }
        }
        UserList.userList = MyData.sortContacts(UserList.userList).toMutableList()
    }

    fun savImageToFile(imageUri: Uri): File? {
        val contentResolver = this.contentResolver
        val inputStream = contentResolver.openInputStream(imageUri) ?: return null
        val outputFile = createImageFile() ?: return null

        inputStream.use { input ->
            FileOutputStream(outputFile).use { output ->
                val buffer = ByteArray(1024)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
        }

        return outputFile
    }

    private fun createImageFile(): File? {
        val imageFileName = "JPEG_" + System.currentTimeMillis() + "_"
        val storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return try {
            File.createTempFile(imageFileName, ".jpg", storageDir)
        } catch (e: IOException) {
            null
        }
    }
}
