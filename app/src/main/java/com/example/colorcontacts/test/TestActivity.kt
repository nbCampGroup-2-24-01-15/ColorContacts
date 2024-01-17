package com.example.colorcontacts.test

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.colorcontacts.Manifest
import com.example.colorcontacts.R
import com.example.colorcontacts.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var button: Button
    private var imageUri: Uri? = null

    // 갤러리 open
    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
               /* openGallery()*/
            }
        }
    // 가져온 사진 보여주기
    private val pickImageLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let {
                    imageUri = it
                    imageView.setImageURI(imageUri)
                }
            }
        }


    private val binding by lazy {
        ActivityTestBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = "Edit Profile"
    }

/*        init()
    }

    private fun init(){
        binding.testImg.setOnClickListener {*/
            /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery()
                // 버튼 텍스트 숨기기
                button.text = null
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)*/
            }
//        }
/*

        binding.testTeg.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivity(intent)
        }
    }
*/

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
/*
        pickImageLauncher.launch(gallery)
*/
    }
}