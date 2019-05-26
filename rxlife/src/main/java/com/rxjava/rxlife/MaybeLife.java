package com.rxjava.rxlife;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.operators.maybe.MaybeCallbackObserver;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * User: ljx
 * Date: 2019/4/18
 * Time: 18:40
 */
public class MaybeLife<T> extends RxSource<MaybeObserver<? super T>> {

    private Maybe<T> upStream;

    MaybeLife(Maybe<T> upStream, Scope scope, boolean onMain) {
        super(scope, onMain);
        this.upStream = upStream;
    }

    @Override
    public final Disposable subscribe() {
        return subscribe(Functions.emptyConsumer(), Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
    }

    public final Disposable subscribe(Consumer<? super T> onSuccess) {
        return subscribe(onSuccess, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
    }

    public final Disposable subscribe(Consumer<? super T> onSuccess, Consumer<? super Throwable> onError) {
        return subscribe(onSuccess, onError, Functions.EMPTY_ACTION);
    }

    public final Disposable subscribe(Consumer<? super T> onSuccess, Consumer<? super Throwable> onError,
                                      Action onComplete) {
        ObjectHelper.requireNonNull(onSuccess, "onSuccess is null");
        ObjectHelper.requireNonNull(onError, "onError is null");
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");
        return subscribeWith(new MaybeCallbackObserver<T>(onSuccess, onError, onComplete));
    }

    @Override
    public final void subscribe(MaybeObserver<? super T> observer) {
        ObjectHelper.requireNonNull(observer, "observer is null");

        observer = RxJavaPlugins.onSubscribe(upStream, observer);

        ObjectHelper.requireNonNull(observer, "The RxJavaPlugins.onSubscribe hook returned a null MaybeObserver. Please check the handler provided to RxJavaPlugins.setOnMaybeSubscribe for invalid null returns. Further reading: https://github.com/ReactiveX/RxJava/wiki/Plugins");

        try {
            subscribeActual(observer);
        } catch (NullPointerException ex) {
            throw ex;
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            NullPointerException npe = new NullPointerException("subscribeActual failed");
            npe.initCause(ex);
            throw npe;
        }
    }

    private void subscribeActual(MaybeObserver<? super T> observer) {
        Maybe<T> upStream = this.upStream;
        if (onMain) {
            upStream = upStream.observeOn(AndroidSchedulers.mainThread());
        }
        upStream.onTerminateDetach().subscribe(new LifeMaybeObserver<>(observer, scope));
    }
}
