package com.example.rxlife;

import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle.Event;
import android.arch.lifecycle.LifecycleOwner;
import android.util.Log;

import com.rxjava.rxlife.RxLife;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * User: ljx
 * Date: 2019-05-26
 * Time: 15:20
 */
public class Presenter extends BaseScope implements GenericLifecycleObserver {

    public Presenter() {
        Observable.interval(1, 1, TimeUnit.SECONDS)
            .as(RxLife.asOnMain(this))
            .subscribe(aLong -> {
                Log.e("LJX", "accept aLong=" + aLong);
            });
    }

    @Override
    public void onStateChanged(LifecycleOwner source, Event event) {
        //Activity/Fragment 生命周期回调
        if (event == Event.ON_DESTROY) {  //Activity/Fragment 销毁
            source.getLifecycle().removeObserver(this);
            dispose();
        }
    }
}
