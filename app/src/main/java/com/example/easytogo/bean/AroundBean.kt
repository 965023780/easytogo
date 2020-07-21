package com.example.easytogo.bean

import android.os.Parcelable
import com.chad.library.adapter.base.entity.MultiItemEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AroundBean(var title: String = "", var type: Int = 0) : Parcelable, MultiItemEntity {
    override fun getItemType(): Int {
        return type
    }
}