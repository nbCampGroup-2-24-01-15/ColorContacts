package com.example.colorcontacts.contactList

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.colorcontacts.DetailPageActivity
import com.example.colorcontacts.LayoutType
import com.example.colorcontacts.R
import com.example.colorcontacts.User
import com.example.colorcontacts.UserList
import com.example.colorcontacts.databinding.FragmentContactListBinding


class ContactListFragment : Fragment() {

    private var adapter: ContactAdapter? = null

    private var _binding: FragmentContactListBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: ContactViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[ContactViewModel::class.java]
        _binding = FragmentContactListBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        //레이아웃 타입 초기화
        viewModel.setLayoutType()
        setList()
        setLayoutBtn()
        setMyPageTab()
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
            if (adapter == null) {
                adapter = ContactAdapter(list).apply {
                    itemClick = object : ContactAdapter.ItemClick {
                        override fun onClick(view: View, position: Int) { //즐겨찾기 버튼
                            viewModel.onFavorite(position)
                        }
                    }
                }
                binding.rcContactList.adapter = adapter
            } else {
                adapter?.load(list)
            }
        }

        //현재 보고 있는 레이아웃 타입 설정, 버튼도 그에 맞춰 변경
        viewModel.layoutType.observe(viewLifecycleOwner) { type ->
            when (type) {
                LayoutType.GRID -> {
                    binding.ivContactLayout.setImageResource(R.drawable.ic_fragment_linear)
                    binding.rcContactList.layoutManager = GridLayoutManager(requireContext(), 4)
                }

                else -> {
                    binding.ivContactLayout.setImageResource(R.drawable.ic_fragment_grid)
                    binding.rcContactList.layoutManager = LinearLayoutManager(requireContext())
                }
            }
            viewModel.setContactList(type) //실드 클래스에 넣어 타입 구분
        }
        val itemTouchHelper = ItemTouchHelper(ContactItemHelper(requireContext()))
        itemTouchHelper.attachToRecyclerView(binding.rcContactList)
    }

    private fun setLayoutBtn() {
        binding.ivContactLayout.setOnClickListener {
            viewModel.getLayoutType()
        }
    }

    private fun setMyPageTab() {

        if (UserList.myData.isEmpty()) {
            val myDefault = User(
                img = Uri.EMPTY,
                backgroundImg = Uri.EMPTY,
                name = getString(R.string.edit_name),
                phone = "",
                email = "",
                event = null,
                info = null,
                favorites = false
            )
            UserList.myData.add(myDefault)

            binding.ivMyImg.setImageResource(R.drawable.img_user_profile)
            binding.tvMyName.text = myDefault.name
        } else {
            binding.ivMyImg.setImageURI(UserList.myData[1].img ?: Uri.EMPTY)
            binding.tvMyName.text = UserList.myData[1].name ?: ""
            Glide.with(this)
                .load(UserList.myData[1].backgroundImg)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        binding.linearLayout4.background = resource
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {
                        Toast.makeText(view?.context, "이미지 로드 실패", Toast.LENGTH_SHORT).show()
                    }
                })

        }

        binding.linearLayout4.setOnClickListener {
            val intent = Intent(activity, DetailPageActivity::class.java).apply {
//                putExtra("myPage", )
            }
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}