package com.example.easytogo.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Poi
import com.amap.api.navi.AmapNaviPage
import com.amap.api.navi.AmapNaviParams
import com.amap.api.navi.AmapNaviType
import com.amap.api.navi.INaviInfoCallback
import com.amap.api.navi.model.AMapNaviLocation
import com.amap.api.services.geocoder.GeocodeSearch
import com.example.easytogo.R
import com.example.easytogo.base.CallBack
import com.example.easytogo.databinding.MoudelFragmentHomeBinding
import com.example.easytogo.factory.MapViewModelFactory
import com.example.easytogo.utils.SPUtils
import com.example.easytogo.view.activity.MainActivity
import com.example.easytogo.view.activity.SearchActivity
import com.example.easytogo.viewmodel.MapViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.moudel_fragment_home.view.*
import kotlinx.android.synthetic.main.moudel_recycle_item_map_search.view.*

class HomeFragment : Fragment(), INaviInfoCallback {
    private lateinit var binder: MoudelFragmentHomeBinding
    private lateinit var callBack: CallBack
    private lateinit var mapViewModel: MapViewModel
    private var geocoderSearch: GeocodeSearch? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        var mapView = inflater.inflate(R.layout.moudel_fragment_home, container, false)
        initDataAndUI(mapView, savedInstanceState)
        setListener(mapView)
        return mapView
    }

    private fun initDataAndUI(mapView: View, savedInstanceState: Bundle?) {
        var markerLoading = false
        var data = context?.let { SPUtils(it).getData(listOf("address")) }
        context?.let { it -> SPUtils(it).saveData(mapOf("address" to "")) }
        data?.let {
            data["address"]?.let { it1 ->
                if (!it1.toString().equals("")) {
                    markerLoading = true
                }
            }
            mapViewModel =
                ViewModelProvider(
                    this,
                    MapViewModelFactory(requireContext().applicationContext)
                ).get(
                    MapViewModel::class.java
                )
            mapViewModel.getLDMarkerContent()
                .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    view?.tv_location_content?.setText(mapViewModel.getMarkerContent())
                })
            mapViewModel.getLDMarkerDistance()
                .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    view?.tv_location_distance?.setText(mapViewModel.getMarkerDistance())
                })
            mapViewModel.initMapManger(mapView, savedInstanceState, callBack, markerLoading)
            mapView.ll_location.visibility = View.INVISIBLE
            binder = DataBindingUtil.bind<MoudelFragmentHomeBinding>(mapView)!!
            binder.viewmodel = mapViewModel
            data?.let {
                data["address"]?.let { it1 ->
                    if (!it1.toString().equals("")) {
                        mapViewModel.drawSearchPoint(it1)
                    }
                }
            }
        }
    }

    private fun setListener(mapView: View) {
        mapView.iv_map_search.setOnClickListener {
            var intent = Intent(activity, SearchActivity::class.java)
            startActivity(intent)
        }
        mapView.iv_map_navigate.setOnClickListener {
            context?.let { it1 ->
                MaterialDialog.Builder(it1).title(R.string.navigateDialog)
                    .positiveText("确定")
                    .negativeText("取消").onNegative { dialog, which ->
                        Toasty.info(it1, R.string.navigateCancel).show()
                    }.onPositive { dialog, which ->
                        var start = Poi(
                            "我的位置",
                            LatLng(
                                mapViewModel.getCurLatitude(),
                                mapViewModel.getCurLongitude()
                            ),
                            ""
                        )
                        if (mapViewModel.getMarkerAddress().equals("")) {
                            var params =
                                AmapNaviParams(start, null, null, AmapNaviType.WALK);
                            params.setUseInnerVoice(true);
                            context?.let {
                                AmapNaviPage.getInstance()
                                    .showRouteActivity(it.applicationContext, params, this)
                            }
                        } else {
                            var end = Poi(
                                mapViewModel.getMarkerAddress(),
                                LatLng(
                                    mapViewModel.getMarkerLatitude(),
                                    mapViewModel.getMarkerLongitude()
                                ),
                                ""
                            )
                            var params =
                                AmapNaviParams(start, null, end, AmapNaviType.WALK);
                            params.setUseInnerVoice(true);
                            context?.let {
                                AmapNaviPage.getInstance()
                                    .showRouteActivity(it.applicationContext, params, this)
                            }
                        }
                        Toasty.success(it1, R.string.navigateBegin).show()
                    }.show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mapViewModel.mapManagerDestroy()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callBack = activity as MainActivity
    }

    override fun onResume() {
        super.onResume()
        mapViewModel.mapManagerResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapViewModel.mapManagerSave(outState)
    }

    override fun onPause() {
        super.onPause()
        mapViewModel.mapManagerPause()
    }


    override fun onGetNavigationText(p0: String?) {
        p0?.let { context?.let { it1 -> Toasty.info(it1, it).show() } }
    }

    override fun onCalculateRouteSuccess(p0: IntArray?) {

    }

    override fun onInitNaviFailure() {

    }

    override fun onStrategyChanged(p0: Int) {

    }

    override fun onScaleAutoChanged(p0: Boolean) {

    }

    override fun onReCalculateRoute(p0: Int) {

    }

    override fun getCustomNaviView(): View? {
        return null
    }

    override fun onDayAndNightModeChanged(p0: Int) {

    }

    override fun onCalculateRouteFailure(p0: Int) {

    }

    override fun getCustomMiddleView(): View? {
        return null
    }

    override fun onMapTypeChanged(p0: Int) {

    }

    override fun onLocationChange(p0: AMapNaviLocation?) {

    }

    override fun getCustomNaviBottomView(): View? {
        return null
    }

    override fun onArrivedWayPoint(p0: Int) {

    }

    override fun onArriveDestination(p0: Boolean) {

    }

    override fun onStartNavi(p0: Int) {

    }

    override fun onStopSpeaking() {

    }

    override fun onExitPage(p0: Int) {
    }

    override fun onNaviDirectionChanged(p0: Int) {

    }

    override fun onBroadcastModeChanged(p0: Int) {

    }

}


