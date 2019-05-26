package com.example.rxlife

import android.app.Application
import android.util.Log

import com.squareup.leakcanary.LeakCanary

import io.reactivex.plugins.RxJavaPlugins

/**
 * User: ljx
 * Date: 2019/4/3
 * Time: 15:50
 */
class AppHolder : Application() {

    override fun onCreate() {
        super.onCreate()
        LeakCanary.install(this)
        //设置RxJava 全局异常处理
        RxJavaPlugins.setErrorHandler { throwable ->
            Log.e("LJX", "setErrorHandler=$throwable")
            /**
             * RxJava2的一个重要的设计理念是：不吃掉任何一个异常,即抛出的异常无人处理，便会导致程序崩溃
             * 这就会导致一个问题，当RxJava2“downStream”取消订阅后，“upStream”仍有可能抛出异常，
             * 这时由于已经取消订阅，“downStream”无法处理异常，此时的异常无人处理，便会导致程序崩溃
             */
        }
    }
}
