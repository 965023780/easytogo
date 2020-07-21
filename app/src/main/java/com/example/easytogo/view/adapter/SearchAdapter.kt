package com.example.easytogo.view.adapter

import com.amap.api.services.core.PoiItem
import com.amap.api.services.help.Tip
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.easytogo.R
import com.example.easytogo.bean.FormApplication.Companion.context
import com.example.easytogo.bean.SearchMultiItemBean
import com.example.easytogo.utils.SPUtils


class SearchAdapter(listData: ArrayList<SearchMultiItemBean<*>>) :
    BaseMultiItemQuickAdapter<SearchMultiItemBean<*>, BaseViewHolder>(listData) {

    init {
        addItemType(0, R.layout.moudel_recycle_item_map_search)
    }

    override fun convert(helper: BaseViewHolder, item: SearchMultiItemBean<*>) {
        when (item.type) {
            0 -> {
                val tip = item.t as Tip
                helper.setText(R.id.tv_item_search, "${tip.name} ${tip.district}")
            }
        }
    }

}