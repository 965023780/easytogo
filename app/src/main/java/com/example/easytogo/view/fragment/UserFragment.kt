package com.example.easytogo.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.autonavi.base.amap.mapcore.tools.GLFileUtil.getCacheDir
import com.bumptech.glide.Glide
import com.example.easytogo.R
import com.example.easytogo.databinding.MoudelFragmentUserBinding
import com.example.easytogo.factory.UserViewModelFactory
import com.example.easytogo.ui.MaskPopWindow
import com.example.easytogo.utils.SPUtils
import com.example.easytogo.view.activity.LoginActivity
import com.example.easytogo.viewmodel.UserViewModel
import com.yalantis.ucrop.UCrop
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.moudel_fragment_user.*
import kotlinx.android.synthetic.main.moudel_fragment_user.view.*
import org.json.JSONObject
import java.io.File
import java.net.URL

class UserFragment : Fragment() {
    companion object {
        private val MSG_SEND_DB_WHAT = 100
        private val MSG_SET_NICK_WHAT = 200
        private val MSG_SET_AVATAR_WHAT = 300
        private val MSG_SET_BACKGROUND_WHAT = 400
        private val MSG_SET_OFFLINEMAP_WHAT = 500
        private val MSG_SET_PASSWAORD_WHAT = 600
        private val MSG_SET_BACK_WHAT = 700
        private lateinit var userViewModel: UserViewModel
    }


