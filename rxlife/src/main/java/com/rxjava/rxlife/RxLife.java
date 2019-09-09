package com.rxjava.rxlife;

import android.arch.lifecycle.Lifecycle.Event;
import android.arch.lifecycle.LifecycleOwner;
import android.view.View;

import org.reactivestreams.Publisher;

import io.reactivex.*;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.parallel.ParallelFlowable;

/**
 * User: ljx
 * Date: 2019/4/3
 * Time: 14:41
 */
public final class RxLife {

    public static <T> RxConverter<T> as(LifecycleOwner owner) {
        return as(owner, Event.ON_DESTROY, false);
    }

    public static <T> RxConverter<T> as(LifecycleOwner owner, Event event) {
        return as(owner, event, false);
    }

    public static <T> RxConverter<T> asOnMain(LifecycleOwner owner) {
        return as(owner, Event.ON_DESTROY, true);
    }

    public static <T> RxConverter<T> asOnMain(LifecycleOwner owner, Event event) {
        return as(owner, event, true);
    }

    private static <T> RxConverter<T> as(LifecycleOwner owner, Event event, boolean onMain) {
        return as(LifecycleScope.from(owner, event), onMain);
    }

    public static <T> RxConverter<T> as(View view) {
        return as(ViewScope.from(view), false);
    }

    public static <T> RxConverter<T> asOnMain(View view) {
        return as(ViewScope.from(view), true);
    }

    public static <T> RxConverter<T> as(Scope scope) {
        return as(scope, false);
    }

    public static <T> RxConverter<T> asOnMain(Scope scope) {
        return as(scope, true);
    }

    private static <T> RxConverter<T> as(Scope scope, boolean onMain) {
        return new RxConverter<T>() {

            @Override
            public ObservableLife<T> apply(Observable<T> upstream) {
                return new ObservableLife<>(upstream, scope, onMain);
            }

            @Override
            public FlowableLife<T> apply(Flowable<T> upstream) {
                return new FlowableLife<>(upstream, scope, onMain);
            }

            @Override
            public ParallelFlowableLife<T> apply(ParallelFlowable<T> upstream) {
                return new ParallelFlowableLife<>(upstream, scope, onMain);
            }

            @Override
            public MaybeLife<T> apply(Maybe<T> upstream) {
                return new MaybeLife<>(upstream, scope, onMain);
            }

            @Override
            public SingleLife<T> apply(Single<T> upstream) {
                return new SingleLife<>(upstream, scope, onMain);
            }

            @Override
            public CompletableLife apply(Completable upstream) {
                return new CompletableLife(upstream, scope, onMain);
            }
        };
    }
}
