package com.antipov.ndk_idp

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    companion object {
        init {
            System.loadLibrary("quadratic-equation")
        }
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListeners()
    }

    private fun initListeners() {
        actionBtn.setOnClickListener { solveEquation() }
        openCaptcha.setOnClickListener { checkPermission() }
    }

    private fun checkPermission() =
        RxPermissions(this)
            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe(::onPermissionGranted, ::onPermissionFailure)
            .addToDisposables()

    private fun onPermissionGranted(isGranted: Boolean) {
        if (isGranted) {
            startActivity(intentFor<CaptchaActivity>())
        } else {
            toast("Permission is needed")
        }
    }

    private fun onPermissionFailure(throwable: Throwable) {
        toast(throwable.toString())
    }

    @SuppressLint("SetTextI18n")
    private fun solveEquation() {
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

    private external fun quadraticEquation(a: Int, b: Int, c: Int): DoubleArray

    private fun Disposable.addToDisposables() = compositeDisposable.add(this)

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
