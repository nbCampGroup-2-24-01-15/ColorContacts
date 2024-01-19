package com.example.colorcontacts.utill

import com.example.colorcontacts.data.User

class HeaderList {
    operator fun invoke(contacts: MutableList<User>): List<Any> {
        val header = mutableListOf<Any>()
        var lastHeader = ""

        contacts.forEach { user ->
            val initial = getKorean(user.name)
            if (initial != lastHeader) {
                header.add(initial)
                lastHeader = initial
            }
            header.add(user)
        }
        return header
    }

    fun getKorean(name: String): String {
        if (name.isEmpty()) return "#"

        val firstChar = name.first()
        return when {
            firstChar in '가'..'깋' -> "ㄱ"
            firstChar in '나'..'닣' -> "ㄴ"
            firstChar in '다'..'딯' -> "ㄷ"
            firstChar in '라'..'맇' -> "ㄹ"
            firstChar in '마'..'밓' -> "ㅁ"
            firstChar in '바'..'빟' -> "ㅂ"
            firstChar in '사'..'싷' -> "ㅅ"
            firstChar in '아'..'잏' -> "ㅇ"
            firstChar in '자'..'짛' -> "ㅈ"
            firstChar in '차'..'칳' -> "ㅊ"
            firstChar in '카'..'킿' -> "ㅋ"
            firstChar in '타'..'팋' -> "ㅌ"
            firstChar in '파'..'핗' -> "ㅍ"
            firstChar in '하'..'힣' -> "ㅎ"
            firstChar.isDigit() -> "0"
            firstChar.isLetter() -> "A-Z"
            else -> "#"
        }
    }
}