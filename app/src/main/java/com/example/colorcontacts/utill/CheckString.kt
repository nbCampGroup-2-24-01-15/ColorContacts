package com.example.colorcontacts.utill

/**
 *  TODO : 문자열 유효성 검사 하는 클래스
 *  Regex(정규표현식)을 이용하여 구현
 *
 *  1. 이름 : 영문자 및 한글만 들어가야 한다.
 *  2. 폰번호 : 010-xxxx-xxxx 으로만 구성 되어야 한다.
 *  3. 이메일 : xxx@xxxxx.xxx 형태로 되어야 한다
 */
class CheckString {
    //1. 이름 유효성 검사
    fun checkName(str : String): Boolean{
        val regex = Regex("^[가-힣a-zA-Z]+$")
        return regex.matches(str)
    }
    //2. 폰번호 유효성 검사
    fun checkPhoneNumber(str : String): Boolean{
        val regex = Regex("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}\$")
        return regex.matches(str)
    }
    //3. 이메일 유효성 검사
    fun checkEmail(str: String): Boolean{
        val regex = Regex("\\w+@\\w+\\.\\w+(\\.\\w+)?")
        return regex.matches(str)
    }
}