package com.cy.rtspdemo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.cy.rtspdemo.lisenter.IPermissionListener
import com.permissionx.guolindev.PermissionX

/**
 * @desc 权限申请
 *
 * @author hudebo
 * @date 2023/10/20
 */
object PermissionXUtil {
     fun applyPermissions(context: FragmentActivity, permissions: List<String>, permissionListener: IPermissionListener?) {
        PermissionX.init(context)
            .permissions( permissions)
            .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, context.getString(R.string.reapply_permission),
                    context.getString(R.string.accept))
            }
            .onForwardToSettings { scope,deniedList ->
                scope.showForwardToSettingsDialog(deniedList, context.getString(R.string.open_permission),
                    context.getString(R.string.accept))
            }
            .request { allGranted: Boolean, grantedList: List<String?>?, deniedList: List<String?>? ->
                if (allGranted) {
                    permissionListener?.allGranted()
                } else {
                    Log.e("PermissionXUtil","These permissions are denied: $deniedList")
                    permissionListener?.denied(deniedList)
                }
            }
    }

    /**
     * 检查其他权限：所有文件管理权限
     *
     *  @param activity activity
     */
    fun applyFileManagerPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) { // 是否有访问所有文件的权限
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.data = Uri.parse("package:${activity.packageName}")
                activity.startActivity(intent)
            }
        }
    }
}