    private lateinit var ivBackground: ImageView
    private lateinit var ivAvatar: ImageView
    private lateinit var binder: MoudelFragmentUserBinding
    private lateinit var handler: MyHandler


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.moudel_fragment_user, container, false)
        init(root)
        handler = MyHandler(context?.applicationContext)
        return root
    }

    private fun initViewModel() {
        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(requireContext().applicationContext)
        ).get(UserViewModel::class.java)
        userViewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            view?.user_nick?.setText(userViewModel.getNick())
            Log.d("livedata", "nick change")
        })
        if (!userViewModel.getName().equals("")) {
            return
        }
        var key = listOf<String>("userName", "password", "nick", "backgroundPath", "avatarPath")
        var data: Map<String, String?>
        context?.let {
            data = SPUtils(it.applicationContext).getData(key)
            if (!data["userName"].toString().equals("")) {
                userViewModel.setAvatarPath(data["avatarPath"].toString())
                userViewModel.setBackgroundPath(data["backgroundPath"].toString())
                userViewModel.setName(data["userName"].toString())
                if (data["nick"].toString().equals("")) {
                    userViewModel.setNick(resources.getString(R.string.userNickInitial))
                } else {
                    userViewModel.setNick(data["nick"].toString())
                }
            }
        }
    }

    private fun init(root: View) {
        initViewModel()
        ivBackground = root.findViewById(R.id.user_background)
        ivAvatar = root.findViewById(R.id.user_avatar)
        if (userViewModel.getBackgroundPath().equals("")) {
            Glide.with(root.context).load(R.drawable.image_user_background).into(ivBackground)
        } else {
            Glide.with(root.context).load(URL(userViewModel.getBackgroundPath())).into(ivBackground)
        }
        if (!userViewModel.getAvatarPath().equals("")) {
            Glide.with(root.context).load(URL(userViewModel.getAvatarPath())).into(ivAvatar)
        }
        binder = DataBindingUtil.bind(root)!!
        binder.userViewModel = userViewModel
        setListener(root)
    }

    private fun setListener(root: View) {
        root.user_set.setOnClickListener {
            var popupWindow = MaskPopWindow(root.context, handler)
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
        }
        root.user_prepare_login.setOnClickListener {
            if (!userViewModel.getName().equals("")) {
                context?.let { it1 ->
                    Toasty.info(it1, R.string.userHasLogined, Toast.LENGTH_LONG).show()
                }
            } else {
                var intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        root.user_offline_map.setOnClickListener {
            context?.let { it1 -> Toasty.info(it1, R.string.noFunction).show() }
        }
    }

    private fun cropPhoto(uri: Uri, requestCode: Int) {
        var destinationUri = Uri.fromFile(File(getCacheDir(context), "myCroppedImage.jpg"))
        var uCrop = UCrop.of(uri, destinationUri)
        var options = UCrop.Options()
        options.setFreeStyleCropEnabled(true)
        if (requestCode == MSG_SET_AVATAR_WHAT) {
            options.setCircleDimmedLayer(true)
        }
        uCrop.withOptions(options)
        context?.let { uCrop.start(it, this, requestCode / 10) }
    }

    private fun getFromAlbum(requestCode: Int) {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, requestCode)
    }

    inner class MyHandler(var context: Context?) : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MSG_SEND_DB_WHAT -> {
                    Log.d("Handler", msg.data.getString("user"))
                    var code = JSONObject(msg.data.getString("user")).opt("code").toString()
                    if (code.equals("-1")) {
                        Toasty.warning(context!!, R.string.setFail).show()
                    } else {
                        Toasty.success(context!!, R.string.setSuccess).show()
                        var jsonObject =
                            JSONObject(
                                JSONObject(msg.data.getString("user")).opt("data").toString()
                            )
                        if (jsonObject.opt("password") != null) {
                            var saveData = mapOf<String, String>(
                                "password" to jsonObject.optString("password")
                            )
                            SPUtils(context!!).saveData(saveData)
                        }
                        if (jsonObject.opt("backgroundPath") != null) {
                            var saveData = mapOf<String, String>(
                                "backgroundPath" to jsonObject.optString("backgroundPath")
                            )
                            SPUtils(context!!).saveData(saveData)
                        }
                        if (jsonObject.opt("avatarPath") != null) {
                            var saveData = mapOf<String, String>(
                                "avatarPath" to jsonObject.optString("avatarPath")
                            )
                            SPUtils(context!!).saveData(saveData)
                        }
                    }
                }
                MSG_SET_NICK_WHAT -> {
                    if (userViewModel.getName().equals("")) {
                        Toasty.warning(context!!, R.string.backFail).show()
                        return
                    }
                    var nick = msg.data.get("nick").toString()
                    var params = mapOf<String, String>(
                        "userName" to userViewModel.getName(),
                        "nick" to nick
                    )
                    userViewModel.sendUpdateNickRequest(params, MyHandler(context))
                    userViewModel.setNick(nick)
                }
                MSG_SET_AVATAR_WHAT -> {
                    if (userViewModel.getName().equals("")) {
                        Toasty.warning(context!!, R.string.backFail).show()
                        return
                    }
                    getFromAlbum(MSG_SET_AVATAR_WHAT)
                }
                MSG_SET_BACKGROUND_WHAT -> {
                    if (userViewModel.getName().equals("")) {
                        Toasty.warning(context!!, R.string.backFail).show()
                        return
                    }
                    getFromAlbum(MSG_SET_BACKGROUND_WHAT)
                }
                MSG_SET_BACK_WHAT -> {
                    if (userViewModel.getName().equals("")) {
                        Toasty.warning(context!!, R.string.backFail).show()
                        return
                    }
                    var saveData = mapOf<String, String>(
                        "userName" to "",
                        "nick" to "",
                        "password" to "",
                        "backgroundPath" to "",
                        "avatarPath" to ""
                    )
                    SPUtils(context!!).saveData(saveData)
                    Glide.with(context).load(R.drawable.image_user_background)
                        .into(user_background)
                    Glide.with(context).load(R.drawable.ic_user_avatar)
                        .into(user_avatar)
                    userViewModel.init()
                    Toasty.success(context!!, R.string.backSuccess, Toast.LENGTH_LONG).show()
                }
                MSG_SET_PASSWAORD_WHAT -> {
                    var password = msg.data.get("password").toString()
                    var params = mapOf<String, String>(
                        "userName" to userViewModel.getName(),
                        "password" to password
                    )
                    userViewModel.sendUpdatePasswordRequest(params, MyHandler(context))
                }
                MSG_SET_OFFLINEMAP_WHAT -> {
                    Toasty.info(context!!, R.string.noFunction, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, intent)
        when (requestCode) {
            MSG_SET_BACKGROUND_WHAT -> if (resultCode == Activity.RESULT_OK) {
                val uri = intent!!.data
                cropPhoto(uri!!, MSG_SET_BACKGROUND_WHAT)
            }
            MSG_SET_AVATAR_WHAT -> if (resultCode == Activity.RESULT_OK) {
                val uri = intent!!.data
                cropPhoto(uri!!, MSG_SET_AVATAR_WHAT)
            }
            MSG_SET_BACKGROUND_WHAT / 10 -> {
                val result = intent?.let { UCrop.getOutput(it) }
                if (result == null) {
                    return
                }
                val bitmap =
                    BitmapFactory.decodeStream(
                        activity?.getContentResolver()?.openInputStream(result!!)
                    )
                if (resultCode == Activity.RESULT_OK) {
                    val resultUri = result
                    Glide.with(context).load(result)
                        .into(ivBackground)
                    try {
                        val address = resultUri.toString()
                        userViewModel.sendBackgroundRequest(bitmap, MyHandler(context))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            MSG_SET_AVATAR_WHAT / 10 -> {
                val result = intent?.let { UCrop.getOutput(it) }
                if (result == null) {
                    return
                }
                val bitmap =
                    BitmapFactory.decodeStream(
                        activity?.getContentResolver()?.openInputStream(result!!)
                    )

                if (resultCode == Activity.RESULT_OK) {
                    val resultUri = result
                    Glide.with(context).load(result)
                        .into(ivAvatar)
                    try {
                        val address = resultUri.toString()
                        userViewModel.sendAvatarRequest(bitmap, MyHandler(context))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}