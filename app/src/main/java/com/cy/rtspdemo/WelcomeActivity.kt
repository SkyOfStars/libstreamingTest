package com.cy.rtspdemo

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.cy.rtspdemo.lisenter.IPermissionListener

class WelcomeActivity : AppCompatActivity() {
    private val TAG = "welcomeActivity"

    private val permissions: List<String> by lazy {
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
        ) + when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> listOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADVERTISE,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.NEARBY_WIFI_DEVICES
            )

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> listOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADVERTISE,
                Manifest.permission.BLUETOOTH_CONNECT
            )

            else -> listOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                /*Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE*/
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_welcome)
        findViewById<Button>(R.id.btn_start_rtsp).setOnClickListener {
            //首先两个手机都连接同一个wifi，
            //在一个手机上启动RTSP推流到服务器
            intent.setClass(this@WelcomeActivity, MainActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.btn_play).setOnClickListener {
            //首先两个手机都连接同一个wifi，
            // 启动RTSP播放器在另一台Android设备上播放
            intent.setClass(this@WelcomeActivity, RtspPlayerActivity::class.java)
            startActivity(intent)
        }

        PermissionXUtil.applyPermissions(this, permissions, object : IPermissionListener {
            override fun allGranted() {
                Log.d(TAG, "allGranted")
    
            }

            override fun denied(deniedList: List<String?>?) {
                Log.d(TAG, "These permissions are denied: $deniedList")
            }
        })

        PermissionXUtil.applyFileManagerPermission(this)

    }

}