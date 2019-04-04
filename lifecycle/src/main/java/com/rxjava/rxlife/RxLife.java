package com.rxjava.rxlife;

import android.arch.lifecycle.Lifecycle.Event;
import android.arch.lifecycle.LifecycleOwner;

import io.reactivex.*;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * User: ljx
 * Date: 2019/4/3
 * Time: 14:41
 */
public final class RxLife {

    public static <T> ObservableTransformer<T, T> observable(LifecycleOwner owner) {
        return observable(owner, Event.ON_DESTROY);
    }

    public static <T> ObservableTransformer<T, T> observable(LifecycleOwner owner, Event event) {
        return upstream -> upstream.onTerminateDetach()
                .lift(observer -> {
                    return new LifeObserver<>(owner, observer, event);
                });
    }

    public static <T> ObservableTransformer<T, T> observableOnMain(LifecycleOwner owner) {
        return observableOnMain(owner, Event.ON_DESTROY);
    }

    public static <T> ObservableTransformer<T, T> observableOnMain(LifecycleOwner owner, Event event) {
        return upstream -> upstream
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()
                .lift(observer -> {
                    return new LifeObserver<>(owner, observer, event);
                });
    }

    public static <T> FlowableTransformer<T, T> flowable(LifecycleOwner owner) {
        return flowable(owner, Event.ON_DESTROY);
    }

    public static <T> FlowableTransformer<T, T> flowable(LifecycleOwner owner, Event event) {
        return upstream -> upstream.onTerminateDetach()
                .lift(subscriber -> {
                    return new LifeSubscriber<>(owner, subscriber, event);
                });
    }

    public static <T> FlowableTransformer<T, T> flowableOnMain(LifecycleOwner owner) {
        return flowableOnMain(owner, Event.ON_DESTROY);
    }

    public static <T> FlowableTransformer<T, T> flowableOnMain(LifecycleOwner owner, Event event) {
        return observable -> observable
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()
                .lift(subscriber -> {
                    return new LifeSubscriber<>(owner, subscriber, event);
                });
    }

    public static <T> SingleTransformer<T, T> single(LifecycleOwner owner) {
        return single(owner, Event.ON_DESTROY);
    }

    public static <T> SingleTransformer<T, T> single(LifecycleOwner owner, Event event) {
        return upstream -> upstream.onTerminateDetach()
                .lift(observer -> {
                    return new LifeSingleObserver<>(owner, observer, event);
                });
    }

    public static <T> SingleTransformer<T, T> singleOnMain(LifecycleOwner owner) {
        return singleOnMain(owner, Event.ON_DESTROY);
    }

    public static <T> SingleTransformer<T, T> singleOnMain(LifecycleOwner owner, Event event) {
        return observable -> observable
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()
                .lift(observer -> {
                    return new LifeSingleObserver<>(owner, observer, event);
                });
    }

    public static <T> MaybeTransformer<T, T> maybe(LifecycleOwner owner) {
        return maybe(owner, Event.ON_DESTROY);
    }

    public static <T> MaybeTransformer<T, T> maybe(LifecycleOwner owner, Event event) {
        return upstream -> upstream.onTerminateDetach()
                .lift(observer -> {
                    return new LifeMaybeObserver<T>(owner, observer, event);
                });
    }

    public static <T> MaybeTransformer<T, T> maybeOnMain(LifecycleOwner owner) {
        return maybeOnMain(owner, Event.ON_DESTROY);
    }

    public static <T> MaybeTransformer<T, T> maybeOnMain(LifecycleOwner owner, Event event) {
        return upstream -> upstream
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()
                .lift(observer -> {
                    return new LifeMaybeObserver<T>(owner, observer, event);
                });
    }


    public static CompletableTransformer completable(LifecycleOwner owner) {
        return completable(owner, Event.ON_DESTROY);
    }

    public static CompletableTransformer completable(LifecycleOwner owner, Event event) {
        return upstream -> upstream.onTerminateDetach()
                .lift(subscriber -> {
                    return new LifeCompletableObserver(owner, subscriber, event);
                });
    }

    public static CompletableTransformer completableOnMain(LifecycleOwner owner) {
        return completableOnMain(owner, Event.ON_DESTROY);
    }

    public static CompletableTransformer completableOnMain(LifecycleOwner owner, Event event) {
        return upstream -> upstream
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()
                .lift(subscriber -> {
                    return new LifeCompletableObserver(owner, subscriber, event);
                });
    }
}
