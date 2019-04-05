package com.rxjava.rxlife;

import android.arch.lifecycle.Lifecycle.Event;
import android.arch.lifecycle.LifecycleOwner;

import org.reactivestreams.Subscriber;

import io.reactivex.*;

/**
 * User: ljx
 * Date: 2019/4/5
 * Time: 13:46
 */
public final class RxLifeOperator<T> implements
        FlowableOperator<T, T>,
        ObservableOperator<T, T>,
        SingleOperator<T, T>,
        MaybeOperator<T, T>,
        CompletableOperator {

    private LifecycleOwner mLifecycleOwner;
    private Event          mEvent;

    RxLifeOperator(LifecycleOwner lifecycleOwner, Event event) {
        mLifecycleOwner = lifecycleOwner;
        mEvent = event;
    }

    @Override
    public Subscriber<? super T> apply(Subscriber<? super T> subscriber) throws Exception {
        return new LifeSubscriber<>(mLifecycleOwner, subscriber, mEvent);
    }

    @Override
    public Observer<? super T> apply(Observer<? super T> observer) throws Exception {
        return new LifeObserver<>(mLifecycleOwner, observer, mEvent);
    }

    @Override
    public SingleObserver<? super T> apply(SingleObserver<? super T> observer) throws Exception {
        return new LifeSingleObserver<>(mLifecycleOwner, observer, mEvent);
    }

    @Override
    public MaybeObserver<? super T> apply(MaybeObserver<? super T> observer) throws Exception {
        return new LifeMaybeObserver<>(mLifecycleOwner, observer, mEvent);
    }

    @Override
    public CompletableObserver apply(CompletableObserver observer) throws Exception {
        return new LifeCompletableObserver(mLifecycleOwner, observer, mEvent);
    }

}
