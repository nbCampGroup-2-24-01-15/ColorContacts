package com.example.colorcontacts.view.favorite.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.colorcontacts.data.TagMember
import com.example.colorcontacts.data.UserList
import com.example.colorcontacts.utill.LayoutType
import com.example.colorcontacts.view.favorite.adapter.FavoriteViewType

class FavoriteViewModel : ViewModel() {

    private val _favoriteList: MutableLiveData<List<FavoriteViewType>> = MutableLiveData()
    val favoriteList: LiveData<List<FavoriteViewType>> get() = _favoriteList

    //즐겨찾기 목록에 있는 유저를 레이아웃타입을 구별하고 실드클래스에 넣어줌
    fun setFavoriteList(type: LayoutType) {
        _favoriteList.value = TagMember.totalTags.flatMap { keyTag ->
            UserList.userList.filter { keyTag.member.contains(it.key) }.map { user ->
                when (type) {
                    LayoutType.LINEAR -> FavoriteViewType.FavoriteUser(user)
                    else -> FavoriteViewType.FavoriteGrid(user)
                }
            }
        }
    }

}

