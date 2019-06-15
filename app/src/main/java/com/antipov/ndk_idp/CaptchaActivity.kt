package com.antipov.ndk_idp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_captcha.*
import java.io.File

class CaptchaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_captcha)

        // create a file
        val file = File(filesDir, "captcha.bmp")

        makeCaptcha(true, file.absolutePath)

        val myBitmap = BitmapFactory.decodeFile(file.absolutePath)

        captcha.setImageBitmap(myBitmap)
    }

    private external fun makeCaptcha(addBorder: Boolean, path: String): Boolean

    companion object {
        init {
            System.loadLibrary("cimg-test")
        }
    }
}
