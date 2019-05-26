package com.rxjava.rxlife;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.observers.CallbackCompletableObserver;
import io.reactivex.internal.observers.EmptyCompletableObserver;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * User: ljx
 * Date: 2019/4/18
 * Time: 18:40
 */
public class CompletableLife extends RxSource<CompletableObserver> {

    private Completable upStream;

    CompletableLife(Completable upStream, Scope scope, boolean onMain) {
        super(scope, onMain);
        this.upStream = upStream;
    }

    @Override
    public final Disposable subscribe() {
        EmptyCompletableObserver observer = new EmptyCompletableObserver();
        subscribe(observer);
        return observer;
    }

    public final Disposable subscribe(final Action onComplete) {
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");

        CallbackCompletableObserver observer = new CallbackCompletableObserver(onComplete);
        subscribe(observer);
        return observer;
    }

    public final Disposable subscribe(final Action onComplete, final Consumer<? super Throwable> onError) {
        ObjectHelper.requireNonNull(onError, "onError is null");
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");

        CallbackCompletableObserver observer = new CallbackCompletableObserver(onError, onComplete);
        subscribe(observer);
        return observer;
    }

    @Override
    public final void subscribe(CompletableObserver observer) {
        ObjectHelper.requireNonNull(observer, "observer is null");
        try {

            observer = RxJavaPlugins.onSubscribe(upStream, observer);

            ObjectHelper.requireNonNull(observer, "The RxJavaPlugins.onSubscribe hook returned a null CompletableObserver. Please check the handler provided to RxJavaPlugins.setOnCompletableSubscribe for invalid null returns. Further reading: https://github.com/ReactiveX/RxJava/wiki/Plugins");

            subscribeActual(observer);
        } catch (NullPointerException ex) { // NOPMD
            throw ex;
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            RxJavaPlugins.onError(ex);
            NullPointerException npe = new NullPointerException("Actually not, but can't pass out an exception otherwise...");
            npe.initCause(ex);
            throw npe;
        }
    }

    private void subscribeActual(CompletableObserver observer) {
        Completable upStream = this.upStream;
        if (onMain) {
            upStream = upStream.observeOn(AndroidSchedulers.mainThread());
        }
        upStream.onTerminateDetach().subscribe(new LifeCompletableObserver(observer, scope));
    }
}
