package com.antipov.ndk_idp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.core.view.isVisible
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_captcha.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.toast
import java.io.File

class CaptchaActivity : AppCompatActivity() {

    private var captchaString = ""

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
        initListeners()
        generateCaptcha()
    }

    private fun initListeners() {
        buttonProve.setOnClickListener { checkCaptcha() }
        refresh.setOnRefreshListener { generateCaptcha() }
    }

    private fun checkCaptcha() {
        if (yourAnswer.text.toString() == captchaString) {
            toast("Congrats! You are not robot").apply {
                view.backgroundColor = Color.GREEN
            }
        } else {
            toast("YOU ARE ROBOT").apply {
                view.backgroundColor = Color.RED
            }
        }
    }

    private fun generateCaptcha() {
        Single.fromCallable {
            // create a file
            val file = File(filesDir, FILENAME)
            // call native function
            val str = makeCaptcha(true, file.absolutePath)
            // decoding generated bitmap with captcha
            str to BitmapFactory.decodeFile(file.absolutePath)
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onCaptchaGenerated, ::onCaptchaError)
            .addToDisposables()
    }

    private fun onCaptchaError(throwable: Throwable) {
        toast(throwable.toString())
    }

    private fun onCaptchaGenerated(result: Pair<String, Bitmap>) {
        val (answer, bmp) = result
        captcha.setImageBitmap(bmp)
        refresh.isRefreshing = false
        if (progress.isVisible) progress.isVisible = false
        captchaString = answer
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    private external fun makeCaptcha(addBorder: Boolean, path: String): String

    private fun Disposable.addToDisposables() = compositeDisposable.add(this)

}
