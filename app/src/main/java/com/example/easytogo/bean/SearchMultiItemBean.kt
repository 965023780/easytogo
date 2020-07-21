package com.example.easytogo.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

data class SearchMultiItemBean<out T>( val type: Int , val t : T) :MultiItemEntity {
    override fun getItemType(): Int {
        return type
    }
}
