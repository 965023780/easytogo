package com.example.easytogo.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.easytogo.R
import com.example.easytogo.model.UserModel
import com.example.easytogo.utils.SPUtils
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.util.*


class WelcomeActivity : AppCompatActivity() {
    companion object {
        private var countTime = 5
        private var MSG_COUNT_WHAT = 100
        private var USER_DATA_WHAT = 200
        private lateinit var time: Timer
        private fun openNextActivity(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java)
           activity.startActivity(intent)
           activity.finish()
        }
    }

    private lateinit var handler: MyHandler
    private lateinit var runnable: Runnable
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.moudel_activity_welcome)
        initView()
        initHandler()
        checkUserData()
    }

    private fun checkUserData() {
        Thread() {
            Looper.prepare()
            var flag=false
            var key = listOf<String>("userName", "password", "nick", "backgroundPath", "avatarPath")
            var data = SPUtils(applicationContext).getData(key)
            var handler = Handler() {
                if (it.what == 100) {
                    var jsonObject =
                        JSONObject(JSONObject(it.data.getString("user")).get("data").toString())
                    var saveData = mapOf<String, String>(
                        "userName" to jsonObject.optString("userName"),
                        "nick" to jsonObject.optString("nick"),
                        "password" to jsonObject.optString("password"),
                        "backgroundPath" to jsonObject.optString("backgroundPath"),
                        "avatarPath" to jsonObject.optString("avatarPath")
                    )
                    SPUtils(applicationContext).saveData(saveData)
                    flag=true
                }
                false
            }
            if (!data.get("userName").equals("") && !data.get("password").equals("")) {
                var params = mapOf<String, String>(
                    "userName" to data.get("userName").toString(),
                    "password" to data.get("password").toString()
                )
                UserModel().sendLoginRequire(params,handler)
            }else{
                flag=true
            }
            while(!false){}
            Looper.loop()
        }.start()
    }

    private fun initView() {
        imageView = findViewById(R.id.iv_welcome_background)
        Glide.with(this).load(R.drawable.image_welcome_background).into(imageView)
    }

    private fun initHandler() {
        handler =
            MyHandler(this)
        runnable = Runnable {
            time = Timer()
            var task = object : TimerTask() {
                override fun run() {
                    Log.d("Time", "Welcome:$countTime")
                    countTime--
                    var msg = handler.obtainMessage()
                    msg.what = MSG_COUNT_WHAT
                    msg.arg1 = countTime
                    handler.sendMessage(msg)
                }
            }
            time.schedule(task, 0, 100)
        }
    }

    override fun onResume() {
        handler.post(runnable)
        super.onResume()
    }

    override fun onStop() {
        stopThread()
        countTime=5
        super.onStop()
    }

    private fun stopThread() {
        handler.removeCallbacks(runnable)
    }

    private class MyHandler(private var welcomeActivity: WelcomeActivity) : Handler() {
        private var outer = WeakReference<WelcomeActivity>(welcomeActivity)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            var activity: WelcomeActivity? = outer.get()
            if (activity != null) {
                when (msg.what) {
                    MSG_COUNT_WHAT -> {
                        if (msg.arg1 == 0) {
                            time.cancel()
                            openNextActivity(activity)
                        }
                    }
                    USER_DATA_WHAT -> {

                    }
                }
            }
        }
    }

}