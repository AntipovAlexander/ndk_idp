package com.antipov.ndk_idp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import java.io.File

class MainActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        openCaptcha.setOnClickListener {
            val permissions = RxPermissions(this)
            compositeDisposable += permissions.request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe({ isGranted ->
                    if (isGranted) {
                        startActivity(intentFor<CaptchaActivity>())
                    } else {
                        toast("Permission is needed")
                    }
                }, {
                    toast(it.toString())
                })
        }
    }

    private external fun quadraticEquation(a: Int, b: Int, c: Int): DoubleArray

    companion object {
        init {
            System.loadLibrary("quadratic-equation")
        }
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}

private operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
     this.add(disposable)
}
