package com.example.easytogo.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.services.help.Inputtips
import com.amap.api.services.help.InputtipsQuery
import com.amap.api.services.help.Tip
import com.example.easytogo.R
import com.example.easytogo.bean.FormApplication
import com.example.easytogo.bean.SearchMultiItemBean
import com.example.easytogo.utils.SPUtils
import com.example.easytogo.view.adapter.SearchAdapter
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.moudel_activity_map_search.*
import kotlinx.android.synthetic.main.moudel_recycle_item_map_search.view.*
import org.jetbrains.anko.startActivity

class SearchActivity : AppCompatActivity(), Inputtips.InputtipsListener {

    private val items = ArrayList<SearchMultiItemBean<*>>()
    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.moudel_activity_map_search)
        ImmersionBar
            .with(this)
            .statusBarDarkFont(true)
            .statusBarColor(android.R.color.white)
            .navigationBarColor(android.R.color.black)
            .init()
        initSet()
        cl_search_back.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        adapter.setOnItemClickListener { adapter, view, position ->
            var intent = Intent(this, MainActivity::class.java)
            var params = mapOf<String, String>("address" to view.tv_item_search.text.toString())
            SPUtils(view.context).saveData(params)
            startActivity(intent)
            this.finish()
        }
    }

    private var tvSearchStatus = false

    private fun initSet() {

        rc_search.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, RecyclerView.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(this@SearchActivity, RecyclerView.VERTICAL)
                    .apply {
                        setDrawable(
                            ContextCompat.getDrawable(
                                this@SearchActivity,
                                R.drawable.shape_divider
                            )!!
                        )
                    })
        }.adapter = SearchAdapter(items)
            .apply {
                adapter = this
                setOnItemClickListener { _, _, position ->
                    val tip = items[position].t as Tip
                    startActivity<MainActivity>(Pair("tip", tip))
                }
            }

        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (et_search.text.toString().isNotEmpty() && !tvSearchStatus) {
                    TransitionManager.beginDelayedTransition(cl_search)
                    tvSearchStatus = true
                } else if (et_search.text.toString().isEmpty() && tvSearchStatus) {
                    TransitionManager.beginDelayedTransition(cl_search)
                    tvSearchStatus = false
                }
                Inputtips(this@SearchActivity, InputtipsQuery(
                    et_search.text.toString(),
                    FormApplication.cityCode
                ).apply {
                    cityLimit = true
                }).apply {
                    setInputtipsListener(this@SearchActivity)
                }.requestInputtipsAsyn()
            }
        })
    }

    override fun onGetInputtips(tipList: MutableList<Tip>?, p1: Int) {
        items.clear()
        tipList?.let {
            it.forEach { tip ->
                items.add(SearchMultiItemBean(0, tip))
                SPUtils(this.applicationContext).saveData(mapOf(tip.name to tip.adcode))
            }
        }

        adapter.setNewData(items)
    }

}