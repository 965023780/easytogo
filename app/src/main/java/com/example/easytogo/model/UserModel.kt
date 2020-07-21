package com.example.easytogo.model

import com.example.easytogo.utils.HttpUtils

class UserModel {
    private var name: String = ""
    private var backgroundPath: String = ""
    private var avatarPath: String = ""
    private var httpUtils = HttpUtils()


    public fun init() {
        name = ""
        backgroundPath = ""
        avatarPath = ""
    }

    public fun sendBackgroundRequire(params: Map<String, String>, handler: android.os.Handler) {
        httpUtils.setAddress("/background")
        sendPostRequire(params, handler)
    }

    public fun sendAvatarRequire(params: Map<String, String>, handler: android.os.Handler) {
        httpUtils.setAddress("/avatar")
        sendPostRequire(params, handler)
    }

    public fun sendUpdatePasswordRequire(params: Map<String, String>, handler: android.os.Handler) {
        httpUtils.setAddress("/updatePassword")
        sendPostRequire(params, handler)
    }

    public fun sendUpdateNickRequire(params: Map<String, String>, handler: android.os.Handler) {
        httpUtils.setAddress("/updateNick")
        sendPostRequire(params, handler)
    }

    public fun sendLoginRequire(params: Map<String, String>, handler: android.os.Handler) {
        httpUtils.setAddress("/login")
        sendPostRequire(params, handler)
    }

    public fun sendRegisterRequire(params: Map<String, String>, handler: android.os.Handler) {
        httpUtils.setAddress("/register")
        sendPostRequire(params, handler)
    }

    private fun sendPostRequire(params: Map<String, String>, handler: android.os.Handler) {
        httpUtils.setHandler(handler)
        httpUtils.setBody(params)
        httpUtils.setRequest()
        httpUtils.post()
    }


    public fun getName(): String {
        return name
    }

    public fun getBackgroundPath(): String {
        return backgroundPath
    }

    public fun getAvatarPath(): String {
        return avatarPath
    }

    public fun setBackgroundPath(backgroundPath: String) {
        this.backgroundPath = backgroundPath
    }

    public fun setAvatarPath(avatarPath: String) {
        this.avatarPath = avatarPath
    }

    public fun setName(username: String) {
        this.name = username
    }
}