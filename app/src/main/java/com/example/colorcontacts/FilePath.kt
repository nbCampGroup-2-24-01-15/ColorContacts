package com.example.colorcontacts

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

object FilePath {
    fun Context.absolutelyPath(path: Uri): String? {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor? = contentResolver.query(path, proj, null, null, null)
        val index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()
        if (index == null) return ""

        return c.getString(index)
    }
}