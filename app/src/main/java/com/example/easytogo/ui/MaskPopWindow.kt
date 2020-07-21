package com.example.easytogo.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.text.InputType
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.core.app.ActivityCompat.startActivityForResult
import com.afollestad.materialdialogs.MaterialDialog
import com.example.easytogo.R
import com.example.easytogo.utils.SPUtils
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.moudel_pop_user_setting.view.*


class MaskPopWindow(var handler: Handler) : PopupWindow() {
    private lateinit var context: Context
    private var windowManager: WindowManager? = null
    private var maskView: View? = null

    @SuppressLint("ResourceAsColor")
    constructor(context: Context, handler: Handler) : this(handler) {
        this.context = context
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        contentView = initContentView()
        height = initHeight()
        width = initWidth()
        isOutsideTouchable = true
        isFocusable = true
        isTouchable = true
        setBackgroundDrawable(ColorDrawable())
        animationStyle = R.style.anim_window_pop_from_bottom
        contentView.user_setNick.setOnClickListener {
            MaterialDialog.Builder(context)
                .title(it.resources.getString(R.string.dialog_setNick))
                .inputRangeRes(2, 20, R.color.warningColor)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(
                    it.resources.getString(R.string.dialog_setNick),
                    null,
                    MaterialDialog.InputCallback { dialog, input ->
                        var bundle = Bundle()
                        bundle.putString("nick", input.toString())
                        var msg = handler.obtainMessage()
                        msg.what = 200
                        msg.data = bundle
                        handler.sendMessage(msg)
                        dismiss()
                    })
                .positiveText("确定")
                .show()

        }
        contentView.user_setAvatar.setOnClickListener {
            var msg = handler.obtainMessage()
            msg.what = 300
            handler.sendMessage(msg)
            dismiss()
        }
        contentView.user_setBackground.setOnClickListener {
            var msg = handler.obtainMessage()
            msg.what = 400
            handler.sendMessage(msg)
            dismiss()
        }
        contentView.user_setBack.setOnClickListener {
            var msg = handler.obtainMessage()
            msg.what = 700
            handler.sendMessage(msg)
            dismiss()
        }
        contentView.user_setMap.setOnClickListener {
            var msg = handler.obtainMessage()
            msg.what = 500
            handler.sendMessage(msg)
            dismiss()
        }
        contentView.user_setPassword.setOnClickListener {
            MaterialDialog.Builder(context)
                .title(it.resources.getString(R.string.dialog_checkOldPassword))
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(
                    it.resources.getString(R.string.dialog_checkOldPassword),
                    null,
                    MaterialDialog.InputCallback { dialog, input ->
                        var params = listOf<String>("password")
                        var data = SPUtils(context).getData(params)
                        if (data["password"].toString().equals(input.toString())) {
                            showPasswordMaterialDialog()
                        } else {
                            Toasty.error(context, "请输入正确的密码").show()
                        }
                        dismiss()
                    })
                .positiveText("确定")
                .show()
        }
    }

    private fun showPasswordMaterialDialog() {
        MaterialDialog.Builder(context)
            .title(context.resources.getString(R.string.dialog_setPassword))
            .inputType(InputType.TYPE_CLASS_TEXT)
            .input(
                context.resources.getString(R.string.dialog_setPassword),
                null,
                MaterialDialog.InputCallback { dialog, input ->
                    showTwicePasswordMaterialDialog(input.toString())
                })
            .positiveText("确定")
            .show()
    }

    private fun showTwicePasswordMaterialDialog(first: String) {
        MaterialDialog.Builder(context)
            .title(context.resources.getString(R.string.dialog_setTwicePassword))
            .inputType(InputType.TYPE_CLASS_TEXT)
            .input(
                context.resources.getString(R.string.dialog_setTwicePassword),
                null,
                MaterialDialog.InputCallback { dialog, input ->
                    if (first.equals(input.toString())) {
                        var bundle = Bundle()
                        bundle.putString("password", input.toString())
                        var msg = handler.obtainMessage()
                        msg.what = 600
                        msg.data = bundle
                        handler.sendMessage(msg)
                    } else {
                        Toasty.warning(context, "两次密码输入不相同").show()
                    }
                })
            .positiveText("确定")
            .show()
    }

    private fun initContentView(): View {
        return LayoutInflater.from(context).inflate(R.layout.moudel_pop_user_setting, null, false);
    }

    private fun initHeight(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    protected fun initWidth(): Int {
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

    override fun showAsDropDown(anchor: View) {
        addMask(anchor.getWindowToken())
        super.showAsDropDown(anchor)
    }

    override fun showAtLocation(parent: View?, gravity: Int, x: Int, y: Int) {
        addMask(parent!!.getWindowToken())
        super.showAtLocation(parent, gravity, x, y)
    }

    private fun addMask(token: IBinder) {
        val wl = WindowManager.LayoutParams()
        wl.width = WindowManager.LayoutParams.MATCH_PARENT
        wl.height = WindowManager.LayoutParams.MATCH_PARENT
        wl.format = PixelFormat.TRANSLUCENT
        wl.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
        wl.token = token
        maskView = View(context)
        maskView!!.setBackgroundColor(0x7f000000)
        maskView!!.setFitsSystemWindows(false)
        maskView!!.setOnKeyListener { view: View, i: Int, keyEvent: KeyEvent ->
            if (i == KeyEvent.KEYCODE_BACK) {
                removeMask()
                true
            }
            false
        }
        windowManager!!.addView(maskView, wl)
    }

    private fun removeMask() {
        if (null != maskView) {
            windowManager!!.removeViewImmediate(maskView)
            maskView = null
        }
    }

    override fun dismiss() {
        removeMask()
        super.dismiss()
    }

}