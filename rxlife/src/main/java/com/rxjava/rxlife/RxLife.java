package com.rxjava.rxlife;

import android.arch.lifecycle.Lifecycle.Event;
import android.arch.lifecycle.LifecycleOwner;

import org.reactivestreams.Publisher;

import io.reactivex.*;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * User: ljx
 * Date: 2019/4/3
 * Time: 14:41
 */
public final class RxLife {

    public static <T> RxLifeOperator<T> lift(LifecycleOwner lifecycleOwner) {
        return lift(lifecycleOwner, Event.ON_DESTROY);
    }

    public static <T> RxLifeOperator<T> lift(LifecycleOwner lifecycleOwner, Event event) {
        return new RxLifeOperator<>(lifecycleOwner, event);
    }

    public static <T> RxLifeTransformer<T> compose(LifecycleOwner owner) {
        return compose(owner, Event.ON_DESTROY);
    }

    public static <T> RxLifeTransformer<T> compose(LifecycleOwner owner, Event event) {
        return new RxLifeTransformer<T>() {
            @Override
            public SingleSource<T> apply(Single<T> upstream) {
                return upstream.onTerminateDetach()
                        .lift(lift(owner, event));
            }

            @Override
            public MaybeSource<T> apply(Maybe<T> upstream) {
                return upstream.onTerminateDetach()
                        .lift(lift(owner, event));
            }

            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.onTerminateDetach()
                        .lift(lift(owner, event));
            }

            @Override
            public CompletableSource apply(Completable upstream) {
                return upstream.onTerminateDetach()
                        .lift(lift(owner, event));
            }

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.onTerminateDetach()
                        .lift(lift(owner, event));
            }
        };
    }

    public static <T> RxLifeTransformer<T> composeOnMain(LifecycleOwner owner) {
        return composeOnMain(owner, Event.ON_DESTROY);
    }

    public static <T> RxLifeTransformer<T> composeOnMain(LifecycleOwner owner, Event event) {
        return new RxLifeTransformer<T>() {
            @Override
            public SingleSource<T> apply(Single<T> upstream) {
                return upstream.observeOn(AndroidSchedulers.mainThread())
                        .onTerminateDetach()
                        .lift(lift(owner, event));
            }

            @Override
            public MaybeSource<T> apply(Maybe<T> upstream) {
                return upstream.observeOn(AndroidSchedulers.mainThread())
                        .onTerminateDetach()
                        .lift(lift(owner, event));
            }

            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.observeOn(AndroidSchedulers.mainThread())
                        .onTerminateDetach()
                        .lift(lift(owner, event));
            }

            @Override
            public CompletableSource apply(Completable upstream) {
                return upstream.observeOn(AndroidSchedulers.mainThread())
                        .onTerminateDetach()
                        .lift(lift(owner, event));
            }

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.observeOn(AndroidSchedulers.mainThread())
                        .onTerminateDetach()
                        .lift(lift(owner, event));
            }
        };
    }
}
