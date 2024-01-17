package com.example.colorcontacts.utill

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.colorcontacts.data.ColorTheme
import com.example.colorcontacts.data.NowColor
import com.example.colorcontacts.data.Tag
import com.example.colorcontacts.data.TagMember
import com.example.colorcontacts.data.UserList

class SharedViewModel : ViewModel() {

    private val _layoutType: MutableLiveData<LayoutType> = MutableLiveData(LayoutType.LINEAR)
    val layoutType: LiveData<LayoutType> get() = _layoutType

    private val _color: MutableLiveData<ColorTheme> = MutableLiveData()
    val color: LiveData<ColorTheme> get() = _color

    private val _tagMembers: MutableLiveData<MutableList<Tag>> = MutableLiveData()
    val tagMember: LiveData<MutableList<Tag>> get() = _tagMembers

    /**
     * TODO TagMember에 가입하기
     *
     * addTag는 디폴트값으로 노란별 "default"태그를 가짐
     * addTag를 해서 키값을 넘겨주면 자동으로 디폴트 URI이미지 획득하고 태그 가입
     */
    fun onFavorite(key: String) {
        TagMember.addTag(key = key)
        _tagMembers.value = TagMember.totalTags
    }

    fun offFavorite(key: String) {
        TagMember.removeMember(key = key)
        _tagMembers.value = TagMember.totalTags
    }


    //레이아웃 변경 버튼을 눌렀을때 갱신
    fun getLayoutType() {
        UserList.layoutType =
            if (_layoutType.value == LayoutType.LINEAR) LayoutType.GRID else LayoutType.LINEAR
        _layoutType.value = UserList.layoutType
    }

    //프래그먼트창이 켜질때 레이아웃타입 갱신
    fun setLayoutType() {
        _layoutType.value = UserList.layoutType
    }

    //현재 적용중인 컬러 갱신
    fun setColor() {
        _color.value = NowColor.color
    }

}