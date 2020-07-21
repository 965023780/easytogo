package com.example.easytogo.base

import android.app.Activity
import android.content.Context

interface BasePresenter {
    fun createModel(activity: Activity?)
    fun sendRequestToModel(context: Context?)
}