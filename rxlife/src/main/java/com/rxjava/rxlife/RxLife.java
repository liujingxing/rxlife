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
        return as(ScopeLifecycle.from(owner, event), onMain);
    }

    public static <T> RxConverter<T> as(View view) {
        return as(ScopeView.from(view), false);
    }

    public static <T> RxConverter<T> asOnMain(View view) {
        return as(ScopeView.from(view), true);
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

    @Deprecated
    public static <T> RxLifeOperator<T> lift(LifecycleOwner lifecycleOwner) {
        return lift(lifecycleOwner, Event.ON_DESTROY);
    }

    @Deprecated
    public static <T> RxLifeOperator<T> lift(LifecycleOwner lifecycleOwner, Event event) {
        return new RxLifeOperator<>(ScopeLifecycle.from(lifecycleOwner, event));
    }

    @Deprecated
    public static <T> RxLifeTransformer<T> compose(LifecycleOwner owner) {
        return compose(owner, Event.ON_DESTROY);
    }

    @Deprecated
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

    @Deprecated
    public static <T> RxLifeTransformer<T> composeOnMain(LifecycleOwner owner) {
        return composeOnMain(owner, Event.ON_DESTROY);
    }

    @Deprecated
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
