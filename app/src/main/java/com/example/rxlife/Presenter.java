package com.example.rxlife;

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
public class Presenter {

    private LifecycleOwner mOwner;

    public Presenter(LifecycleOwner owner) {
        mOwner = owner;
    }

    public void observable() {
        Observable.intervalRange(1, 100, 0, 200, TimeUnit.MILLISECONDS)
            .as(RxLife.asOnMain(mOwner))
            .subscribe(aLong -> {
                Log.e("LJX", "accept=" + aLong + " Thread=" + Thread.currentThread());
            });
    }
}
