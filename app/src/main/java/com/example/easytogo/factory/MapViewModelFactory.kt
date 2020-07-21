package com.example.easytogo.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.easytogo.viewmodel.MapViewModel

class MapViewModelFactory(private var context: Context) :ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(context) as T
    }

}