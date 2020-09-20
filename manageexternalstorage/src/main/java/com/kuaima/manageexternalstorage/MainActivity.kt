package com.kuaima.manageexternalstorage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {
    companion object {
        private val REQUEST_SETTINGS_CODE = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //判断是否有所有文件访问权限
        if (!Environment.isExternalStorageManager()) {
            Snackbar.make(
                findViewById(R.id.container), "需要打开权限才能使用该功能，您也可以前往设置->应用。。。开启权限",
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction("确定") {
                    //跳转到所有文件访问权限开启
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.setData(Uri.parse("package:$packageName"));
                    startActivityForResult(intent, REQUEST_SETTINGS_CODE)
                }
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("wfeii", "requestCode:$requestCode resultCode:$resultCode data:${data}")
        if (!Environment.isExternalStorageManager()) {
            Snackbar.make(
                findViewById(R.id.container), "需要打开权限才能使用该功能，您也可以前往设置->应用。。。开启权限",
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction("确定") {
                    //跳转到所有文件访问权限开启
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.setData(Uri.parse("package:$packageName"));
                    startActivityForResult(intent, REQUEST_SETTINGS_CODE)
                }
                .show()
        }

    }
}
