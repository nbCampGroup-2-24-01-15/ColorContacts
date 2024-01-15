package com.example.colorcontacts

import java.util.UUID

data class User(
    var key: String = UUID.randomUUID().toString(),
    var img : Int,
    var name: String,
    var phone: String,
    var email: String,
    var event: String? = null,
    var info: String?,
    var favorites: Boolean
)
