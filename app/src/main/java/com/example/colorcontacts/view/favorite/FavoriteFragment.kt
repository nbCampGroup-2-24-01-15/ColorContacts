package com.example.colorcontacts.view.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colorcontacts.data.TagMember
import com.example.colorcontacts.utill.LayoutType
import com.example.colorcontacts.utill.SharedViewModel
import com.example.colorcontacts.databinding.FragmentFavoriteBinding
import com.example.colorcontacts.view.favorite.adapter.FavoriteAdapter
import com.example.colorcontacts.view.favorite.adapter.FavoriteItemHelper
import com.example.colorcontacts.view.favorite.model.FavoriteViewModel

class FavoriteFragment : Fragment() {
    private var adapter: FavoriteAdapter? = null

    private var _binding: FragmentFavoriteBinding? = null

    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var viewModel: FavoriteViewModel
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        viewModel = ViewModelProvider(requireActivity())[FavoriteViewModel::class.java]
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        sharedViewModel.setLayoutType()
        setList()
    }

    private fun setList() {
        viewModel.favoriteList.observe(viewLifecycleOwner){ list ->
            if (adapter == null) {
                adapter = FavoriteAdapter(list).apply {
                    itemClick = object : FavoriteAdapter.ItemClick {
                        override fun onClick(view: View, position: Int, key: String) {
                            //태그 목록에 있을때
                            if (TagMember.totalTags.any { it.member.contains(key) }) sharedViewModel.offFavorite(key)
                            else sharedViewModel.onFavorite(key)
                        }
                    }
                }
                binding.rcFavoriteList.adapter = adapter
            } else {
                adapter?.load(list)
            }
        }

        sharedViewModel.layoutType.observe(viewLifecycleOwner) { type ->
            when(type){
                LayoutType.GRID -> {
                    binding.rcFavoriteList.layoutManager = GridLayoutManager(requireContext(), 4)
                }
                else -> {
                    binding.rcFavoriteList.layoutManager = LinearLayoutManager(requireContext())
                }
            }
            viewModel.setFavoriteList(type)
        }

        val itemHelper = ItemTouchHelper(FavoriteItemHelper(requireContext()))
        itemHelper.attachToRecyclerView(binding.rcFavoriteList)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        setList()
    }

    /**
     * TODO Fragment RecyclerView 검색
     */
    fun updateItem(text: String) {
        adapter?.performSearch(text)
    }
}