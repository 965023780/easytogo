package com.example.easytogo.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.easytogo.R
import com.example.easytogo.model.UserModel
import com.example.easytogo.utils.SPUtils
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.moudel_activity_user_register.*
import org.json.JSONObject

class RegisterActivity : Activity() {
    private lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.moudel_activity_user_register)
        var options = RequestOptions().centerCrop().priority(Priority.HIGH).diskCacheStrategy(
            DiskCacheStrategy.AUTOMATIC
        )
        Glide.with(this).load(R.drawable.image_login_register_plane).apply(options)
            .into(btn_user_register_plane)
        init()
    }

    private fun init() {
        btn_user_register_back.setOnClickListener{
            intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        btn_user_check_register.setOnClickListener {
            if (btn_user_register_name.text.toString()
                    .equals("") || et_user_new_password.text.toString().equals("") ||
                et_user_repeated_password.text.toString().equals("")
            ) {
                Toasty.warning(this, R.string.usernameOrPasswordEmpty, Toast.LENGTH_LONG).show()
            } else if (et_user_repeated_password.text.toString()
                    .equals(et_user_new_password.text.toString())
            ) {
                var params = mapOf(
                    "userName" to btn_user_register_name.text.toString(),
                    "password" to et_user_new_password.text.toString()
                )
                UserModel().sendRegisterRequire(params, handler)
            } else {
                Toasty.warning(this, R.string.twicePasswordError, Toast.LENGTH_LONG).show()
            }
        }
        handler = Handler() {
            if (it.what == 100) {
                Log.d("Handler", "Success" + it.data.getString("user"))
                var code = JSONObject(it.data.getString("user")).opt("code").toString()
                if (code.equals("-1")) {
                    Toasty.error(this, "该用户名已存在", Toast.LENGTH_LONG).show()
                } else if (code.equals("500")) {
                    Toasty.error(this, "数据库错误", Toast.LENGTH_LONG).show()
                } else {
                    var jsonObject =
                        JSONObject(JSONObject(it.data.getString("user")).opt("data").toString())
                    Toasty.success(this, "注册成功", Toast.LENGTH_LONG).show()
                    var intent = Intent(this, MainActivity::class.java)
                    var data = mapOf<String, String>(
                        "userName" to btn_user_register_name.text.toString(),
                        "password" to et_user_new_password.text.toString()
                    )
                    SPUtils(applicationContext).saveData(data)
                    startActivity(intent)
                    this.finish()
                }
            }
            false
        }
    }
}