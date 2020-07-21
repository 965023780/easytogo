package com.example.easytogo.viewmodel

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easytogo.R
import com.example.easytogo.base.CallBack
import com.example.easytogo.model.MapModel
import com.example.easytogo.utils.MapManager

class MapViewModel(private var context: Context) : ViewModel() {
    private var mapModel = MapModel()
    private lateinit var mapManager: MapManager

    private val markerContent = MutableLiveData<String>().apply {
        value = context.resources.getString(R.string.locationContent)
    }
    private val markerDistance = MutableLiveData<String>().apply {
        value = context.resources.getString(R.string.locationDistance)
    }


    public fun drawSearchPoint(address: String) {
        mapManager.drawSearchPoint(address)
    }

    public fun getMarkerAddress(): String {
        return mapModel.getMarkerAddress()
    }

    public fun setMarkerAddress(newAddress: String) {
        mapModel.setMarkerAddress(newAddress)
    }

    public fun initMapManger(
        mapView: View,
        savedInstanceState: Bundle?,
        callBack: CallBack,
        markerLoading: Boolean
    ) {
        mapManager = MapManager(mapView.context.applicationContext, this)
        mapManager.initMap(savedInstanceState, mapView, callBack, markerLoading)
    }

    public fun mapManagerDestroy() {
        mapManager.onDestroy()
    }

    public fun mapManagerResume() {
        mapManager.onResume()
    }

    public fun mapManagerPause() {
        mapManager.onPause()
    }

    public fun mapManagerSave(savedInstanceState: Bundle?) {
        mapManager.onSaveInstanceStete(savedInstanceState)
    }


    public fun getLDMarkerContent(): MutableLiveData<String> {
        return markerContent
    }

    public fun getLDMarkerDistance(): MutableLiveData<String> {
        return markerDistance
    }

    @Bindable
    public fun getMarkerContent(): String? {
        return markerContent.value
    }

    @Bindable
    public fun getMarkerDistance(): String? {
        return markerDistance.value
    }

    public fun setMarkerDistance(newDistance: String) {
        markerDistance.value = newDistance
    }

    public fun setMarkerContent(newContent: String) {
        markerContent.value = newContent
    }

    public fun getCurLongitude(): Double {
        return mapModel.getCurLongitude()
    }

    public fun getCurLatitude(): Double {
        return mapModel.getCurLatitude()
    }

    public fun setCurLatitude(newLatitude: Double) {
        mapModel.setCurLatitude(newLatitude)
    }

    public fun setCurLongitude(newLongitude: Double) {
        mapModel.setCurLongitude(newLongitude)
    }

    public fun setMarkerLongitude(newLongitude: Double) {
        mapModel.setMarkerLongitude(newLongitude)
    }


    public fun setMarkerLatitude(newLatitude: Double) {
        mapModel.setCurLatitude(newLatitude)
    }

    public fun getMarkerLongitude(): Double {
        return mapModel.getMarkerLongitude()
    }

    public fun getMarkerLatitude(): Double {
        return mapModel.getMarkerLatitude()
    }
}