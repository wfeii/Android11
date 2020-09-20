package com.kuaima.destinationapp

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = intent
        dealMediaStoreUrl(intent)
        dealFilePath(intent)
        dealFileProviderPath(intent)
        Manifest.permission.MANAGE_EXTERNAL_STORAGE
    }

    private fun dealMediaStoreUrl(intent: Intent?) {
        if (intent == null) {
            return
        }
        val mediaStoreUrl = intent.getStringExtra(MEDIA_STORE)

        if (mediaStoreUrl == null) {
            return
        }
        val readOnlyMode = "r"
        val fileDescriptor =
            contentResolver.openFileDescriptor(
                Uri.parse(mediaStoreUrl),
                readOnlyMode
            )?.fileDescriptor
        if (fileDescriptor == null) {
            return
        }
        val fileInputStream: InputStream = FileInputStream(fileDescriptor)

        val file = File(getExternalFilesDir(""), "mediaStore.jpg")
        val fileOutputStream = FileOutputStream(file)
        val byteArray = ByteArray(1024)
        try {
            fileInputStream.use { fileDescriptor ->
                fileOutputStream.use { fileOutputStream ->
                    while (true) {
                        val readLen = fileInputStream.read(byteArray)
                        if (readLen == -1) {
                            break
                        }
                        fileOutputStream.write(byteArray, 0, readLen)
                    }
                }
            }
        } catch (e: Throwable) {
            Log.e("wfeii", "dealMediaStoreUrl:$e")
        }

        Toast.makeText(this, "复制成功", Toast.LENGTH_LONG).show()
    }

    private fun dealFilePath(intent: Intent?) {
        if (intent == null) {
            return
        }
        val filePath = intent.getStringExtra(FILE_PATH)
        if (filePath == null) {
            return
        }
        val file = File(getExternalFilesDir(""), "mediaStore_file.jpg")
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }

        val fileInputStream = FileInputStream(filePath)

        val fileOutputStream = FileOutputStream(file)
        val byteArray = ByteArray(1024)
        try {
            fileInputStream.use { fileInputStream ->
                fileOutputStream.use { fileOutputStream ->
                    while (true) {
                        val readLen = fileInputStream.read(byteArray)
                        if (readLen == -1) {
                            break
                        }
                        fileOutputStream.write(byteArray, 0, readLen)
                    }
                }
            }
        } catch (t: Throwable) {
            Log.e("wfeii", "dealFilePath:$t")
        }
        Toast.makeText(this, "复制成功", Toast.LENGTH_LONG).show()
    }

    private fun dealFileProviderPath(intent: Intent?) {
        if (intent == null) {
            return
        }
        val fileProviderUri = intent.getStringExtra(FILE_PROVIDER_URI)
        if (fileProviderUri == null) {
            return
        }

        Log.e("wfeii", "dealFileProviderPath fileProviderUri:$fileProviderUri")
        val readOnlyMode = "r"
        val fileDescriptor =
            contentResolver.openFileDescriptor(
                Uri.parse(fileProviderUri), readOnlyMode
            )?.fileDescriptor
        if (fileDescriptor == null) {
            return
        }

        val inputStream = FileInputStream(fileDescriptor)
        val file = File(getExternalFilesDir(""), "file_provider_path.jpg")
        val fileOutputStream = FileOutputStream(file)
        val byteArray = ByteArray(1024)
        try {
            inputStream.use { fileDescriptor ->
                fileOutputStream.use { fileOutputStream ->
                    while (true) {
                        val readLen = inputStream.read(byteArray)
                        if (readLen == -1) {
                            break
                        }
                        fileOutputStream.write(byteArray, 0, readLen)
                    }
                }
            }
        } catch (t: Throwable) {
            Log.e("wfeii", "t")
        }
        Toast.makeText(this, "复制成功", Toast.LENGTH_LONG).show()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        dealMediaStoreUrl(intent)
        dealFilePath(intent)
        dealFileProviderPath(intent)
    }

    companion object {
        private const val MEDIA_STORE = "media_store"
        private const val FILE_PATH = "file_path"
        private const val FILE_PROVIDER_URI = "file_provider_uri"
    }
}
