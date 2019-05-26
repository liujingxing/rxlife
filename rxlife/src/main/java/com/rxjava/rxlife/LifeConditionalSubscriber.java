package com.rxjava.rxlife;


import org.reactivestreams.Subscription;

import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * 感知Activity、Fragment生命周期的观察者
 * User: ljx
 * Date: 2019/3/30
 * Time: 21:16
 */
final class LifeConditionalSubscriber<T> extends AbstractLifecycle<Subscription> implements ConditionalSubscriber<T> {

    private ConditionalSubscriber<? super T> downstream;

    LifeConditionalSubscriber(ConditionalSubscriber<? super T> downstream, Scope scope) {
        super(scope);
        this.downstream = downstream;
    }

    @Override
    public void onSubscribe(Subscription s) {
        if (SubscriptionHelper.setOnce(this, s)) {
            try {
                addObserver();
                downstream.onSubscribe(s);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                s.cancel();
                onError(ex);
            }
        }
    }

    @Override
    public void onNext(T t) {
        if (isDisposed()) return;
        try {
            downstream.onNext(t);
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            get().cancel();
            onError(e);
        }
    }

    @Override
    public void onError(Throwable t) {
        if (isDisposed()) {
            RxJavaPlugins.onError(t);
            return;
        }
        lazySet(SubscriptionHelper.CANCELLED);
        try {
            removeObserver();
            downstream.onError(t);
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            RxJavaPlugins.onError(new CompositeException(t, e));
        }
    }

    @Override
    public void onComplete() {
        if (isDisposed()) return;
        lazySet(SubscriptionHelper.CANCELLED);
        try {
            removeObserver();
            downstream.onComplete();
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            RxJavaPlugins.onError(e);
        }
    }

    @Override
    public boolean isDisposed() {
        return get() == SubscriptionHelper.CANCELLED;
    }

    @Override
    public void dispose() {
        SubscriptionHelper.cancel(this);
    }

    @Override
    public boolean tryOnNext(T t) {
        if (!isDisposed()) {
            return downstream.tryOnNext(t);
        }
        return false;
    }
}
