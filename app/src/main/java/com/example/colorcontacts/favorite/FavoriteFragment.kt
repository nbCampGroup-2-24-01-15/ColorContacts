package com.example.colorcontacts.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colorcontacts.UserList
import com.example.colorcontacts.contactList.ContactAdapter
import com.example.colorcontacts.contactList.ContactViewModel
import com.example.colorcontacts.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {
    private var adapter: FavoriteAdapter? = null

    private var _binding: FragmentFavoriteBinding? = null

    private lateinit var viewModel: ContactViewModel
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[ContactViewModel::class.java]
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        setList()
    }

    private fun setList() {
        viewModel.setFavoriteList()
        viewModel.favoriteList.observe(requireActivity()) { list ->
            adapter = FavoriteAdapter(list)
            adapter!!.load()
        }
        binding.rcFavoriteList.adapter = adapter
        binding.rcFavoriteList.layoutManager = LinearLayoutManager(requireContext())
        adapter?.itemClick = object : FavoriteAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                viewModel.onFavorite(position)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        setList()
    }
}