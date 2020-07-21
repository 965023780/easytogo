package com.example.easytogo.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easytogo.R
import com.example.easytogo.bean.AroundBean
import com.example.easytogo.utils.SPUtils
import com.example.easytogo.view.adapter.AroundAdapter
import kotlinx.android.synthetic.main.moudel_fragment_around.view.*

class AroundFragment : Fragment() {
    private lateinit var adapter: AroundAdapter

    private val listData = ArrayList<AroundBean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.moudel_fragment_around, container, false)
        init(root)
        setData()
        return root
    }


    private fun init(root: View) {
        val gridLayoutManager = GridLayoutManager(context, 5)
        root.rc_around.layoutManager = gridLayoutManager
        adapter = AroundAdapter(listData)
        root.rc_around.adapter = adapter
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (listData[position].type) {
                    1, 2 -> gridLayoutManager.spanCount
                    else -> 1
                }
            }
        }
    }

    private fun setData() {
        val strings = arrayOf("美食", "商场", "酒店", "景点", "地铁", "银行", "服务", "超市", "厕所", "更多")
        for (item in 0..9) {
            val bean = AroundBean()
            bean.title = strings[item]
            bean.type = 0
            listData.add(bean)
        }
        val titleBean = AroundBean()
        titleBean.type = 1
        var data = context?.let { SPUtils(it).getData(listOf("CurCity")) }
        titleBean.title = "加载失败"
        data?.let {
            data["CurCity"]?.let {
                if (!it.toString().equals("")) {
                    titleBean.title = it
                }
            }
        }
        listData.add(titleBean)

        val GridBean = AroundBean()
        GridBean.type = 2
        GridBean.title = ""
        listData.add(GridBean)

        adapter.setNewData(listData)
    }
}