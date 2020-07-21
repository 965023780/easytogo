package com.example.easytogo.utils

import com.amap.api.maps.AMap
import com.amap.api.maps.model.*
import com.example.easytogo.R

open class MapMarker {
    private lateinit var marker: Marker;

    public fun drawPosition(longitude: Double, latitude: Double, map: AMap) {
        if(this::marker.isInitialized){
            marker.destroy();
        }
        val latLng = LatLng(latitude, longitude)
        marker =
            map.addMarker(
                MarkerOptions().position(latLng).visible(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_position_marker))
            )
    }

    public fun destroyPosition(){
        if(this::marker.isInitialized){
            marker.destroy()
        }
    }

}