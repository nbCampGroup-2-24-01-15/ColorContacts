package com.example.colorcontacts.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colorcontacts.UserList
import com.example.colorcontacts.contactList.ContactAdapter
import com.example.colorcontacts.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {
    private var adapter: FavoriteAdapter? = null

    private var _binding: FragmentFavoriteBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        setList()
    }

    private fun setList() {
        var list = mutableListOf<FavoriteViewType>()
        UserList.userList.forEach { if (it.favorites)list.add(FavoriteViewType.FavoriteUser(it)) }
        adapter = FavoriteAdapter(list)
        binding.rcFavoriteList.adapter = adapter
        binding.rcFavoriteList.layoutManager = LinearLayoutManager(requireContext())
        adapter?.itemClick = object : FavoriteAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                UserList.userList[position].favorites = UserList.userList[position].favorites != true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}