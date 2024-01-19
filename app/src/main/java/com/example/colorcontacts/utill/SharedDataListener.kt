package com.example.colorcontacts.utill

import com.example.colorcontacts.data.Tag
import com.example.colorcontacts.data.TagMember
import com.example.colorcontacts.data.User
import com.example.colorcontacts.data.UserList
import com.example.colorcontacts.ui.contactList.adapter.ContactViewType
import com.example.colorcontacts.ui.favorite.adapter.FavoriteViewType

class SharedDataListener : OnSharedDataListener {

    private var tagMembers: MutableList<Tag> = mutableListOf()
    lateinit var contactList: List<ContactViewType>
    lateinit var favoriteList: List<FavoriteViewType>

    private var createHeader = HeaderList()
    /**
     * TODO TagMember에 가입하기
     *
     * addTag는 디폴트값으로 노란별 "default"태그를 가짐
     * addTag를 해서 키값을 넘겨주면 자동으로 디폴트 URI이미지 획득하고 태그 가입
     */
    override fun onFavorite(key: String) {
        TagMember.addTag(key = key)
    }

    override fun offFavorite(key: String) {
        TagMember.removeMember(key = key)
    }

    override fun setContactList(type: LayoutType): List<ContactViewType> {
        contactList =
            if (type == LayoutType.LINEAR) {
                createHeader(UserList.userList).map {
                    when(it) {
                        is String -> ContactViewType.Header(it)
                        is User -> ContactViewType.ContactUser(it)
                        else -> ContactViewType.Header("error")
                    }
                }
            }
            else UserList.userList.map { ContactViewType.GridUser(it) }

        return contactList
    }

    override fun setFavoriteList(type: LayoutType): List<FavoriteViewType> {
        favoriteList = TagMember.totalTags.flatMap { keyTag ->
            UserList.userList.filter { keyTag.member.contains(it.key) }.map { user ->
                when (type) {
                    LayoutType.LINEAR -> FavoriteViewType.FavoriteUser(user)
                    else -> FavoriteViewType.FavoriteGrid(user)
                }
            }
        }
        return favoriteList
    }
}