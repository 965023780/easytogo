package com.example.easytogo.utils

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.telecom.Call
import android.view.View
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.*
import com.example.easytogo.R
import com.example.easytogo.base.CallBack
import com.example.easytogo.viewmodel.MapViewModel
import kotlinx.android.synthetic.main.moudel_fragment_home.view.*


class MapManager(private var context: Context, private var mapViewModel: MapViewModel) :
    GeocodeSearch.OnGeocodeSearchListener {
    private var firstLocate = true
    private var markerLoading = false
    private lateinit var mapView: MapView
    private lateinit var map: AMap
    private lateinit var geocoderSearch: GeocodeSearch
    private var myLocation = MyLocationStyle()
    private var mapMarker = MapMarker()
    private val RADIUS = 0f
    private var bundleMsgCount = 0;
    private lateinit var callBack: CallBack
    private lateinit var view: View

    public fun initMap(
        savedInstanceState: Bundle?,
        view: View,
        callBack: CallBack,
        markerLoading: Boolean
    ) {
        mapView = view.findViewById(R.id.mv_map)
        mapView.onCreate(savedInstanceState)
        map = mapView.map
        this.markerLoading = markerLoading
        initUiPosition()
        setMyLocation()
        initMapListener()
        initSearch()
        this.callBack = callBack
        this.view = view
    }

    private fun initUiPosition() {
        val uiSettings = map.uiSettings
        uiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER)
        uiSettings.setLogoBottomMargin(AMapOptions.LOGO_MARGIN_LEFT)
        uiSettings.isMyLocationButtonEnabled = true
    }

    private fun initSearch() {
        geocoderSearch = GeocodeSearch(context)
        geocoderSearch.setOnGeocodeSearchListener(this)
    }

    public fun drawSearchPoint(address: String) {
        getLatLon(address)
    }

    fun getLatLon(name: String?) {
        var data = name?.let { SPUtils(context).getData(listOf(name)) }
        data?.let {
            val query = GeocodeQuery(name, data[name])
            geocoderSearch.getFromLocationNameAsyn(query)
            return
        }
    }

    fun getAddress(latLonPoint: LatLonPoint?) {
        val query = RegeocodeQuery(
            latLonPoint, 200F,
            GeocodeSearch.AMAP
        )
        geocoderSearch.getFromLocationAsyn(query)
    }

    override fun onGeocodeSearched(result: GeocodeResult?, rCode: Int) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.geocodeAddressList != null && result.geocodeAddressList.size > 0
            ) {
                val address = result.geocodeAddressList[0]
                if (address != null) {
                    mapMarker.drawPosition(
                        address.latLonPoint.longitude,
                        address.latLonPoint.latitude,
                        map
                    )
                    mapViewModel.setMarkerAddress(address.formatAddress.toString())
                    mapViewModel.setMarkerContent(address.formatAddress.toString())
                    mapViewModel.setMarkerDistance("adCode:" + address.adcode)
                    changeUI()
                    map.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition(
                                LatLng(
                                    address.latLonPoint.latitude,
                                    address.latLonPoint.longitude
                                ),
                                15F,
                                0F,
                                0F
                            )
                        )
                    )
                }
            }
        }
    }

    override fun onRegeocodeSearched(result: RegeocodeResult?, rCode: Int) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.regeocodeAddress != null && result.regeocodeAddress.formatAddress != null
            ) {
                if (firstLocate) {
                    firstLocate = false
                    SPUtils(context).saveData(mapOf("CurCity" to result.regeocodeAddress.city))
                    return
                }
                mapViewModel.setMarkerAddress(result.regeocodeAddress.formatAddress.toString())
                mapViewModel.setMarkerDistance(
                    "adCode:" + result.regeocodeAddress.adCode.toString()
                )
                mapViewModel.setMarkerContent(result.regeocodeAddress.formatAddress.toString())
            }
        }
    }

    private fun setMyLocation() {
        map.isMyLocationEnabled = true
        myLocation.interval(3000) //3s定位一次
        myLocation.showMyLocation(true)
        myLocation.radiusFillColor(Color.TRANSPARENT)
        myLocation.strokeWidth(RADIUS)
        myLocation.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)
        map.myLocationStyle = myLocation
        map.moveCamera(CameraUpdateFactory.zoomTo(18.0f))
    }

    private fun initMapListener() {
        map.setOnMyLocationChangeListener {
            mapViewModel.setCurLongitude(it.longitude)
            mapViewModel.setCurLatitude(it.latitude)
            if (firstLocate && !markerLoading) {
                map.moveCamera(
                    CameraUpdateFactory.changeLatLng(
                        LatLng(
                            it.latitude,
                            it.longitude
                        )
                    )
                )
                getAddress(LatLonPoint(it.latitude, it.longitude))
            }
        }
        map.addOnMapLongClickListener {
            mapMarker.drawPosition(
                it.longitude,
                it.latitude,
                map
            )
            mapViewModel.setMarkerLatitude(it.latitude)
            mapViewModel.setMarkerLongitude(it.latitude)
            changeUI()
            getAddress(LatLonPoint(it.latitude, it.longitude))
        }
        map.addOnMapClickListener() {
            mapMarker.destroyPosition()
            mapViewModel.setMarkerAddress("")
            view.ll_location.visibility = View.INVISIBLE
            var bundle = Bundle()
            if (bundleMsgCount % 2 == 1) {
                bundle.putString("msg", "Hide")
            } else {
                bundle.putString("msg", "Show")
            }
            callBack.call(bundle)
            bundleMsgCount++;
        }
    }

    private fun changeUI() {
        view.ll_location.visibility = View.VISIBLE
        var bundle = Bundle()
        bundle.putString("msg", "Hide")
        callBack.call(bundle)
        bundleMsgCount = 2
    }

    public fun getMapViewModel(): MapViewModel {
        return mapViewModel
    }

    public fun onDestroy() {
        mapView.onDestroy()
    }

    public fun onResume() {
        mapView.onResume()
    }

    public fun onSaveInstanceStete(outState: Bundle?) {
        mapView.onSaveInstanceState(outState)
    }

    public fun onPause() {
        mapView.onPause()
    }
}