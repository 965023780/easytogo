package com.example.easytogo.view.adapter

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.easytogo.R
import com.example.easytogo.bean.AroundBean


class AroundAdapter(listData: ArrayList<AroundBean>) :
    BaseMultiItemQuickAdapter<AroundBean, BaseViewHolder>(listData) {
    private lateinit var imageResource: Map<String, Int>

    init {
        addItemType(0, R.layout.moudel_recycler_item_around_icons)
        addItemType(1, R.layout.moudel_recycler_item_around_address)
        addItemType(2, R.layout.moudel_recycler_item_around_pois)
        imageResource = mapOf(
            "美食" to R.drawable.ic_around_food,
            "商场" to R.drawable.ic_around_mall,
            "酒店" to R.drawable.ic_around_hotel,
            "景点" to R.drawable.ic_around_scenery,
            "地铁" to R.drawable.ic_around_subway,
            "银行" to R.drawable.ic_around_bank,
            "服务" to R.drawable.ic_around_serve,
            "超市" to R.drawable.ic_around_shop,
            "厕所" to R.drawable.ic_around_closet,
            "更多" to R.drawable.ic_around_more
        )
    }

    override fun convert(helper: BaseViewHolder, item: AroundBean) {
        when (helper.itemViewType) {
            0 -> {
                val size = mContext.resources.displayMetrics.widthPixels / 5
                val parent = helper.getView<LinearLayout>(R.id.parent)
                val params = ViewGroup.LayoutParams(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                parent.gravity = Gravity.CENTER
                parent.layoutParams = params
                helper.setText(R.id.tv_item_around_icons, item.title)
                imageResource[item.title]?.let {
                    helper.setImageResource(
                        R.id.iv_item_around_icons,
                        it
                    )
                }
            }
            1 -> {
                helper.setText(R.id.tv_item_around_address, item.title)
            }
            2 -> {
                val margin = dpToPx(mContext, 6.0f)
                val size = (mContext.resources.displayMetrics.widthPixels - dpToPx(
                    mContext,
                    16.0f
                ) * 2 - margin) / 2


                val cardView1 = helper.getView<CardView>(R.id.cv_map_demo1)
                val cardView2 = helper.getView<CardView>(R.id.cv_map_demo2)
                val cardView3 = helper.getView<CardView>(R.id.cv_map_demo3)

                val paramsCard1 = RelativeLayout.LayoutParams(size, size)
                cardView1.layoutParams = paramsCard1

                val paramsCard2 = RelativeLayout.LayoutParams(size, (size - margin) / 2)
                paramsCard2.addRule(RelativeLayout.RIGHT_OF, R.id.cv_map_demo1)
                paramsCard2.setMargins(margin, 0, 0, 0)
                cardView2.layoutParams = paramsCard2

                val paramsCard3 = RelativeLayout.LayoutParams(size, (size - margin) / 2)
                paramsCard3.addRule(RelativeLayout.RIGHT_OF, R.id.cv_map_demo1)
                paramsCard3.addRule(RelativeLayout.BELOW, R.id.cv_map_demo2)
                paramsCard3.setMargins(margin, margin, 0, 0)
                cardView3.layoutParams = paramsCard3

            }
        }
    }

    fun dpToPx(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}