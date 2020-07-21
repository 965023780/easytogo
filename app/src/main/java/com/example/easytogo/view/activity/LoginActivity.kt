package com.example.easytogo.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.easytogo.R
import com.example.easytogo.model.UserModel
import com.example.easytogo.utils.SPUtils
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.moudel_activity_user_login.*
import org.json.JSONObject


class LoginActivity : Activity() {
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.moudel_activity_user_login)
        var options = RequestOptions().centerCrop().priority(Priority.HIGH).diskCacheStrategy(
            DiskCacheStrategy.AUTOMATIC
        )
        Glide.with(this).load(R.drawable.image_login_register_plane).apply(options)
            .into(iv_user_login_plane)
        init()
    }

    private fun init() {
        btn_user_check_login.setOnClickListener {
            if (et_user_name.text.toString().equals("") || et_user_password.text.toString().equals("")) {
                Toasty.warning(this, R.string.usernameOrPasswordEmpty, Toast.LENGTH_LONG).show()
            } else {
                var params = mapOf(
                    "userName" to et_user_name.text.toString(),
                    "password" to et_user_password.text.toString()
                )
                UserModel().sendLoginRequire(params, handler)
            }
        }
        btn_user_login_back.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        btn_user_prepare_register.setOnClickListener {
            intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        handler = Handler() {
            if (it.what == 100) {
                Log.d("Handler", "Success" + it.data.getString("user"))
                var code = JSONObject(it.data.getString("user")).opt("code").toString()
                if (code.equals("-1")) {
                    Toasty.error(this, R.string.usernameOrPasswordError, Toast.LENGTH_LONG).show()
                } else if (code.equals("500")) {
                    Toasty.error(this, R.string.dataBaseError, Toast.LENGTH_LONG).show()
                } else {
                    var jsonObject =
                        JSONObject(JSONObject(it.data.getString("user")).opt("data").toString())
                    Toasty.success(this, R.string.loginSuccess, Toast.LENGTH_LONG).show()
                    var intent = Intent(this, MainActivity::class.java)
                    var saveData = mapOf<String, String>(
                        "userName" to jsonObject.optString("userName"),
                        "nick" to jsonObject.optString("nick"),
                        "password" to jsonObject.optString("password"),
                        "backgroundPath" to jsonObject.optString("backgroundPath"),
                        "avatarPath" to jsonObject.optString("avatarPath")
                    )
                    SPUtils(applicationContext).saveData(saveData)
                    startActivity(intent)
                    this.finish()
                }
            }
            false
        }
    }
}