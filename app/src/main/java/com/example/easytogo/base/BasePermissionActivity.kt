package com.example.easytogo.base

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.example.easytogo.R
import java.util.*

open class BasePermissionActivity : AppCompatActivity() {
    companion object {
        private val BACKGROUND_LOCATION_PERMISSION = "android.permission.ACCESS_BACKGROUND_LOCATION"
        private val PERMISSON_REQUESTCODE = 0
        private var isNeedCheck = true
        private val needCheckBackLocation = false
        private var needPermissions = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT > 28 && applicationContext.applicationInfo.targetSdkVersion > 28) {
            needPermissions = arrayOf(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                BACKGROUND_LOCATION_PERMISSION
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= 23 && applicationInfo.targetSdkVersion >= 23) {
            if (isNeedCheck) {
                checkPermissions(*needPermissions)
            }
        }
    }

    private fun checkPermissions(vararg permissions: String) {
        try {
            if (Build.VERSION.SDK_INT >= 23 && applicationInfo.targetSdkVersion >= 23) {
                Log.d("permissions", permissions.toString())
                val needRequestPermissonList = findDeniedPermissions(permissions)
                if (null != needRequestPermissonList && needRequestPermissonList.size > 0) {
                    val array = needRequestPermissonList.toTypedArray()
                    val method = javaClass.getMethod(
                        "requestPermissions",
                        *arrayOf<Class<*>?>(Array<String>::class.java, Int::class.javaPrimitiveType)
                    )
                    method.invoke(
                        this, array,
                        PERMISSON_REQUESTCODE
                    )
                }
            }
        } catch (e: Throwable) {
        }
    }

    private fun findDeniedPermissions(permissions: Array<out String>): List<String> {
        val needRequestPermissonList: MutableList<String> = ArrayList()
        if (Build.VERSION.SDK_INT >= 23 && applicationInfo.targetSdkVersion >= 23) {
            try {
                for (perm in permissions) {
                    val checkSelfMethod =
                        javaClass.getMethod("checkSelfPermission", String::class.java)
                    val shouldShowRequestPermissionRationaleMethod = javaClass.getMethod(
                        "shouldShowRequestPermissionRationale",
                        String::class.java
                    )
                    if (checkSelfMethod.invoke(
                            this,
                            perm
                        ) as Int != PackageManager.PERMISSION_GRANTED ||
                        shouldShowRequestPermissionRationaleMethod.invoke(
                            this,
                            perm
                        ) as Boolean
                    ) {
                        if (!needCheckBackLocation && BACKGROUND_LOCATION_PERMISSION == perm) {
                            continue
                        }
                        needRequestPermissonList.add(perm)
                        println(permissions)
                    }
                }
            } catch (e: Throwable) {
            }
        }
        return needRequestPermissonList
    }

    private fun verifyPermissions(grantResults: IntArray): Boolean {
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        paramArrayOfInt: IntArray
    ) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog()
                isNeedCheck = false
            }
        }
    }

    private fun showMissingPermissionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.notifyTitle)
        builder.setMessage(R.string.notifyMsg)
        builder.setNegativeButton(
            R.string.cancel,
            DialogInterface.OnClickListener { dialog, which -> finish() })
        builder.setPositiveButton(
            R.string.setting,
            DialogInterface.OnClickListener { dialog, which -> startAppSettings() })
        builder.setCancelable(false)
        builder.show()
    }

    private fun startAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}