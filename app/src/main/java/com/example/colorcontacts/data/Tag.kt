package com.example.colorcontacts.data

import java.io.File

data class Tag(
    val title: String? = "기본", // 태그이름
    val img: File? = null,
    val member: MutableList<String> = mutableListOf() //유저의 key값으로 저장, 유저의 데이터가 변경 될 수 있으니 key로 구분
)

object TagMember {
    var totalTags = mutableListOf<Tag>()
    var defaultTag = Tag()

    init {
        addNewTag(
            Tag(
                "기본", null
            )
        )
    }

    //태그 추가 or 확인하기 key는 User의 Key값
    fun addTag(title: String? = defaultTag.title, img: File? = defaultTag.img, key: String?) {
        val duplicateTag = Tag(title, img) // null 일시 디폴트 값의 태그
        if (totalTags.find { it.title == duplicateTag.title } == null) totalTags.add(
            Tag(
                title,
                img
            )
        ) //duplicate는 디폴트도아닌 기존에 없던 태그가 됨
        else duplicateTag //기존에 있던 태그

        if (key != null) addMember(duplicateTag, key)
    }

    //태그 제거
    fun delTag(title: String) {
        totalTags.remove(totalTags.find { it.title == title })
    }

    //태그에 멤버 추가
    fun addMember(tag: Tag, key: String) {
        totalTags.find { it.title == tag.title }?.member?.add(key)
    }

    //태그 멤버 제거
    fun removeMember(key: String) {
        val tag = getFindTag(key) ?: return
        totalTags.find { it.title == tag.title }!!.member.remove(key)
        printMemberList()
    }

    //유저가 속한 태그 목록 리스트로 리턴
    fun getTag(key: String): List<String?> {
        return totalTags.map { tag -> tag.member.find { it == key } }
    }

    //태그에 속한 유저 목록
    fun getMembers(title: String): MutableList<String>? {
        return totalTags.find { it.title == title }?.member
    }

    // 모든 태그 목록
    fun setAllTag(): List<String?> {
        return totalTags.map { it.title }
    }

    fun memberChk(key: String): Tag? {
        return totalTags.find { it.member.contains(key) }
    }

    //태그 타이틀을 키, 멤버를 쌍으로
    fun setAllMember(): Map<String?, MutableList<String>?> {
        val tagList = setAllTag()
        val tagMember = setAllTag().map { tag -> tag?.let { getMembers(it) } }
        return tagList.zip(tagMember).toMap()
    }

    fun updateMemberTag(key: String, newTag: Tag) {
        val existingTag = getFindTag(key)
        if (existingTag != null) {
            totalTags.find { it.title == existingTag.title }?.apply {
                member.remove(key)
            }
            addMember(newTag, key)
            printMemberList()
        }
    }

    /**
     * 추가
     */

    fun addNewTag(newTag: Tag) {
        if (totalTags.any { it.title == newTag.title && it.img == newTag.img }) {
            return
        }
        totalTags.add(newTag)
    }

    fun getFindTag(key: String): Tag? {
        printMemberList()
        return totalTags.find { tag -> key in tag.member }
    }

    private fun printMemberList() {
        totalTags.forEachIndexed { index, tag ->
            val members = tag.member.joinToString(", ")
            println("Tag $index Members: $members")
        }
    }
}