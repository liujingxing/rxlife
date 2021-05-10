package com.rxjava.rxlife;


import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.exceptions.CompositeException;
import io.reactivex.rxjava3.exceptions.Exceptions;
import io.reactivex.rxjava3.internal.disposables.DisposableHelper;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;

/**
 * 感知Activity、Fragment生命周期的观察者
 * User: ljx
 * Date: 2019/3/30
 * Time: 21:16
 */
final class LifeSingleObserver<T> extends AbstractLifecycle<Disposable> implements SingleObserver<T> {

    private SingleObserver<? super T> downstream;

    LifeSingleObserver(SingleObserver<? super T> downstream, Scope scope) {
        super(scope);
        this.downstream = downstream;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (DisposableHelper.setOnce(this, d)) {
            try {
                addObserver();
                downstream.onSubscribe(d);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                d.dispose();
                onError(ex);
            }
        }
    }

    @Override
    public void onSuccess(T t) {
        if (isDisposed()) return;
        lazySet(DisposableHelper.DISPOSED);
        try {
            removeObserver();
            downstream.onSuccess(t);
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            RxJavaPlugins.onError(e);
        }
    }

    @Override
    public void onError(Throwable t) {
        if (isDisposed()) {
            RxJavaPlugins.onError(t);
            return;
        }
        lazySet(DisposableHelper.DISPOSED);
        try {
            removeObserver();
            downstream.onError(t);
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            RxJavaPlugins.onError(new CompositeException(t, e));
        }
    }

    @Override
    public boolean isDisposed() {
        return DisposableHelper.isDisposed(get());
    }

    @Override
    public void dispose() {
        DisposableHelper.dispose(this);
    }
}
