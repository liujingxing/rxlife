package com.rxjava.rxlife;

import android.view.View;

import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleOwner;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.parallel.ParallelFlowable;


/**
 * User: ljx
 * Date: 2019/4/3
 * Time: 14:41
 */
public final class RxLife {

    public static <T> RxConverter<T> to(LifecycleOwner owner) {
        return to(owner, Event.ON_DESTROY, false);
    }

    public static <T> RxConverter<T> to(LifecycleOwner owner, Event event) {
        return to(owner, event, false);
    }

    public static <T> RxConverter<T> toMain(LifecycleOwner owner) {
        return to(owner, Event.ON_DESTROY, true);
    }

    public static <T> RxConverter<T> toMain(LifecycleOwner owner, Event event) {
        return to(owner, event, true);
    }

    private static <T> RxConverter<T> to(LifecycleOwner owner, Event event, boolean onMain) {
        return to(LifecycleScope.from(owner, event), onMain);
    }

    public static <T> RxConverter<T> to(View view) {
        return to(ViewScope.from(view, false), false);
    }

    /**
     * @param view         目标View
     * @param ignoreAttach 忽略View是否添加到Window，默认为false，即不忽略
     * @return RxConverter
     */
    public static <T> RxConverter<T> to(View view, boolean ignoreAttach) {
        return to(ViewScope.from(view, ignoreAttach), false);
    }

    public static <T> RxConverter<T> toMain(View view) {
        return to(ViewScope.from(view, false), true);
    }

    /**
     * @param view         目标View
     * @param ignoreAttach 忽略View是否添加到Window，默认为false，即不忽略
     * @return RxConverter
     */
    public static <T> RxConverter<T> toMain(View view, boolean ignoreAttach) {
        return to(ViewScope.from(view, ignoreAttach), true);
    }

    public static <T> RxConverter<T> to(Scope scope) {
        return to(scope, false);
    }

    public static <T> RxConverter<T> toMain(Scope scope) {
        return to(scope, true);
    }

    private static <T> RxConverter<T> to(Scope scope, boolean onMain) {
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


    /**
     * @deprecated please user {@link RxLife#to(LifecycleOwner)} instead
     */
    @Deprecated
    public static <T> RxConverter<T> as(LifecycleOwner owner) {
        return to(owner, Event.ON_DESTROY, false);
    }

    /**
     * @deprecated please user {@link RxLife#to(LifecycleOwner, Event)} instead
     */
    @Deprecated
    public static <T> RxConverter<T> as(LifecycleOwner owner, Event event) {
        return to(owner, event, false);
    }

    /**
     * @deprecated please user {@link RxLife#to(LifecycleOwner)} instead
     */
    @Deprecated
    public static <T> RxConverter<T> asOnMain(LifecycleOwner owner) {
        return to(owner, Event.ON_DESTROY, true);
    }

    public static <T> RxConverter<T> asOnMain(LifecycleOwner owner, Event event) {
        return to(owner, event, true);
    }

    private static <T> RxConverter<T> as(LifecycleOwner owner, Event event, boolean onMain) {
        return to(LifecycleScope.from(owner, event), onMain);
    }

    /**
     * @deprecated please user {@link RxLife#to(LifecycleOwner)} instead
     */
    @Deprecated
    public static <T> RxConverter<T> as(View view) {
        return to(ViewScope.from(view, false), false);
    }

    /**
     * @deprecated please user {@link RxLife#to(LifecycleOwner)} instead
     */
    @Deprecated
    public static <T> RxConverter<T> as(View view, boolean ignoreAttach) {
        return to(ViewScope.from(view, ignoreAttach), false);
    }

    /**
     * @deprecated please user {@link RxLife#to(LifecycleOwner)} instead
     */
    @Deprecated
    public static <T> RxConverter<T> asOnMain(View view) {
        return to(ViewScope.from(view, false), true);
    }

    /**
     * @deprecated please user {@link RxLife#to(LifecycleOwner)} instead
     */
    @Deprecated
    public static <T> RxConverter<T> asOnMain(View view, boolean ignoreAttach) {
        return to(ViewScope.from(view, ignoreAttach), true);
    }

    /**
     * @deprecated please user {@link RxLife#to(LifecycleOwner)} instead
     */
    @Deprecated
    public static <T> RxConverter<T> as(Scope scope) {
        return to(scope, false);
    }

    /**
     * @deprecated please user {@link RxLife#to(LifecycleOwner)} instead
     */
    @Deprecated
    public static <T> RxConverter<T> asOnMain(Scope scope) {
        return to(scope, true);
    }

    /**
     * @deprecated please user {@link RxLife#to(LifecycleOwner)} instead
     */
    @Deprecated
    private static <T> RxConverter<T> as(Scope scope, boolean onMain) {
        return to(scope, onMain);
    }
}
