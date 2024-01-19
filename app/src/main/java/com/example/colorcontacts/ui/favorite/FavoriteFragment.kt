package com.example.colorcontacts.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colorcontacts.data.NowColor
import com.example.colorcontacts.data.Tag
import com.example.colorcontacts.data.TagMember
import com.example.colorcontacts.data.TagMember.totalTags
import com.example.colorcontacts.data.UserList
import com.example.colorcontacts.databinding.FragmentFavoriteBinding
import com.example.colorcontacts.dialog.AddFavoriteTagDialog
import com.example.colorcontacts.ui.favorite.adapter.FavoriteAdapter
import com.example.colorcontacts.ui.favorite.adapter.FavoriteItemHelper
import com.example.colorcontacts.ui.favorite.adapter.FavoriteTagAdapter
import com.example.colorcontacts.ui.favorite.adapter.FavoriteViewType
import com.example.colorcontacts.utill.DataChangedListener
import com.example.colorcontacts.utill.RecyclerViewBindingWrapper
import com.example.colorcontacts.utill.SharedDataListener
import java.io.File

class FavoriteFragment : Fragment(), AddFavoriteTagDialog.OnTagAddListener {

    companion object {
        const val DIALOG_TAG = "AddTagDialog"
    }

    private val bindingWrapper by lazy {
        RecyclerViewBindingWrapper(binding)
    }
    val dataChangedListener by lazy {
        DataChangedListener(adapter, bindingWrapper)
    }
    private val adapter by lazy {
        FavoriteAdapter(emptyList(), NowColor.color, binding.tvFavoriteList)
    }

    private var tagAdapter: FavoriteTagAdapter? = null

    val sharedDataListener = SharedDataListener()
    private val binding by lazy {
        FragmentFavoriteBinding.inflate(layoutInflater)
    }

    private var loadedData: List<FavoriteViewType> = mutableListOf()

    private var filteredFavoriteList: List<FavoriteViewType> = emptyList()

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
        loadedData = sharedDataListener.setFavoriteList(UserList.layoutType)
        adapter.load(loadedData)
        binding.rcFavoriteList.layoutManager = LinearLayoutManager(context)
        setList()
        setFavoriteTypeAdapter()
        onButtonAction()
    }

    private fun onButtonAction() {
        /**
         * 태그 추가
         */
        binding.ivAddTag.setOnClickListener { // 태그 추가
            val dialog = AddFavoriteTagDialog()
            dialog.setOnTagAddListener(this@FavoriteFragment)
            dialog.show(requireActivity().supportFragmentManager, DIALOG_TAG)
        }

        /**
         * 전체 리스트
         */
        binding.ivFullList.setOnClickListener { // 전체 리스트
            loadedData = sharedDataListener.setFavoriteList(UserList.layoutType)
            adapter.load(loadedData)
        }
    }

    private fun setList() {
        dataChangedListener.onColorChanged(NowColor.color)
        dataChangedListener.onLayoutTypeChanged(UserList.layoutType)
        loadedData = sharedDataListener.setFavoriteList(UserList.layoutType)
        adapter.load(loadedData)

        adapter.itemClick = object : FavoriteAdapter.ItemClick {
            override fun onClick(view: View, position: Int, key: String) {
                if (totalTags.any { it.member.contains(key) }) sharedDataListener.offFavorite(
                    key
                )
                else sharedDataListener.onFavorite(key)
            }
        }

        val itemHelper = ItemTouchHelper(FavoriteItemHelper(requireContext()))
        itemHelper.attachToRecyclerView(binding.rcFavoriteList)
    }

    override fun onResume() {
        super.onResume()
        setList()
        setFavoriteTypeAdapter()
    }

    /**
     * Fragment RecyclerView 검색
     */
    fun updateItem(text: String) {
        adapter.performSearch(text)
    }

    /**
     * 즐겨 찾기 태그
     * 태그에 해당 하는 RecyclerView의 아이템 보여 주기
     */
    private fun setFavoriteTypeAdapter() {
        val items = totalTags
        tagAdapter = FavoriteTagAdapter(items)
        binding.favoriteRecyclerView.adapter = tagAdapter
        tagAdapter?.itemClick = object : FavoriteTagAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                filteredFavoriteList = filteredTag(loadedData, items[position].img)
                adapter.load(filteredFavoriteList)
            }
        }
    }

    /**
     * 다이얼로그에서 받은 데이터
     */
    override fun onTagAdd(name: String, uriad: File) {
        TagMember.addNewTag(Tag(name, uriad))
        tagAdapter?.updateItem(totalTags)
    }

    private fun filteredTag(
        list: List<FavoriteViewType>,
        uri: File?/* = TagMember.defaultTag.img*/
    ): List<FavoriteViewType> {
        return list.filter {
            val userKey = when (it) {
                is FavoriteViewType.FavoriteUser -> it.user.key
                is FavoriteViewType.FavoriteGrid -> it.user.key
            }
            TagMember.memberChk(userKey)?.img == uri
        }
    }

}