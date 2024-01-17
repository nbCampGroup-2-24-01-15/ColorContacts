package com.example.colorcontacts.view.dialpad

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.colorcontacts.R

class DialPadFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dial_pad, container, false)
    }

    override fun onResume() {
        super.onResume()/*
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:010"))
        startActivity(intent)*/
    }

}