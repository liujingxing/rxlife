package com.example.rxlife;

import com.rxjava.rxlife.RxLife;

import java.util.concurrent.TimeUnit;

import androidx.lifecycle.GenericLifecycleObserver;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleOwner;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * User: ljx
 * Date: 2019-05-26
 * Time: 15:20
 */
public class Presenter implements GenericLifecycleObserver {

    private LifecycleOwner mOwner;

    public Presenter(LifecycleOwner owner) {
        owner.getLifecycle().addObserver(this);
    }

    @Override
    public void onStateChanged(LifecycleOwner source, Event event) {
        //Activity 生命周期回调
        mOwner = source;
        if (event == Event.ON_DESTROY) {  //Activity销毁
            mOwner.getLifecycle().addObserver(this);
        }
    }

    private void test() {
        Observable.intervalRange(1, 100, 0, 200, TimeUnit.MILLISECONDS)
            .as(RxLife.asOnMain(mOwner))
            .subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {

                }
            });

    }
}
