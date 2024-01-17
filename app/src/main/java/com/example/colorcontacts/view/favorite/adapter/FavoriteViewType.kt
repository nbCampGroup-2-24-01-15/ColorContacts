package com.example.colorcontacts.view.favorite.adapter

import com.example.colorcontacts.data.User

sealed class FavoriteViewType{
    data class FavoriteUser(val user: User) : FavoriteViewType()
    data class FavoriteGrid(val user: User) : FavoriteViewType()
}
