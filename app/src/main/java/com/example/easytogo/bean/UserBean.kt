package com.example.easytogo.bean

class UserBean {
    private var userName: String? = null
    private var password: String? = null
    private var nick: String? = null
    private var avatarPath: String? = null
    private var backgroundPath: String? = null

    fun getAvatarPath(): String? {
        return avatarPath
    }

    fun getBackgroundPath(): String? {
        return backgroundPath
    }

    fun getNick(): String? {
        return nick
    }

    fun getPassword(): String? {
        return password
    }

    fun getUserName(): String? {
        return userName
    }

    fun setPassword(password: String?) {
        this.password = password
    }

    fun setUserName(userName: String?) {
        this.userName = userName
    }

    fun setNick(nick: String?) {
        this.nick = nick
    }

    fun setAvatarPath(avatarPath: String?) {
        this.avatarPath = avatarPath
    }

    fun setBackgroundPath(backgroundPath: String?) {
        this.backgroundPath = backgroundPath
    }

}