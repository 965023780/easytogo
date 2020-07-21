package com.example.easytogo.bean

class BaseBean {
    private var code = 0
    private var msg: String? = null
    private var data: UserBean? = null

    fun getCode(): Int {
        return code
    }

    fun setCode(code: Int) {
        this.code = code
    }

    fun getMsg(): String? {
        return msg
    }

    fun setMsg(msg: String?) {
        this.msg = msg
    }

    fun getData(): UserBean? {
        return data
    }

    fun setData(data: UserBean) {
        this.data = data
    }

}