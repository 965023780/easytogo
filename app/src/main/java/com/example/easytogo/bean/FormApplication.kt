package com.example.easytogo.bean

import android.app.Application
import android.content.Context

class FormApplication : Application(){

    companion object{
        lateinit var context : Context
        var cityName = ""
        var cityCode = ""
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}