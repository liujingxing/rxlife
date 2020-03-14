package com.example.rxlife

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.rxjava.rxlife.lifeOnMain
import io.reactivex.*
import io.reactivex.functions.Function
import kotlinx.android.synthetic.main.rx_life_activity.*
import java.util.concurrent.TimeUnit

class RxLifeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rx_life_activity)
    }

    fun observableByView(view: View) {
        customLayout.startInterval();
    }

    fun observableByVM(view: View) {
        //注:不能使用new 关键字创建MyViewModel对象，否则MyViewModel感知不到生命周期
        val viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
    }

    fun observableByPresenter(view: View) {
        val presenter = Presenter(this)
    }


    fun observable(view: View) {
        Observable.intervalRange(1, 100, 0, 200, TimeUnit.MILLISECONDS)
            .lifeOnMain(this)
            .subscribe {
                Log.e("LJX", "accept=$it Thread=${Thread.currentThread()}")
            }
    }

    fun flowable(view: View) {
        Flowable.intervalRange(1, 100, 0, 200, TimeUnit.MILLISECONDS)
            .lifeOnMain(this)
            .subscribe {
                Log.e("LJX", "accept =$it")
            }
    }

    fun single(view: View) {
        Single.timer(5, TimeUnit.SECONDS)
            .lifeOnMain(this)
            .subscribe { aLong ->
                Log.e("LJX", "accept =" + aLong!!)
            }
    }

    fun maybe(view: View) {
        Maybe.timer(5, TimeUnit.SECONDS)
            .lifeOnMain(this)
            .subscribe {
                Log.e("LJX", "accept =$it")
            }
    }

    fun completable(view: View) {
        Completable.timer(5, TimeUnit.SECONDS)
            .lifeOnMain(this)
            .subscribe { Log.e("LJX", "run") }
    }

    fun leakCanary(view: View) {
        Observable.timer(100, TimeUnit.MILLISECONDS)
            .map(MyFunction())
            .lifeOnMain(this)
            .subscribe {
                Log.e("LJX", "accept =$it")
            }
    }

    internal class MyFunction<T> : Function<T, T> {

        @Throws(Exception::class)
        override fun apply(t: T): T {
            //当dispose时，第一次睡眠会被吵醒,接着便会进入第二次睡眠
            try {
                Thread.sleep(3000)
            } catch (e: Exception) {

            }

            try {
                Thread.sleep(30000)
            } catch (e: Exception) {

            }

            return t
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
