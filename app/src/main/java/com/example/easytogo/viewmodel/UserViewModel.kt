package com.example.easytogo.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.util.Base64
import android.widget.PopupWindow
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easytogo.R
import com.example.easytogo.model.UserModel
import java.io.ByteArrayOutputStream
import java.io.IOException

class UserViewModel(var context: Context) : ViewModel() {
    private val userModel=UserModel()
    private val nick = MutableLiveData<String>().apply {
        value = context.resources.getString(R.string.userNick)
    }

    public fun getLiveData():MutableLiveData<String>{
        return nick
    }

    public fun init(){
        setNick(context.resources.getString(R.string.userNick))
        userModel.init()
    }

    public fun sendUpdateNickRequest(params:Map<String,String>,handler: Handler){
        userModel.sendUpdateNickRequire(params,handler)
    }

    public fun sendUpdatePasswordRequest(params:Map<String,String>,handler: Handler){
        userModel.sendUpdatePasswordRequire(params,handler)
    }

    public fun sendBackgroundRequest(bitmap: Bitmap,handler: Handler){
        var params= mapOf<String,String>("userName" to getName(),"background" to bitmapToBase64(bitmap))
        userModel.sendBackgroundRequire(params,handler)
    }


    public fun sendAvatarRequest(bitmap: Bitmap?,handler: Handler){
        var params= mapOf<String,String>("userName" to getName(),"avatar" to bitmapToBase64(bitmap))
        userModel.sendAvatarRequire(params,handler)
    }

    public fun sendLoginRequest(params:Map<String,String>,handler: Handler){
        userModel.sendLoginRequire(params,handler)
    }

    public fun getName(): String {
        return userModel.getName()
    }

    public fun getBackgroundPath(): String {
        return userModel.getBackgroundPath()
    }

    public fun getAvatarPath(): String {
        return userModel.getAvatarPath()
    }

    @Bindable
    public fun getNick(): String? {
        return nick.value
    }

    public fun setNick(nick: String) {
        this.nick.value = nick
    }

    public fun setBackgroundPath(backgroundPath: String) {
        userModel.setBackgroundPath(backgroundPath)
    }

    public fun setAvatarPath(avatarPath: String) {
        userModel.setAvatarPath(avatarPath)
    }

    public fun setName(username: String) {
        userModel.setName(username)
    }

    public fun bitmapToBase64(bitmap: Bitmap?): String {
        var result: String=""
        var baos: ByteArrayOutputStream? = null
        try {
            if (bitmap != null) {
                baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                baos.flush()
                baos.close()
                val bitmapBytes: ByteArray = baos.toByteArray()
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (baos != null) {
                    baos.flush()
                    baos.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return result
    }
}