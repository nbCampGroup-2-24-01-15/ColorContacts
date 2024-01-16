package com.example.colorcontacts.view.contactList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colorcontacts.data.ColorTheme
import com.example.colorcontacts.utill.LayoutType
import com.example.colorcontacts.data.NowColor
import com.example.colorcontacts.databinding.FragmentContactListBinding
import com.example.colorcontacts.utill.SharedViewModel


class ContactListFragment : Fragment() {

    private var adapter: ContactAdapter? = null

    private var _binding: FragmentContactListBinding? = null
    private var nowList: List<ContactViewType>? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        _binding = FragmentContactListBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        //레이아웃 타입 초기화
        viewModel.setLayoutType()
        setList()
    }

    /**
     * TODO 연락처 리스트 세팅
     *
     * enum class로 layout타입을 나눠서 구별
     * 현재 타입에 맞춰 sealed class의 ContactUser와 GridUser타입으로 어댑터에 넣어줌
     * sealed class(ContactViewType)의 데이터 클래스에 따라 레이아웃을 어댑터에서 설정함
     */
    private fun setList() {
        //list는 뷰에 넣어줄 데이터값, viewLifecycleOwner는  Fragment의 View의 생명주기에 맞춰 데이터를 관찰함
        viewModel.list.observe(viewLifecycleOwner) { list ->
            onRecyclerView(list, NowColor.color)
            nowList = list
        }
        viewModel.color.observe(viewLifecycleOwner) { color ->
            nowList?.let { onRecyclerView(it, color) }
        }

        //현재 보고 있는 레이아웃 타입 설정, 버튼도 그에 맞춰 변경
        viewModel.layoutType.observe(viewLifecycleOwner) { type ->
            when (type) {
                LayoutType.GRID -> {
                    binding.rcContactList.layoutManager = GridLayoutManager(requireContext(), 4)
                }

                else -> {
                    binding.rcContactList.layoutManager = LinearLayoutManager(requireContext())
                }
            }
            viewModel.setContactList(type) //실드 클래스에 넣어 타입 구분
        }
        val itemTouchHelper = ItemTouchHelper(ContactItemHelper(requireContext()))
        itemTouchHelper.attachToRecyclerView(binding.rcContactList)
    }

    private fun onRecyclerView(list: List<ContactViewType>, color: ColorTheme) {
        if (adapter == null) {
            adapter = ContactAdapter(list, color).apply {
                itemClick = object : ContactAdapter.ItemClick {
                    override fun onClick(view: View, position: Int) { //즐겨찾기 버튼
                        viewModel.onFavorite(position)
                    }
                }
            }
            binding.rcContactList.adapter = adapter
        } else {
            adapter?.updateColor(color)
            adapter?.load(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * TODO Fragment RecyclerView 검색
     */
    fun updateItem(text: String) {
        adapter?.performSearch(text)
    }

}