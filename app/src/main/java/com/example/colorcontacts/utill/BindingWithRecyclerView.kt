package com.example.colorcontacts.utill

import androidx.recyclerview.widget.RecyclerView
import com.example.colorcontacts.databinding.FragmentContactListBinding
import com.example.colorcontacts.databinding.FragmentFavoriteBinding

interface BindingWithRecyclerView {
    val recyclerView: RecyclerView
}

class RecyclerViewBindingWrapper(val binding: Any) : BindingWithRecyclerView {
    //FragmentContactListBinding. FragmentFavoriteListBinding 클래스를 직접 수정하여 인터페이스를 구현할 수는 없음
    //그래서 둘을 감싸줄 래퍼 클래스를 만들어야함
    override val recyclerView: RecyclerView
        get() = when (binding) {
            is FragmentContactListBinding -> binding.rcContactList
            is FragmentFavoriteBinding -> binding.rcFavoriteList
            else -> throw IllegalArgumentException("Unsupported binding type")
        }
}