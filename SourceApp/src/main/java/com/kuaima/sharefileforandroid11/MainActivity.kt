package com.kuaima.sharefileforandroid11

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fileProviderBtn.setOnClickListener {
            val fileProviderPath = fileProviderShare()
            if (fileProviderPath == null) {
                return@setOnClickListener
            }

//            dealFileProviderPath(fileProviderPath)
            val intent = Intent()
            //授权临时权限
            grantUriPermission(
                "com.kuaima.destinationapp",
                Uri.parse(fileProviderPath), Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            intent
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setAction("android.intent.action.media.test")
                .addCategory(Intent.CATEGORY_DEFAULT)
                .putExtra("file_provider_uri", fileProviderPath)
            startActivity(intent)
        }
        mediaStoreBtn.setOnClickListener {
            val mediaStoreUri = mediaStore()
            if (mediaStoreUri != null) {
                val intent = Intent()
                intent
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setAction("android.intent.action.media.test")
                    .addCategory(Intent.CATEGORY_DEFAULT)
                    .putExtra("media_store", mediaStoreUri)

                startActivity(intent)
            }
        }

        fileBtn.setOnClickListener {
            val fileShare = fileShare()
            val intent = Intent()
            intent
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setAction("android.intent.action.media.test")
                .addCategory(Intent.CATEGORY_DEFAULT)
                .putExtra("file_path", fileShare)
            startActivity(intent)
        }
    }


    //通过MediaStore存储到公共目录再分享
    private fun mediaStore(): String? {
        val resolver = applicationContext.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "friends.jpg")
        }

        val uri = resolver
            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        if (uri != null) {
            val outputStream = resolver.openOutputStream(uri)
            if (outputStream == null) {
                return null
            }
            val inputStream = this.assets.open("friends.jpg")
            val byteArray = ByteArray(1024)
            try {
                inputStream.use { input ->
                    outputStream.use { output ->
                        while (true) {
                            val readLen = input.read(byteArray)
                            if (readLen == -1) {
                                break
                            }
                            outputStream.write(byteArray, 0, readLen)
                        }
                    }
                }
            } catch (e: Throwable) {
                Log.e("wfeii", "mediaStore e:$e")
            }

        }
        Log.e("wfeii", "mediaStore:$uri")
        return uri?.toString()
    }

    //通过FileProvider分享
    private fun fileProviderShare(): String? {
        val imagePath = File(this.getExternalFilesDir(null), "images")!!
        val file = File(imagePath, "friends_file_provider.jpg")

        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }

        //更具获取Url
        val contentUri: Uri =
            FileProvider.getUriForFile(this, "com.kuaima.sharefileforandroid11.fileProvider", file)
        val fileOutputStream = contentResolver.openOutputStream(contentUri)
        if (fileOutputStream == null) {
            return null
        }
        val inputStream = this.assets.open("friends.jpg")
        val byteArray = ByteArray(1024)
        try {
            fileOutputStream.use { outputStream ->
                inputStream.use { inputStream ->
                    while (true) {
                        val readLen = inputStream.read(byteArray)
                        if (readLen == -1) {
                            break
                        }
                        outputStream.write(byteArray, 0, readLen)
                    }
                }
            }
        } catch (e: Throwable) {
            Log.e("wfeii", "fileProviderShare e:$e")
        }
        Log.e("wfeii", "fileProviderShare:$contentUri")
        return contentUri.toString()
    }

    //通过File的方式分享
    private fun fileShare(): String {
        val file = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ),
            "friends_file.jpg"
        )
        if (!file.parentFile.exists()) {
            file.mkdirs()
        }
        val fileOutputStream = FileOutputStream(file)
        val inputStream = this.assets.open("friends.jpg")
        val byteArray = ByteArray(1024)
        try {
            fileOutputStream.use { outputStream ->
                inputStream.use { inputStream ->
                    while (true) {
                        val readLen = inputStream.read(byteArray)
                        if (readLen == -1) {
                            break
                        }
                        outputStream.write(byteArray, 0, readLen)
                    }
                }
            }
        } catch (e: Throwable) {
            Log.e("wfeii", "fileShare:$e")
        }

        return file.path
    }
}