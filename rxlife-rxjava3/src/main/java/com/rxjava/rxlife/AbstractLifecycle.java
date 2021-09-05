package com.rxjava.rxlife;

import android.os.Looper;

import java.util.concurrent.atomic.AtomicReference;

import androidx.annotation.MainThread;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * User: ljx
 * Date: 2019/4/3
 * Time: 11:04
 */
public abstract class AbstractLifecycle<T> extends AtomicReference<T> implements Disposable {

    private final Scope scope;

    private final Object mObject = new Object();

    public AbstractLifecycle(Scope scope) {
        this.scope = scope;
    }

    private boolean isAddObserver;

    /**
     * 事件订阅时调用此方法
     */
    protected final void addObserver() throws Exception {
        //Lifecycle添加监听器需要在主线程执行
        if (isMainThread() || !(scope instanceof LifecycleScope)) {
            addObserverOnMain();
        } else {
            final Object object = mObject;
            AndroidSchedulers.mainThread().scheduleDirect(() -> {
                addObserverOnMain();
                synchronized (object) {
                    isAddObserver = true;
                    object.notifyAll();
                }
            });
            synchronized (object) {
                while (!isAddObserver) {
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @MainThread
    private void addObserverOnMain() {
        scope.onScopeStart(this);
    }

    /**
     * onError/onComplete 时调用此方法
     */
    final void removeObserver() {
        //Lifecycle移除监听器需要在主线程执行
        if (isMainThread() || !(scope instanceof LifecycleScope)) {
            scope.onScopeEnd();
        } else {
            AndroidSchedulers.mainThread().scheduleDirect(this::removeObserver);
        }
    }

    private boolean isMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }
}
