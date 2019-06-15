package com.antipov.ndk_idp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.core.view.isVisible
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_captcha.*
import org.jetbrains.anko.toast
import java.io.File

class CaptchaActivity : AppCompatActivity() {

    companion object {
        private const val FILENAME = "captcha.bmp"

        init {
            System.loadLibrary("cimg-test")
        }
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_captcha)
        refresh.setOnRefreshListener { generateCaptcha() }
        generateCaptcha()
    }

    private fun generateCaptcha() {
        Single.fromCallable {
            // create a file
            val file = File(filesDir, FILENAME)
            // call native function
            makeCaptcha(true, file.absolutePath)
            // decoding generated bitmap with captcha
            BitmapFactory.decodeFile(file.absolutePath)
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onCaptchaGenerated, ::onCaptchaError)
            .addToDisposables()
    }

    private fun onCaptchaError(throwable: Throwable) {
        toast(throwable.toString())
    }

    private fun onCaptchaGenerated(bmp: Bitmap) {
        captcha.setImageBitmap(bmp)
        refresh.isRefreshing = false
        if (progress.isVisible) progress.isVisible = false
    }

    private external fun makeCaptcha(addBorder: Boolean, path: String): Boolean

    private fun Disposable.addToDisposables() = compositeDisposable.add(this)
}
