package com.example.colorcontacts.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colorcontacts.LayoutType
import com.example.colorcontacts.R
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
        viewModel.setLayoutType()
        setList()
        setLayoutBtn()
    }

    private fun setList() {
        viewModel.favoriteList.observe(viewLifecycleOwner){ list ->
            if (adapter == null) {
                adapter = FavoriteAdapter(list).apply {
                    itemClick = object : FavoriteAdapter.ItemClick {
                        override fun onClick(view: View, position: Int) {
                            viewModel.onFavorite(position)
                        }
                    }
                }
                binding.rcFavoriteList.adapter = adapter
            } else {
                adapter?.load(list)
            }
        }

        viewModel.layoutType.observe(viewLifecycleOwner) { type ->
            when(type){
                LayoutType.GRID -> {
                    binding.ivFavoriteLayout.setImageResource(R.drawable.ic_fragment_linear)
                    binding.rcFavoriteList.layoutManager = GridLayoutManager(requireContext(), 4)
                }
                else -> {
                    binding.ivFavoriteLayout.setImageResource(R.drawable.ic_fragment_grid)
                    binding.rcFavoriteList.layoutManager = LinearLayoutManager(requireContext())
                }
            }
            viewModel.setFavoriteList(type)
        }

        val itemHelper = ItemTouchHelper(FavoriteItemHelper(requireContext()))
        itemHelper.attachToRecyclerView(binding.rcFavoriteList)
    }

    private fun setLayoutBtn() {
        binding.ivFavoriteLayout.setOnClickListener {
            viewModel.getLayoutType()
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