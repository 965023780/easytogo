package com.example.easytogo.utils

import android.content.Context
import android.content.SharedPreferences

class SPUtils(var context: Context) {

    public fun saveData(data: Map<String, String>) {
        val editor = context.getSharedPreferences("data", Context.MODE_PRIVATE).edit()
        for((key,value) in data){
            editor.putString(key,value)
        }
        editor.apply()
    }

    public fun getData(key:List<String>):Map<String,String?>{
        var data= mutableMapOf<String,String?>()
        val prefs=context.getSharedPreferences("data",Context.MODE_PRIVATE)
        for(key in key){
            data.put(key,prefs.getString(key,""))
        }
        return data
    }
}