package com.example.rxlife


import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import com.rxjava.rxlife.lifeOnMain
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

/**
 * User: ljx
 * Date: 2019-05-23
 * Time: 17:18
 */
class CustomLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {


    fun startInterval() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
            .doOnDispose { Log.e("LJX", "doOnDispose") }
            .lifeOnMain(this)
            .subscribe(
                { aLong -> Log.e("LJX", "accept =" + aLong!! + " thread=" + Thread.currentThread().name) },
                { Log.e("LJX", "onError") },
                { Log.e("LJX", "onComplete") },
                { Log.e("LJX", "onSubscribe") })
    }
}
