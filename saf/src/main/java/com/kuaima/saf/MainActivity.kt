package com.kuaima.saf

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        create_file.setOnClickListener {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/jpeg"
                putExtra(Intent.EXTRA_TITLE, "invoice.jpg")
            }
            startActivityForResult(intent, REQUEST_CODE_CREATE_FILE)
        }

        open_file.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_OPEN_DOCUMENT
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/jpeg"
            }
            startActivityForResult(intent, REQUEST_CODE_OPEN_FILE)
        }

        open_file_tree.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_OPEN_DOCUMENT_TREE
            }
            startActivityForResult(intent, REQUEST_CODE_OPEN_FILE_TREE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (data == null) {
            return
        }
        val uri = data.data ?: return
        when (requestCode) {

            REQUEST_CODE_CREATE_FILE -> {
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                contentResolver.takePersistableUriPermission(uri, takeFlags)
                val fromSingleUri = DocumentFile.fromSingleUri(this, uri)
                val outputStream = contentResolver.openOutputStream(uri) ?: return
                val inputStream = this.assets.open("friends.jpg")
                try {
                    outputStream.use { outputStream ->
                        inputStream.use { inputStream ->
                            val byteArray = ByteArray(1024)
                            while (true) {
                                val readCount = inputStream.read(byteArray)
                                if (readCount == -1) {
                                    break
                                }
                                outputStream.write(byteArray, 0, readCount)
                            }
                        }
                    }
                } catch (e: IOException) {
                    Log.e("wfeii", "$e")
                }

            }

            REQUEST_CODE_OPEN_FILE -> {
                val fileOutputStream =
                    FileOutputStream(File(this.getExternalFilesDir(""), "11.jpg"))
                val inputStream = contentResolver.openInputStream(uri)
                val byteArray = ByteArray(1024)
                try {
                    inputStream?.use {
                        fileOutputStream.use {
                            while (true) {
                                val readCount = inputStream.read(byteArray)
                                if (readCount == -1) {
                                    break
                                }
                                fileOutputStream.write(byteArray, 0, readCount)
                            }
                        }
                    }
                } catch (e: IOException) {

                }
            }
            REQUEST_CODE_OPEN_FILE_TREE -> {
                val documentFile = DocumentFile.fromTreeUri(this, uri)
                if (documentFile != null) {
                    Log.e(
                        "wfeii",
                        "documentFile : ${documentFile.name} ${documentFile.isDirectory}"
                    )
                }
            }
        }
    }

    companion object {
        private val REQUEST_CODE_CREATE_FILE = 10
        private val REQUEST_CODE_OPEN_FILE = 20
        private val REQUEST_CODE_OPEN_FILE_TREE = 30
    }
}
