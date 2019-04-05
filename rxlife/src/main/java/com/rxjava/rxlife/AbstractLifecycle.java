package com.rxjava.rxlife;

import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Lifecycle.Event;
import android.arch.lifecycle.LifecycleOwner;
import android.os.Looper;
import android.support.annotation.MainThread;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * User: ljx
 * Date: 2019/4/3
 * Time: 11:04
 */
public abstract class AbstractLifecycle<T> extends AtomicReference<T> implements GenericLifecycleObserver, Disposable {

    private final    Lifecycle mLifecycle;
    private volatile Event     mEvent;

    private final Object mObject = new Object();

    public AbstractLifecycle(LifecycleOwner lifecycleOwner, Event event) {
        mEvent = event;
        mLifecycle = lifecycleOwner.getLifecycle();
    }

    @Override
    public final void onStateChanged(LifecycleOwner source, Event event) {
        if (event.equals(mEvent)) {
            dispose();
            removeObserver();
        }
    }

    /**
     * 事件订阅时调用此方法
     */
    protected final void addObserver() throws InterruptedException {
        if (isMainThread()) {
            addObserverOnMain();
        } else {
            final Object object = mObject;
            AndroidSchedulers.mainThread().scheduleDirect(() -> {
                addObserverOnMain();
                synchronized (object) {
                    object.notifyAll();
                }
            });
            synchronized (object) {
                object.wait();
            }
        }
    }

    @MainThread
    private void addObserverOnMain() {
        mLifecycle.addObserver(this);
    }

    /**
     * onError/onComplete 时调用此方法
     */
    protected final void removeObserver() {
        if (isMainThread()) {
            mLifecycle.removeObserver(this);
        } else {
            AndroidSchedulers.mainThread().scheduleDirect(this::removeObserver);
        }
    }

    private boolean isMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }
}
