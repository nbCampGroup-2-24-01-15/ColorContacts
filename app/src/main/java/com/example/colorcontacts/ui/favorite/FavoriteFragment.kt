package com.example.colorcontacts.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colorcontacts.data.NowColor
import com.example.colorcontacts.data.TagMember
import com.example.colorcontacts.data.UserList
import com.example.colorcontacts.databinding.FragmentFavoriteBinding
import com.example.colorcontacts.ui.favorite.adapter.FavoriteAdapter
import com.example.colorcontacts.ui.favorite.adapter.FavoriteItemHelper
import com.example.colorcontacts.utill.DataChangedListener
import com.example.colorcontacts.utill.RecyclerViewBindingWrapper
import com.example.colorcontacts.utill.SharedDataListener

class FavoriteFragment : Fragment() {

    private val bindingWrapper by lazy {
        RecyclerViewBindingWrapper(binding)
    }
    val dataChangedListener by lazy {
        DataChangedListener(adapter, bindingWrapper)
    }
    private val adapter by lazy {
        FavoriteAdapter(emptyList(), NowColor.color)
    }

    val sharedDataListener = SharedDataListener()
    private val binding by lazy {
        FragmentFavoriteBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcFavoriteList.adapter = adapter
        dataChangedListener

        init()
    }

    private fun init() {
        val loadedData = sharedDataListener.setFavoriteList(UserList.layoutType)
        adapter.load(loadedData ?: emptyList())
        binding.rcFavoriteList.layoutManager = LinearLayoutManager(context)
        setList()
    }

    private fun setList() {
        dataChangedListener.onColorChanged(NowColor.color)
        dataChangedListener.onLayoutTypeChanged(UserList.layoutType)
        val data = sharedDataListener.setFavoriteList(UserList.layoutType)
        adapter.load(data)

        adapter.itemClick = object : FavoriteAdapter.ItemClick{
            override fun onClick(view: View, position: Int, key: String) {
                if (TagMember.totalTags.any { it.member.contains(key) }) sharedDataListener.offFavorite(key)
                else sharedDataListener.onFavorite(key)
            }
        }

        val itemHelper = ItemTouchHelper(FavoriteItemHelper(requireContext()))
        itemHelper.attachToRecyclerView(binding.rcFavoriteList)
    }

    override fun onResume() {
        super.onResume()
        setList()
    }

    /**
     * TODO Fragment RecyclerView 검색
     */
    fun updateItem(text: String) {
        adapter.performSearch(text)
    }
}