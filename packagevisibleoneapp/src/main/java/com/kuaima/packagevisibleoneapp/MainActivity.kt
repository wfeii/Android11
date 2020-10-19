package com.kuaima.packagevisibleoneapp

import android.content.Intent
import android.content.pm.PackageManager.MATCH_DEFAULT_ONLY
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        start_activity.setOnClickListener {
            val intent = Intent()
            intent.action = "android.intent.action.test_hello_world"
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val packageManager = packageManager
            val queryIntentActivities =
                packageManager.queryIntentActivities(intent, MATCH_DEFAULT_ONLY)
            for (activity in queryIntentActivities) {
                Log.e("wfeii", "activity : $activity")
            }
            if (isInstalled()) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "请安装应用", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isInstalled(): Boolean {
        val installedPackages = packageManager.getInstalledPackages(0)
        for (installedPackage in installedPackages) {
            if (TextUtils.equals(
                    installedPackage.packageName,
                    "com.kuaima.packagevisiblesecondapp"
                )
            ) {
                return true
            }
        }
        return false
    }
}
