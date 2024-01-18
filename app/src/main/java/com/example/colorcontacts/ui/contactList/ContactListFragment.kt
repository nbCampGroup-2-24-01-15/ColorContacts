package com.example.colorcontacts.ui.contactList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colorcontacts.data.NowColor
import com.example.colorcontacts.data.TagMember
import com.example.colorcontacts.data.UserList
import com.example.colorcontacts.databinding.FragmentContactListBinding
import com.example.colorcontacts.ui.contactList.adapter.ContactAdapter
import com.example.colorcontacts.ui.contactList.adapter.ContactItemHelper
import com.example.colorcontacts.utill.DataChangedListener
import com.example.colorcontacts.utill.RecyclerViewBindingWrapper
import com.example.colorcontacts.utill.SharedDataListener


class ContactListFragment : Fragment() {

    private val bindingWrapper by lazy {
        RecyclerViewBindingWrapper(binding)
    }
    val dataChangedListener by lazy {
        DataChangedListener(adapter, bindingWrapper)
    }
    private val adapter by lazy {
        ContactAdapter(emptyList(), NowColor.color, binding.tvContactList)
    }

    val sharedDataListener = SharedDataListener()
    private val binding by lazy {
        FragmentContactListBinding.inflate(layoutInflater)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcContactList.adapter = adapter
        Log.d("ContactListFragment", "Adapter initialized with empty list")
        dataChangedListener

        init()
        // other initialization
    }
    private fun init() {
        val loadedData = sharedDataListener.setContactList(UserList.layoutType)
        adapter.load(loadedData ?: emptyList())
        binding.rcContactList.layoutManager = LinearLayoutManager(context)

        setList()
        Log.d("ContactListFragment", "Loaded data: $loadedData")
    }

    /**
     * TODO 연락처 리스트 세팅
     *
     * enum class로 layout타입을 나눠서 구별
     * 현재 타입에 맞춰 sealed class의 ContactUser와 GridUser타입으로 어댑터에 넣어줌
     * sealed class(ContactViewType)의 데이터 클래스에 따라 레이아웃을 어댑터에서 설정함
     */
    private fun setList() {
        dataChangedListener.onColorChanged(NowColor.color)
        dataChangedListener.onLayoutTypeChanged(UserList.layoutType)
        val data = sharedDataListener.setContactList(UserList.layoutType)
        //현재 보고 있는 레이아웃 타입 설정, 버튼도 그에 맞춰 변경 -> 컬러랑 레이아웃타입은 메인페이지에서 버튼 누를때 변경!

        data.let { adapter.load(it) }
        Log.d("ContactListFragment", "Data loaded into adapter, size: ${adapter.itemCount}")
        adapter.itemClick = object : ContactAdapter.ItemClick{
            override fun onClick(view: View, position: Int, key: String) {
                Log.d("ContactListFragment", "Item clicked - Position: $position, Key: $key")
                if (TagMember.totalTags.any { it.member.contains(key) }) sharedDataListener.offFavorite(key)
                else sharedDataListener.onFavorite(key)
            }
        }
        //스와이프 통화
        val itemTouchHelper = ItemTouchHelper(ContactItemHelper(requireContext()))
        itemTouchHelper.attachToRecyclerView(binding.rcContactList)
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