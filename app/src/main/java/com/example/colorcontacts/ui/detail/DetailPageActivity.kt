package com.example.colorcontacts.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.colorcontacts.databinding.ActivityDetailPageBinding

class DetailPageActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailPageBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}