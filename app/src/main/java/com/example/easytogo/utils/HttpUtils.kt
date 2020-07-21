package com.example.easytogo.utils

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.util.Log
import okhttp3.*
import java.io.*


class HttpUtils {
    private val url = "http://129.211.72.73:8080/map"
    private lateinit var request: Request
    private var okHttpClient = OkHttpClient()
    private lateinit var body: FormBody
    private lateinit var address: String
    private lateinit var mainHandler: Handler



    public fun post() {
        var call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("okHttp", "Fail")
                var string = "{\"code\":500}"
                var bundle = Bundle()
                bundle.putString("user", string)
                var msg = mainHandler.obtainMessage()
                msg.what = 100
                msg.data = bundle
                mainHandler.sendMessage(msg)
            }

            override fun onResponse(call: Call, response: Response) {
                var string = response.body()?.string()
                Thread() {
                    var bundle = Bundle()
                    bundle.putString("user", string)
                    var msg = mainHandler.obtainMessage()
                    msg.what = 100
                    msg.data = bundle
                    mainHandler.sendMessage(msg)
                }.start()
                Log.d("okHttp", string)
                System.out.println(string)
            }
        })
    }


    public fun setBody(params: Map<String, String>) {
        var builder = FormBody.Builder()
        for ((key, value) in params) {
            builder.add(key, value)
            Log.d("okHttpBody",key+" to "+value)
        }
        body = builder.build()
    }

    public fun setRequest() {
        request = Request.Builder()
            .url(address)
            .post(body)
            .build()
    }

    public fun setAddress(path: String) {
        address = this.url + path
    }

    public fun setHandler(handler: Handler) {
        mainHandler = handler
    }

}