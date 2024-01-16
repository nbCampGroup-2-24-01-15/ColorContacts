package com.example.colorcontacts.contactList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colorcontacts.Contacts
//import com.example.colorcontacts.UserList
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
        setList()
        Log.e("user", "${Contacts.UserList.userList}")
    }

    private fun setList() {
        val myData = Contacts.MyData.myData.map { ContactViewType.ContactMy(it) }
        val contactList = Contacts.UserList.userList.map { ContactViewType.ContactUser(it) }
        val list = mutableListOf(myData + contactList)
        adapter = ContactAdapter(contactList)
        binding.rcContactList.adapter = adapter
        binding.rcContactList.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}