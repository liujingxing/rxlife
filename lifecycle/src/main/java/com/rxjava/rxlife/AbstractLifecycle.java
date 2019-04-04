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
public abstract class AbstractLifecycle<T> extends AtomicReference<T> implements GenericLifecycleObserver, Disposable, Runnable {

    private final    Lifecycle mLifecycle;
    private volatile Event     mEvent;

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

    @MainThread
    @Override
    public void run() {
        removeObserver();
    }

    @MainThread
    protected final void addObserver() {
        if (isMainThread()) {
            mLifecycle.addObserver(this);
        } else
            throw new RuntimeException("only be invoked in the UI thread");
    }

    protected final void removeObserver() {
        if (isMainThread()) {
            mLifecycle.removeObserver(this);
        } else {
            AndroidSchedulers.mainThread().scheduleDirect(this);
        }
    }

    private boolean isMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }
}
