package com.example.colorcontacts.contactList

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.colorcontacts.Manifest
import com.example.colorcontacts.R
import com.example.colorcontacts.databinding.FragmentContactListBinding


class ContactListFragment : Fragment() {

    private var adapter: ContactAdapter? = null

    private var _binding: FragmentContactListBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactListBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        /*requestContactPermission()
        getContacts()
    }

    private fun getContacts() {
        val contactsList = mutableListOf<>()
    }

    private fun requestContactPermission() {
        //사용자가 퍼미션 허용 했는지 확인
        val status = ContextCompat.checkSelfPermission(requireContext(), "android.permission.READ_CONTACTS")
        if (status == PackageManager.PERMISSION_GRANTED) Log.d("Test", "permission granted")
        else {
            //퍼미션 요청 다이얼로그 표시
            ActivityCompat.requestPermissions(requireContext() as Activity, arrayOf<String>("android.permission.READ_CONTACTS"), 100)
            Log.d("Test", "permission denied")
        }*/
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}