package com.example.colorcontacts.favorite

import com.example.colorcontacts.User

sealed class FavoriteViewType{
    data class FavoriteUser(val user: User) : FavoriteViewType()
}
