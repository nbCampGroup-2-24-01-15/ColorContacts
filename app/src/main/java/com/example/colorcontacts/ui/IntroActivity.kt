package com.example.colorcontacts.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.colorcontacts.R
import com.example.colorcontacts.data.User
import com.example.colorcontacts.data.UserList
import com.example.colorcontacts.databinding.ActivityIntroBinding
import com.example.colorcontacts.ui.main.MainActivity

@Suppress("DEPRECATION")
class IntroActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityIntroBinding.inflate(layoutInflater)
    }
    private var contactsLoaded = false

    private val blick by lazy {
        AnimationUtils.loadAnimation(this, R.anim.blink)
    }

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

    private fun requestContactPermission() {
        val status =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
        if (status == PackageManager.PERMISSION_GRANTED) {
            loadContacts()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                100
            )
        }


    }

    private fun requestCallPermission() {
        val callPermission =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
        if (callPermission == PackageManager.PERMISSION_GRANTED) {
            if (contactsLoaded) {
                startMotion()
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CALL_PHONE),
                55
            )
        }
    }

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
        }
    }

    private fun loadContacts() {
        Thread {
            getContacts()
            runOnUiThread {
                contactsLoaded = true
                requestCallPermission()
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
                if (photoUri != null) {
                    profileImageUri = Uri.parse(photoUri)
                }

                val user = User(
                    img = profileImageUri,
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
        UserList.userList.sortBy { it.name }
    }
}
