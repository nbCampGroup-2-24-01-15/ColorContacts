package com.example.colorcontacts.contactList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colorcontacts.UserList
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
    }

    private fun setList() {
        val list = UserList.userList.map { ContactViewType.ContactUser(it) }
        adapter = ContactAdapter(list)
        binding.rcContactList.adapter = adapter
        binding.rcContactList.layoutManager = LinearLayoutManager(requireContext())
        adapter?.itemClick = object : ContactAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                setFavorite(position)
            }
        }
    }

    private fun setFavorite(position: Int) {
        UserList.userList[position].favorites = UserList.userList[position].favorites != true
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}