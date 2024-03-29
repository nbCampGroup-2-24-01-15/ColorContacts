package com.example.colorcontacts.utill

import com.example.colorcontacts.R

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
    fun checkName(str : String): Int? {
        val regex = Regex("^[가-힣a-zA-Z]+$")
        return if (regex.matches(str)) null
        else R.string.add_contact_dialog_name_error
    }
    //2. 폰번호 유효성 검사
    fun checkPhoneNumber(str : String): Int? {
        val regex = Regex("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}\$")
        return if(regex.matches(str)) null
        else R.string.add_contact_dialog_phone_number_error
    }
    //3. 이메일 유효성 검사
    fun checkEmail(str: String): Int? {
        val regex = Regex("\\w+@\\w+\\.\\w+(\\.\\w+)?")
        return if (regex.matches(str)) null
        else R.string.add_contact_dialog_email_error
    }
}