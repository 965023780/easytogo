package com.example.easytogo.model

class MapModel {
    private var curLongitude: Double = 0.0
    private var curLatitude: Double = 0.0
    private var markerLongitude: Double = 0.0
    private var markerLatitude: Double = 0.0
    private var markerAddress:String=""

    public fun getMarkerAddress():String{
        return markerAddress
    }

    public fun setMarkerAddress(newAddress:String){
        this.markerAddress=newAddress
    }

    public fun getCurLongitude(): Double {
        return curLongitude
    }

    public fun getCurLatitude(): Double {
        return curLatitude
    }

    public fun setCurLatitude(newLatitude: Double) {
        curLatitude = newLatitude
    }

    public fun setCurLongitude(newLongitude: Double) {
        curLongitude = newLongitude
    }

    public fun setMarkerLongitude(newLongitude: Double) {
        markerLongitude = newLongitude
    }


    public fun setMarkerLatitude(newLatitude: Double) {
        markerLatitude = newLatitude
    }

    public fun getMarkerLongitude(): Double {
        return markerLongitude
    }

    public fun getMarkerLatitude(): Double {
        return markerLatitude
    }
}