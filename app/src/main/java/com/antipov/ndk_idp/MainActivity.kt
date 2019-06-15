package com.antipov.ndk_idp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // create a file
        val file = File(filesDir, "captcha.bmp").apply {
            createNewFile()
        }

        makeCaptcha(true, file.absolutePath)

        actionBtn.setOnClickListener {
            try {
                val result = quadraticEquation(
                    aValue.text.toString().toInt(),
                    bValue.text.toString().toInt(),
                    cValue.text.toString().toInt()
                )
                resultLabel.text = "X1 = ${result[0]}, X2 = ${result[1]}"
            } catch (e: Exception) {
                resultLabel.text = e.toString()
            }
        }
    }

    private external fun quadraticEquation(a: Int, b: Int, c: Int): DoubleArray
    private external fun makeCaptcha(addBorder: Boolean, path: String): Boolean

    companion object {
        init {
            System.loadLibrary("quadratic-equation")
            System.loadLibrary("cimg-test")
        }
    }
}
