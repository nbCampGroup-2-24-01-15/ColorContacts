package com.example.colorcontacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.colorcontacts.databinding.ActivityMainBinding
import com.example.colorcontacts.dialog.AddContactDialog

class MainActivity : AppCompatActivity() {
    private val binding by lazy{ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            AddContactDialog(this).show()
        }
    }
}